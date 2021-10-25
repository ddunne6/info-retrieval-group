package tcd.smells_like_lucene_it;
import static tcd.constants.FilePathPatterns.*;
import tcd.parse_to_index.*;

import java.util.List;

public class App 
{
    public static void main( String[] args )
    {
        // Get Each File for Financial Times
        List<String> financialTimesFiles = getFinancialTimesFiles();

        // Parse each file
        for(String fileName: financialTimesFiles) {
        	DocumentParserSGML documentParser = new DocumentParserSGML();
            List<CustomDocument> documents = documentParser.parse(fileName);
            // TODO index the documents
        }
        System.out.println("Parsing and Indexing COMPLETE");

    }
}
