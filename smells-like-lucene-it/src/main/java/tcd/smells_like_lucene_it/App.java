package tcd.smells_like_lucene_it;
import static tcd.constants.FilePathPatterns.*;
import tcd.parse_to_index.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
        // Get Each File for Financial Times
        List<String> financialTimesFiles = getFinancialTimesFiles();
        // Get Each file for Federal Register
        List<String> federalRegisterFiles = getAllFederalRegisterFiles();
        CreateIndex CI = new CreateIndex();
        
        List<CustomDocument> documents = new ArrayList<CustomDocument>();
        // Parse Financial Times
        for(String fileName: financialTimesFiles) {
        	DocumentParserSGML documentParser = new DocumentParserSGML();
            documents = documentParser.parseFT(fileName);
            CI.IndexFT(documents);
        }
        
        // Parse Federal Register
        for(String fileName: federalRegisterFiles) {
        	DocumentParserSGML documentParser = new DocumentParserSGML();
            documents = documentParser.parseFR(fileName);
            CI.IndexFT(documents);
        }

        //TODO move this to within for-loop for Federal Register and Financial Times

        

        System.out.println("Parsing and Indexing COMPLETE");

    }
}
