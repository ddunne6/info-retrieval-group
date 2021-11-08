package tcd.generate_queries;

import java.io.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.index.Term;

public class CreateQuery {
	/*
	 * Need to Init:
	 * 	- max results per query
	 * 	- index dir
	 *  - analyzer (English Analyzer used in place of CustomAnalyzr)
	 *  - isearcher
	 *  - similarity score
	 */
	private static File topicsFile = new File("../topics");
	private static Elements topics;
		
	public static void main(String[] args) throws IOException, ParseException{
		// Parse topics file with Jsoup & select topic tags
		Document doc = Jsoup.parse(topicsFile, "UTF-8", "");
		topics = doc.body().select("top");
		QueryParser parser = new QueryParser("content", new EnglishAnalyzer());
		
		// Iterate through topic tags & structure queries
    	for(Element t : topics) {
        	//Query Structure: 'title:<title> AND content:<desc>' to start, then alter if needed/try other expressions
    		String title = t.select("title").text();
    		String description = t.select("desc").text();
    		
    		int startIndex = "Description:".length() + 1;
    		int indexOfNarr = description.indexOf("Narrative:");
    		description = description.substring(0, indexOfNarr);
    		description = description.substring(startIndex, description.length());
    		
    		
    		//Two possible ways to do query (where more than one field is specified...)
    		
    		//With Query Parser
    		String query = "title:" + title + " AND content:" + description;
    		Query q = parser.parse(query);
    		//isearcher.search(q);

    		
    		//With Boolean Query
    		Query titleQuery = new TermQuery(new Term("title", title));
    		Query descQuery = new TermQuery(new Term("content", description));
    		BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
    		booleanQuery.add(titleQuery, BooleanClause.Occur.MUST);
    		booleanQuery.add(descQuery, BooleanClause.Occur.MUST);
    		booleanQuery.build();
    		//isearcher.search(booleanQuery.build())
    			
    	}
    	System.out.println("Done");
    }
}
