package tcd.smells_like_lucene_it;
import static tcd.constants.FilePathPatterns.*;

import java.nio.file.Files;
import java.nio.file.Paths;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        // Parse Each Document for Financial Times
        for (String pattern : FT_PATTERNS) {
        	System.out.println("First file >>> " + getFinancialTimesFile(pattern, 1));
        	int index = 1;
        	while(Files.exists(Paths.get(getFinancialTimesFile(pattern, index)))) {
        		System.out.println(getFinancialTimesFile(pattern, index));
        		index++;
        	}
        }
    }
}
