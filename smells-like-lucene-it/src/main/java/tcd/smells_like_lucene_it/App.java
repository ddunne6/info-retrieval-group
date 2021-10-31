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
        
        
        List<CustomDocument> documents = new ArrayList<CustomDocument>();
        // Parse Financial Times
        for(String fileName: financialTimesFiles) {
        	DocumentParserSGML documentParser = new DocumentParserSGML();
            documents = documentParser.parseFT(fileName);
        }
        
        // Parse Federal Register
        for(String fileName: federalRegisterFiles) {
        	DocumentParserSGML documentParser = new DocumentParserSGML();
            documents = documentParser.parseFR(fileName);
        }
        
        //TODO move this to within for-loop for Federal Register and Financial Times
        CreateIndex CI = new CreateIndex();
        CI.IndexFT(documents);

        System.out.println("Parsing and Indexing COMPLETE");

    }
}
