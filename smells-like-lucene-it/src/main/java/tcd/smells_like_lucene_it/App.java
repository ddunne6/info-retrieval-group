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
	//private static Similarity runSimilarity = new BM25Similarity(0.9f, 0.85f);
	private static Similarity runSimilarity = new BM25Similarity(0.6f,0.85f);
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
				
				float[] contentBoostArray = {1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f};
				float[] topicBoostArray = {0.5f, 1f, 1.5f, 2f, 2.5f, 3f, 3.5f, 4f, 4.5f, 5f, 5.5f, 6f, 6.5f, 7f, 7.5f, 8f, 8.5f, 9f};
				
				for(float runBoost1 : contentBoostArray) {
						for(float runBoost2 : topicBoostArray) {	
							for(float runBoost3 : topicBoostArray) {				

								//boostType sets what we're boosting
								String boostType = "field";
								runName = "_FieldBoosts_ContentBoost_"+runBoost1+"_TitleBoost_"+runBoost2+"_OtherBoost_"+runBoost3;
								
								
								//runName = "_TopicBoosts_TitleBoost_"+runBoost1+"_DescriptionBoost_"+runBoost2+"_NewNarrBoost_"+runBoost3;
								System.out.println(runName);
									
									
									try {
										generateQueriesAnalysis(boostType, runBoost1, runBoost2, runBoost3);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							
									
						
							}
					}		
				}
			}
			else if("evaluate".equals(args[0])) {
				
				System.out.println("Final Evaluation");
				
				
				runName = "_config_1";
				
				try {
					generateQueries();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				runName = "_config_2";
				String boostType = "field";
				Float contentBoost = 3f;
				Float titleBoost = 1f;
				Float otherBoost = 1f;
				
				try {
					generateQueriesAnalysis(boostType,contentBoost,titleBoost,otherBoost);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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

		// Clear temporary folder
		clearTempDirectory(new File(TEMP_FOLDER));
		CreateIndex createIndex = new CreateIndex(runName, runSimilarity);
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
	private static void generateQueriesAnalysis(String boostString, Float boostFloat1, Float boostFloat2, Float boostFloat3) throws IOException, ParseException {
		CreateQuery createQuery = new CreateQuery(runName, runSimilarity, boostString,boostFloat1,boostFloat2, boostFloat3);
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
