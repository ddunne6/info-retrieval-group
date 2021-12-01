package tcd.smells_like_lucene_it;

import static tcd.constants.FilePathPatterns.*;
import static tcd.constants.QueryConstants.*;

import tcd.generate_queries.CreateQuery;
import tcd.index.*;
import tcd.parse.CustomDocument;
import tcd.parse.DocumentParserSGML;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;


public class App {
	
	private static String runName = "";
	private static Similarity runSimilarity = new BM25Similarity();
	private static float stopwordThreshold = 999f;
	//private static Float customBoost = 5.35f;


	public static void main(String[] args) {
		if(args.length < 1) {
			
			try {
				generateQueries();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			if("index".equals(args[0])) {
				
				File directory = new File(INDEX_DIRECTORY_CORPUS);
				if (!directory.exists()) {
					try {
						parseAndIndexCorpus();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					// Index Already Created
					System.out.println("Index already created. Delete index directory at '"+INDEX_DIRECTORY_CORPUS+"' to allow re-indexing.");
				}
				
			}
			else if("query".equals(args[0])) {
				try {
					generateQueries();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if("analyze-similarity".equals(args[0])) {
				
				
				System.out.println("Analyzing BM25 Similarity");
				
				float[] k1Array = {0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f,1.1f, 1.2f, 1.3f, 1.4f, 1.5f, 1.6f, 1.7f, 1.8f, 1.9f, 2.0f};
				float[] bArray = {0.3f, 0.35f, 0.4f, 0.45f, 0.5f, 0.55f, 0.6f, 0.65f, 0.7f, 0.75f, 0.8f, 0.85f, 0.9f, 0.95f, 1.0f};
				
				for(float k1param : k1Array) {
					for (float bparam : bArray) {
						

						runSimilarity = new BM25Similarity(k1param, bparam);
						runName = "_BM25_k1_"+k1param+"_b_"+bparam;
						System.out.println(runName);
							
							try {
								parseAndIndexCorpus();
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							try {
								generateQueries();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					
							
							//clearTempDirectory(new File(INDEX_DIRECTORY_CORPUS+runName));
					}	
				}
			}
			
			else if("analyze-boosts".equals(args[0])) {
				
				
				System.out.println("Analyzing Boosts");
				
				
//				float[] contentBoostArray = {0.1f, 0.15f, 0.2f, 0.25f, 0.3f, 0.35f, 0.4f, 0.45f, 0.5f, 0.55f, 0.6f, 0.65f, 0.7f, 0.75f, 0.8f, 0.85f, 0.9f, 0.95f, 1.0f,
//							1.05f,1.1f, 1.15f, 1.2f, 1.25f, 1.3f, 1.35f, 1.4f, 1.45f, 1.5f, 1.55f, 1.6f, 1.65f, 1.7f, 1.75f, 1.8f, 1.85f, 1.9f, 1.95f, 2.0f,
//							2.05f,2.1f, 2.15f, 2.2f, 2.25f, 2.3f, 2.35f, 2.4f, 2.45f, 2.5f, 2.55f, 2.6f, 2.65f, 2.7f, 2.75f, 2.8f, 2.85f, 2.9f, 2.95f, 3.0f};
				
				//float[] contentBoostArray = {4f, 4.3f, 4.6f, 5f, 5.5f, 6f, 6.5f};
				//float[] contentBoostArray = {7f, 7.5f, 8f, 8.5f, 9f, 9.5f, 10f};
//				float[] contentBoostArray = {4.05f,4.1f, 4.15f, 4.2f, 4.25f, 4.3f, 4.35f, 4.4f, 4.45f, 4.5f, 4.55f, 4.6f, 4.65f, 4.7f, 4.75f, 4.8f, 4.85f, 4.9f, 4.95f, 5.0f,
//											5.05f,5.1f, 5.15f, 5.2f, 5.25f, 5.3f, 5.35f, 5.4f, 5.45f, 5.5f, 5.55f, 5.6f, 5.65f, 5.7f, 5.75f, 5.8f, 5.85f, 5.9f, 5.95f, 6.0f,
//											6.05f,6.1f, 6.15f, 6.2f, 6.25f, 6.3f, 6.35f, 6.4f, 6.45f, 6.5f, 6.55f, 6.6f, 6.65f, 6.7f, 6.75f, 6.8f, 6.85f, 6.9f, 6.95f, 7.0f};
				//float[] contentBoostArray = {1.4f, 1.45f, 1.5f, 1.55f, 1.6f, 1.65f, 1.7f, 1.75f, 1.8f, 1.85f, 1.9f, 1.95f, 2.0f,
				//2.05f,2.1f, 2.15f, 2.2f, 2.25f, 2.3f, 2.35f, 2.4f, 2.45f, 2.5f, 2.55f, 2.6f, 2.65f, 2.7f, 2.75f, 2.8f, 2.85f, 2.9f, 2.95f, 3.0f};
				
				float[] contentBoostArray = {0.5f, 1f, 1.5f, 2f, 2.5f, 3f, 3.5f, 4f, 4.5f, 5f, 5.5f, 6f, 6.5f, 7f, 7.5f, 8f, 8.5f, 9f};
				
				for(float runBoost1 : contentBoostArray) {
					for(float runBoost2 : contentBoostArray) {	

						//runSimilarity = new BM25Similarity(k1param, bparam);
					
						//boostType sets what we're boosting
						String boostType = "topic";
						
						//customBoost is the boost value that we're looping through
						
						runName = "_TopicBoosts_TitleBoost_"+runBoost1+"_DescriptionBoost_"+runBoost2;
						System.out.println(runName);
							
							
							try {
								generateQueriesAnalysis(boostType, runBoost1, runBoost2);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					
							
							//clearTempDirectory(new File(INDEX_DIRECTORY_CORPUS+runName));
					}		
				}
			}
			else if ("analyze-stopwords".equals(args[0])) {
				float[] stopwordThresholds = {90f, 0f, 80f, 70f, 60f, 50f, 40f, 30f, 20f, 10f, 0f};
				for(float threshold : stopwordThresholds) {
					runName = "stopwords_at_" + threshold;
					stopwordThreshold = threshold;
					System.out.println(runName);
					
					try {
						parseAndIndexCorpus();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					try {
						CreateQuery createQuery = new CreateQuery(runName, runSimilarity, threshold);
						createQuery.queryTopics();
					} catch (Exception e) {
						e.printStackTrace();
					}
					//clearTempDirectory(new File(INDEX_DIRECTORY_CORPUS));
				}
			}
			else {
				System.out.println("Invalid arguments");
			}
		}
	}

	private static void parseAndIndexCorpus() throws IOException {
		// Get Each file for Financial Times
		List<String> financialTimesFiles = getFinancialTimesFiles();
		// Get Each file for Federal Register
		List<String> federalRegisterFiles = getAllFederalRegisterFiles();
		// Get Each file for Foreign Broadcast Information Service
		List<String> foreignBroadcastISFiles = getAllForeignBroadcastISFiles();
		// Get Each file for Los Angeles Times
		List<String> losAngelosTimesFiles = getAllLATimesFiles();
		
		CreateIndex createIndex;
		// Clear temporary folder
		clearTempDirectory(new File(TEMP_FOLDER));
		if (stopwordThreshold <= 100f) {
			createIndex = new CreateIndex(runName, runSimilarity, stopwordThreshold);
		}
		else {
			createIndex = new CreateIndex(runName, runSimilarity);
		}

		List<CustomDocument> documents = new ArrayList<CustomDocument>();

		System.out.println("STARTING Parsing and Indexing...");
		// Parse Financial Times
		System.out.println("Parsing Financial Times");
		for (String fileName : financialTimesFiles) {
			DocumentParserSGML documentParser = new DocumentParserSGML();
			documents = documentParser.parseFT(fileName);
			createIndex.indexCorpus(documents);
		}

		// Parse Federal Register
		System.out.println("Parsing Federal Register");
		for (String fileName : federalRegisterFiles) {
			DocumentParserSGML documentParser = new DocumentParserSGML();
			documents = documentParser.parseFR(fileName);
			createIndex.indexCorpus(documents);
		}

		// Parse Foreign Broadcast Information Service
		System.out.println("Parsing FBIS documents");
		for (String fileName : foreignBroadcastISFiles) {
			
			DocumentParserSGML documentParser = new DocumentParserSGML();
			documents = documentParser.parseFBIS(fileName);
			createIndex.indexCorpus(documents);
		}

		// Parse Los Angeles Times
		System.out.println("Parsing LA Times documents");
		for (String fileName : losAngelosTimesFiles) {
			DocumentParserSGML documentParser = new DocumentParserSGML();
			documents = documentParser.parseLA(fileName);
			createIndex.indexCorpus(documents);
		}

		// Commit changes and close everything
		createIndex.closeIndex();

		System.out.println("Parsing and Indexing COMPLETE");
	}
	
	//This method takes a boostString and boostFloat for analyzing performance over a range of boost values
	//Only called in when the 'analyze-boost' argument is passed.
	private static void generateQueriesAnalysis(String boostString, Float boostFloat1, Float boostFloat2) throws IOException, ParseException {
		CreateQuery createQuery = new CreateQuery(runName, runSimilarity, boostString,boostFloat1,boostFloat2);
		createQuery.queryTopics();
	}
	
	private static void generateQueries() throws IOException, ParseException {
		CreateQuery createQuery = new CreateQuery(runName, runSimilarity);
		createQuery.queryTopics();
	}

	private static void clearTempDirectory(File tempFolder) {
		try {
			FileUtils.deleteDirectory(tempFolder);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
