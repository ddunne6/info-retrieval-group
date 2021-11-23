package tcd.analyzers;

import org.apache.lucene.analysis.synonym.SolrSynonymParser;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.util.CharsRef;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class MySynonymMap{
    //Adding a synonym Map function (checking)
    private static SynonymMap createSynonymMap() throws FileNotFoundException {
        SynonymMap synMap = new SynonymMap(null, null, 0);
        Scanner readInput = new Scanner(new File("../capitals.txt"));
        Scanner input2 = new Scanner(new File("../uk_us.txt"));

        try {
        	final SynonymMap.Builder builder = new SynonymMap.Builder(true);
        	
        	while(readInput.hasNext()){
                String[] capitalAndCountry = readInput.nextLine().split(" ");
                //System.out.println(Arrays.toString(capitalAndCountry));
                builder.add(new CharsRef(capitalAndCountry[0]), new CharsRef(capitalAndCountry[1]), true);
                builder.add(new CharsRef(capitalAndCountry[1]), new CharsRef(capitalAndCountry[0]), true);   
            }
            while(input2.hasNext()){
                String[] ukAndUsMapping = input2.nextLine().split(" ");
                builder.add(new CharsRef(ukAndUsMapping[0]), new CharsRef(ukAndUsMapping[1]), true);
                builder.add(new CharsRef(ukAndUsMapping[1]), new CharsRef(ukAndUsMapping[0]), true);
            }
        	synMap = builder.build();
            
        } catch (Exception e) {
                System.out.println("ERROR: " + e.getLocalizedMessage());
        }
        return synMap;
    }

    public static void main(String[] args) throws FileNotFoundException{
    	System.out.print(createSynonymMap());
    }
}