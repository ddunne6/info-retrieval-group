package tcd.mappings;

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
    // UK/US synonyms from http://www.tysto.com/uk-us-spelling-list.html
    // Capitals: https://www.countries-ofthe-world.com/capitals-of-the-world.html
    private static final String CITIES_FILE = "../cities.txt";
    private static final String UK_US_FILE = "../uk_us.txt";
    public SynonymMap createSynonymMap() throws FileNotFoundException {
        SynonymMap synMap = new SynonymMap(null, null, 0);
        Scanner readInput = new Scanner(new File(CITIES_FILE));
        Scanner input2 = new Scanner(new File(UK_US_FILE));

        try {
        	final SynonymMap.Builder builder = new SynonymMap.Builder(true);
        	
        	while(readInput.hasNext()){
                String[] capitalAndCountry = readInput.nextLine().split(" ");
                //System.out.println(Arrays.toString(capitalAndCountry));
                String output = "";
                if(capitalAndCountry.length == 3) {
                	output = capitalAndCountry[1] + " " + capitalAndCountry[2];
                	//System.out.println(output);
                }
                else {
                	output = capitalAndCountry[1];
                }
                builder.add(new CharsRef(capitalAndCountry[0]), new CharsRef(output), true);
            }
            while(input2.hasNext()){
                String[] ukAndUsMapping = input2.nextLine().split(" ");
                builder.add(new CharsRef(ukAndUsMapping[0]), new CharsRef(ukAndUsMapping[1]), true);
            }
        	synMap = builder.build();
            
        } catch (Exception e) {
                e.printStackTrace();
        }
        return synMap;
    }
}