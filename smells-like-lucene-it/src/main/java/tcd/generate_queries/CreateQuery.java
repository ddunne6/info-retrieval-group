package tcd.generate_queries;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.io.FileWriter;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import tcd.analyzers.MyCustomAnalyzer;
import static tcd.constants.QueryConstants.*;
import static tcd.constants.FilePathPatterns.*;

public class CreateQuery {
	private static File topicsFile = new File("../topics");
	private static Elements topics;
	private static final int MAX_RESULTS = 1000;
	private String runName="";
	private Similarity runSimilarity = new BM25Similarity();
	private Float contentBoost = 5.35f;
	private Float titleBoost = 1f;
	//private Float topicTitleBoost = 2.35f;
	private Float topicTitleBoost = 4f;
	private Float topicDescriptionBoost = 2.5f;
	private Float topicNarrativeBoost = 1f;
	
	public CreateQuery(String runName, Similarity runSimilarity) {
		this.runName=runName;
		this.runSimilarity = runSimilarity;		
	}
	
	public CreateQuery(String runName, Similarity runSimilarity, String boostString, Float customBoost1, Float customBoost2) {
		this.runName=runName;
		this.runSimilarity = runSimilarity;
		
		if(boostString.equals("field")) {
		System.out.println("Boosting Content Field");
		this.contentBoost = customBoost1;
		this.titleBoost = customBoost2;
		
		} else if(boostString.equals("topic")) {		
			System.out.println("Boosting Topic");
			this.topicTitleBoost = customBoost1;
			this.topicDescriptionBoost = customBoost2;
			
		}
	}
	
	public void queryTopics() throws IOException, ParseException {

		// set up file writer for query results
		File resultsFile = new File("../results/results_file"+runName);
        FileWriter fileWriter = new FileWriter(resultsFile);
		
		// set directory, directory reader & index searcher
		Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY_CORPUS));
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);		
		isearcher.setSimilarity(runSimilarity);

		// Parse topics file with Jsoup & select topic tags
		org.jsoup.nodes.Document doc = Jsoup.parse(topicsFile, "UTF-8", "");
		topics = doc.body().select("top");

		QueryParser parser = new QueryParser("content", new EnglishAnalyzer());
		HashMap<String, Float> fieldBoosts = new HashMap<String, Float>(); 
		//fieldBoosts.put("title", titleBoost);
		
		fieldBoosts.put(CONTENT, contentBoost);	
		MultiFieldQueryParser multiqp = new MultiFieldQueryParser(new String[] { CONTENT, TITLE },new MyCustomAnalyzer(), fieldBoosts);
		//QueryParser titleParser = new QueryParser("title", new MyCustomAnalyzer());

		// Iterate through topic tags & structure queries
    	for(Element t : topics) {
    		String title = t.select("title").text();
    		String description = t.select("desc").text();
			String narrative = t.select("narr").text();
    		String queryId = t.select("num").text();
    		
    		//System.out.println(narrative);

    		// little bit of preprocessing
    		int startIndex = "Description:".length() + 1;
    		int indexOfNarr = description.indexOf("Narrative:");
    		description = description.substring(0, indexOfNarr);
    		description = description.substring(startIndex, description.length());
    		
    		int queryNumIndex = queryId.indexOf("Number: ") + "Number: ".length();
    		queryId = queryId.substring(queryNumIndex, queryNumIndex+4).trim();
    		//System.out.println(queryId);

			int narrLength = "Narrative:".length() + 1;
			//System.out.println(narrative);
			narrative = narrative.substring(narrLength, narrative.length());
			//System.out.println(narrative);
			//narrative = narrative.replace("\r","").replace("\n","");
			
			//Testing Editing the Narrative text to take out any clauses that specify what is not relevant
			String[] narrSentences = narrative.split("\\.");
			
			String newNarr = "";
			String mustNotNarr = "";
			
			for(int i = 0; i < narrSentences.length; i++){
				String[] narrClauses = narrSentences[i].split(",");
				for(int j = 0; j < narrClauses.length; j++) {
					if(!narrClauses[j].contains("not relevant")) {
						newNarr += narrClauses[j];
					}
					else if(narrClauses[j].contains("not relevant")) {
						mustNotNarr += narrClauses[j];
					}
				}
				if(newNarr != "")
					newNarr += ".";
				else
					newNarr = ".";
				
				if(mustNotNarr != "")
					mustNotNarr += ".";
			}

			System.out.println(narrative);
			System.out.println("RELEVANT");
			System.out.println(newNarr);
			System.out.println("NOT RELEVANT");
			System.out.println(mustNotNarr);
			//Previously used newNarr in query, not narrative string
			
			String fullDescriptionForQuery = description + newNarr;
			//System.out.println(fullDescriptionForQuery);
			
			Query topicTitleQuery = multiqp.parse(MultiFieldQueryParser.escape(title));
			Query topicDescriptionQuery = multiqp.parse(MultiFieldQueryParser.escape(description));
			//Query topicNarrativeQuery = multiqp.parse(MultiFieldQueryParser.escape(narrative));
			Query topicNarrativeQuery = multiqp.parse(MultiFieldQueryParser.escape(newNarr));
			
			Query boostedTopicTitle = new BoostQuery(topicTitleQuery, topicTitleBoost);
			Query boostedTopicDescription = new BoostQuery(topicDescriptionQuery, topicDescriptionBoost);
			Query boostedTopicNarrative = new BoostQuery(topicNarrativeQuery, topicNarrativeBoost);
			
			
			
			//String forOther = newNarr + " " + title + " " + description;
			//Query testOther = new TermQuery(new Term(OTHER, forOther));
			//Query testOtherForTitle = new TermQuery(new Term(TITLE, forOther));
			//Query mustNotQuery = new TermQuery(new Term(OTHER, mustNotNarr));

			//Query boostedQ1 = new BoostQuery(q1, 1.5F);
			//Query boostedQ2 = new BoostQuery(q2, 2.5F);
			//Query boostedOther = new BoostQuery(testOther, 2.5F);
			//Query boostedTestOtherForTitle = new BoostQuery(testOtherForTitle, 2.5F);
			//Query boostedMustNot = new BoostQuery(mustNotQuery, 1.5F);

			BooleanQuery.Builder newBooleanQuery = new BooleanQuery.Builder();

			
			newBooleanQuery.add(boostedTopicTitle, BooleanClause.Occur.SHOULD);
			newBooleanQuery.add(boostedTopicDescription, BooleanClause.Occur.SHOULD);
			newBooleanQuery.add(boostedTopicNarrative, BooleanClause.Occur.SHOULD);
			
			//Query newQuery = parser.parse(QueryParserBase.escape(newBooleanQuery.build().toString()));
			Query newQuery = newBooleanQuery.build();
			//System.out.println(newQuery.toString());
			
			
			// Get query results from the index searcher
            ScoreDoc[] hits = isearcher.search(newQuery, MAX_RESULTS).scoreDocs;
            //System.out.println(hits.length);

            for (int i = 0; i < hits.length; i++) {
            	
            	int rank = i+1;
                //append query results to results file
                Document hitDoc = isearcher.doc(hits[i].doc);
                String appendToResults = queryId + " Q0 " + hitDoc.get(DOCID) + " " + rank + " " + hits[i].score + " STANDARD\n";
                //System.out.println(appendToResults);
				fileWriter.write(appendToResults);
            }
    	}
		fileWriter.close();
    	System.out.println("Querying Done ");
	}
}
