package tcd.generate_queries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import tcd.analyzers.MyCustomAnalyzer;
import tcd.parse.FileDecorator;

import static tcd.constants.QueryConstants.*;
import static tcd.constants.SGMLTags.XML_DUMMY;
import static tcd.constants.SGMLTags.closingTag;
import static tcd.constants.SGMLTags.openingTag;
import static tcd.constants.FilePathPatterns.*;

public class CreateQuery {
	private static File topicsFile = new File("../topics");
	private static Elements topics;
	private static final int MAX_RESULTS = 1000;
	private String runName="";
	private Similarity runSimilarity = new BM25Similarity(0.6f,0.85f);
	//private Similarity runSimilarity = new BM25Similarity();
	private Float contentBoost = 6f;
	private Float titleBoost = 1f;
	private Float otherBoost = 0.5f;
	private Float topicTitleBoost = 4f;
	private Float topicDescriptionBoost = 2.5f;
	private Float topicNarrativeBoost = 1f;
	private Float geoBoost = 2f;
	
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
	public CreateQuery(String runName, Similarity runSimilarity, String boostString, Float customBoost1, Float customBoost2, Float customBoost3) {
		this.runName=runName;
		this.runSimilarity = runSimilarity;
		
		if(boostString.equals("field")) {
		System.out.println("Boosting Content Field");
		this.contentBoost = customBoost1;
		this.titleBoost = customBoost2;
		this.otherBoost = customBoost3;
		
		} else if(boostString.equals("topic")) {		
			System.out.println("Boosting Topic");
			this.topicTitleBoost = customBoost1;
			this.topicDescriptionBoost = customBoost2;
			this.topicNarrativeBoost = customBoost3;
			
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
		
		// Decorate file before parsing
		String tempFile = createTempFile(topicsFile);
		FileDecorator fileDecorator = new FileDecorator(tempFile);
		fileDecorator.replaceAcronyms();
		fileDecorator.decorate();
		
		
		// Parse topics file with Jsoup & select topic tags
		org.jsoup.nodes.Document doc = Jsoup.parse(new File(tempFile), "UTF-8", "");
		topics = doc.body().select("top");

		QueryParser parser = new QueryParser("content", new MyCustomAnalyzer());
		HashMap<String, Float> fieldBoosts = new HashMap<String, Float>(); 
		//fieldBoosts.put("title", titleBoost);
		
		fieldBoosts.put(CONTENT, contentBoost);	
		fieldBoosts.put(TITLE, titleBoost);
		fieldBoosts.put(OTHER, otherBoost);
		
		MultiFieldQueryParser multiqp = new MultiFieldQueryParser(new String[] { CONTENT, TITLE, OTHER },new MyCustomAnalyzer(), fieldBoosts);
		
		List<String> geoNames = new ArrayList<String>(); 
		String file = "countries.txt";
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	geoNames.add(line);
		    }
		}
		
		// Iterate through topic tags & structure queries
    	for(Element t : topics) {
    		String title = t.select("title").text();
    		String description = t.select("desc").text();
			String narrative = t.select("narr").text();
    		String queryId = t.select("num").text();
    		
    		// little bit of preprocessing
    		int startIndex = "Description:".length() + 1;
    		int indexOfNarr = description.indexOf("Narrative:");
    		description = description.substring(0, indexOfNarr);
    		description = description.substring(startIndex, description.length());
    		
    		int queryNumIndex = queryId.indexOf("Number: ") + "Number: ".length();
    		queryId = queryId.substring(queryNumIndex, queryNumIndex+4).trim();

			int narrLength = "Narrative:".length() + 1;
			narrative = narrative.substring(narrLength, narrative.length());
			
			//Editing the Narrative text to take out any clauses that specify what is not relevant
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
			
			String geoCountriesTitle = "";
			String geoCountriesDescription = "";
			String geoCountriesString = "";
			
			for (String name : geoNames) {
				if(title.contains(name))
				{

					if (!geoCountriesString.contains(name)) {
						geoCountriesString += name+" ";
					}
				}
				if(description.contains(name))
				{
					geoCountriesDescription += name+" ";
					
					if (!geoCountriesString.contains(name)) {
						geoCountriesString += name+" ";
					}
				}
			}
			
			Query topicTitleQuery = multiqp.parse(MultiFieldQueryParser.escape(title));
			Query topicDescriptionQuery = multiqp.parse(MultiFieldQueryParser.escape(description));
			Query topicNarrativeQuery = multiqp.parse(MultiFieldQueryParser.escape(newNarr));

			Query boostedTopicTitle = new BoostQuery(topicTitleQuery, topicTitleBoost);
			Query boostedTopicDescription = new BoostQuery(topicDescriptionQuery, topicDescriptionBoost);
			Query boostedTopicNarrative = new BoostQuery(topicNarrativeQuery, topicNarrativeBoost);
			
			BooleanQuery.Builder newBooleanQuery = new BooleanQuery.Builder();
			
			newBooleanQuery.add(boostedTopicTitle, BooleanClause.Occur.SHOULD);
			newBooleanQuery.add(boostedTopicDescription, BooleanClause.Occur.SHOULD);
			newBooleanQuery.add(boostedTopicNarrative, BooleanClause.Occur.SHOULD);
			
			if (!geoCountriesString.equals("")) {
				Query geoQuery = multiqp.parse(MultiFieldQueryParser.escape(geoCountriesString));
				Query boostedGeoQuery = new BoostQuery(geoQuery, geoBoost);
				newBooleanQuery.add(boostedGeoQuery, BooleanClause.Occur.SHOULD);
			}
			
			Query newQuery = newBooleanQuery.build();

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
	
	private String createTempFile(File file) {
		String tempFile = TEMP_FOLDER + file.getName() + "-temp.xml"; // TODO return saved copy instead of redoing work
		Path path = Paths.get(tempFile);
		try {
			Files.deleteIfExists(path);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Copy this file to /temp-xml-conversion
		File copied = new File(tempFile);
		try {
			FileUtils.copyFile(file, copied);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tempFile;
	}
}
