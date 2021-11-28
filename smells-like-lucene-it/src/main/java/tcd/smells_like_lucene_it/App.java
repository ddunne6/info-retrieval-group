package tcd.smells_like_lucene_it;

import static tcd.constants.FilePathPatterns.*;

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
	private static Float contentBoost = 1f;


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
			else if("analyze-field-boosts".equals(args[0])) {
				
				
				System.out.println("Analyzing Field Boosts");
				
				
				float[] contentBoostArray = {0.1f, 0.15f, 0.2f, 0.25f, 0.3f, 0.35f, 0.4f, 0.45f, 0.5f, 0.55f, 0.6f, 0.65f, 0.7f, 0.75f, 0.8f, 0.85f, 0.9f, 0.95f, 1.0f,
							1.05f,1.1f, 1.15f, 1.2f, 1.25f, 1.3f, 1.35f, 1.4f, 1.45f, 1.5f, 1.55f, 1.6f, 1.65f, 1.7f, 1.75f, 1.8f, 1.85f, 1.9f, 1.95f, 2.0f,
							2.05f,2.1f, 2.15f, 2.2f, 1.25f, 2.3f, 2.35f, 2.4f, 2.45f, 2.5f, 2.55f, 2.6f, 2.65f, 2.7f, 2.75f, 2.8f, 2.85f, 2.9f, 2.95f, 3.0f};
				
				for(float runContentBoost : contentBoostArray) {
						

						//runSimilarity = new BM25Similarity(k1param, bparam);
						contentBoost = runContentBoost;
						runName = "_FieldBoosts_contentBoost_"+contentBoost;
						System.out.println(runName);
							
							
							try {
								generateQueries();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					
							
							//clearTempDirectory(new File(INDEX_DIRECTORY_CORPUS+runName));
						
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
	
	private static void generateQueries() throws IOException, ParseException {
		CreateQuery createQuery = new CreateQuery(runName, runSimilarity, contentBoost);
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
