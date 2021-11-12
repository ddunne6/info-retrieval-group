package tcd.smells_like_lucene_it;
import static tcd.constants.FilePathPatterns.*;
import tcd.parse_to_index.*;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.apache.commons.io.FileUtils;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
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

        CreateIndex CI = new CreateIndex();
        
        List<CustomDocument> documents = new ArrayList<CustomDocument>();
        
        // Parse Financial Times
        
        for(String fileName: financialTimesFiles) {
        	DocumentParserSGML documentParser = new DocumentParserSGML();
            documents = documentParser.parseFTLA(fileName);
            CI.Indexcorpus(documents);
        }

        // Parse Federal Register
        for(String fileName: federalRegisterFiles) {
        	DocumentParserSGML documentParser = new DocumentParserSGML();
            documents = documentParser.parseFR(fileName);
            CI.Indexcorpus(documents);
        }
        
        // Parse Foreign Broadcast Information Service
        for(String fileName: foreignBroadcastISFiles) {
        	DocumentParserSGML documentParser = new DocumentParserSGML();
            documents = documentParser.parseFBIS(fileName);
            CI.Indexcorpus(documents);
        }
        
        // Parse Los Angeles Times
        for(String fileName: losAngelosTimesFiles) {
        	DocumentParserSGML documentParser = new DocumentParserSGML();
            documents = documentParser.parseFTLA(fileName);
            CI.IndexFT(documents);
        }
        System.out.println("Parsing and Indexing COMPLETE");

    }

	private static void clearTempDirectory(File tempFolder) {
		try {
			FileUtils.deleteDirectory(tempFolder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
