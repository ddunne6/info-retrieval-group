package tcd.smells_like_lucene_it;

import static tcd.constants.FilePathPatterns.*;

import tcd.generate_queries.CreateQuery;
import tcd.parse_to_index.*;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.queryparser.classic.ParseException;


public class App {
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
		CreateIndexDavid CI = new CreateIndexDavid();
		List<CustomDocument> documents = new ArrayList<CustomDocument>();

		System.out.println("STARTING Parsing and Indexing...");
		// Parse Financial Times
		for (String fileName : financialTimesFiles) {
			DocumentParserSGML documentParser = new DocumentParserSGML();
			documents = documentParser.parseFTLA(fileName);
			CI.indexCorpus(documents);
		}

		// Parse Federal Register
		for (String fileName : federalRegisterFiles) {
			DocumentParserSGML documentParser = new DocumentParserSGML();
			documents = documentParser.parseFR(fileName);
			CI.indexCorpus(documents);
		}

		// Parse Foreign Broadcast Information Service
		for (String fileName : foreignBroadcastISFiles) {
			DocumentParserSGML documentParser = new DocumentParserSGML();
			documents = documentParser.parseFBIS(fileName);
			CI.indexCorpus(documents);
		}

		// Parse Los Angeles Times
		for (String fileName : losAngelosTimesFiles) {
			DocumentParserSGML documentParser = new DocumentParserSGML();
			documents = documentParser.parseFTLA(fileName);
			CI.indexCorpus(documents);
		}

		// Commit changes and close everything
		CI.closeIndex();

		System.out.println("Parsing and Indexing COMPLETE");
	}
	
	private static void generateQueries() throws IOException, ParseException {
		CreateQuery createQuery = new CreateQuery();
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
