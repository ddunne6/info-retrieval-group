package tcd.generate_queries;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.io.FileWriter;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Document;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import tcd.analyzers.MyCustomAnalyzer;

public class CreateQuery {
	private static File topicsFile = new File("../topics");
	private static Elements topics;
	private static String indexDir = "../index_corpus";
	private static int MAX_RESULTS = 1000;
	
	public void queryTopics() throws IOException, ParseException{

		// set up file writer for query results
		File resultsFile = new File("../results_file");
        FileWriter fileWriter = new FileWriter(resultsFile);
		
		// set directory, directory reader & index searcher
		Directory directory = FSDirectory.open(Paths.get(indexDir));
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);		
		isearcher.setSimilarity(new BM25Similarity());

		// Parse topics file with Jsoup & select topic tags
		org.jsoup.nodes.Document doc = Jsoup.parse(topicsFile, "UTF-8", "");
		topics = doc.body().select("top");

		QueryParser parser = new QueryParser("content", new MyCustomAnalyzer());
		//QueryParser titleParser = new QueryParser("title", new MyCustomAnalyzer());

		// Iterate through topic tags & structure queries
    	for(Element t : topics) {
    		String title = t.select("title").text();
    		String description = t.select("desc").text();
    		String queryId = t.select("num").text();
    		

    		// little bit of preprocessing
    		int startIndex = "Description:".length() + 1;
    		int indexOfNarr = description.indexOf("Narrative:");
    		description = description.substring(0, indexOfNarr);
    		description = description.substring(startIndex, description.length());
    		
    		int queryNumIndex = queryId.indexOf("Number: ") + "Number: ".length();
    		queryId = queryId.substring(queryNumIndex, queryNumIndex+4).trim();
			
    		//title = "\"" + title + "\"";
	
			// Term Constructor --> new Term(field, text)
			BooleanQuery.Builder newBooleanQuery = new BooleanQuery.Builder();
			newBooleanQuery.add(new TermQuery(new Term("title", title)), BooleanClause.Occur.SHOULD);
			newBooleanQuery.add(new TermQuery(new Term("content", description)), BooleanClause.Occur.SHOULD);
			
			Query newQuery = parser.parse(newBooleanQuery.build().toString());

			// Get query results from the index searcher
            ScoreDoc[] hits = isearcher.search(newQuery, MAX_RESULTS).scoreDocs;
            System.out.println(hits.length);

            for (int i = 0; i < hits.length; i++) {
            	
                //append query results to results file
                Document hitDoc = isearcher.doc(hits[i].doc);
                String appendToResults = queryId + " Q0 " + hitDoc.get("id") + " " + i + " " + hits[i].score + " STANDARD\n";
                System.out.println(appendToResults);
				fileWriter.write(appendToResults);
            }

    	}
		fileWriter.close();
    	System.out.println("Querying Done ");
    
	}
}
