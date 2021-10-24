package tcd.smells_like_lucene_it;
import static tcd.constants.FilePathPatterns.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        // Parse Each Document for Financial Times
        List<String> financialTimesFiles = getFinancialTimesFiles();
        for(String fileName: financialTimesFiles) {
        	System.out.println(fileName);
        }
    }
}
