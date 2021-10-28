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
        List<CustomDocument> documents = new ArrayList<CustomDocument>();
        // Parse each file
        for(String fileName: financialTimesFiles) {
        	DocumentParserSGML documentParser = new DocumentParserSGML();
            documents = documentParser.parse(fileName);
            // TODO index the documents
        }
        
        CreateIndex CI = new CreateIndex();
        CI.IndexFT(documents);

        System.out.println("Parsing and Indexing COMPLETE");

    }
}
