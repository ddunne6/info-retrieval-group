package tcd.mappings;

import org.apache.lucene.analysis.synonym.SolrSynonymParser;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.util.CharsRef;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class MySynonymMap{
    // UK/US synonyms from http://www.tysto.com/uk-us-spelling-list.html
    private static final String CITIES_FILE = "../worldcities.csv";
    private static final String UK_US_FILE = "../uk_us.txt";
    public SynonymMap createSynonymMap() throws IOException {
        SynonymMap synMap = new SynonymMap(null, null, 0);
        Scanner ukUsInput = new Scanner(new File(UK_US_FILE));
        BufferedReader citiesReader = new BufferedReader(new FileReader(CITIES_FILE));

        try {
        	final SynonymMap.Builder builder = new SynonymMap.Builder(true);
        	String row;
        	while((row = citiesReader.readLine()) != null){
                String[] rowData = row.split(",");
                //System.out.println("City: " + rowData[0] + " Country: " + rowData[4]);
                
                builder.add(new CharsRef(rowData[1].toLowerCase()), new CharsRef(rowData[4].toLowerCase()), true);
            }
            while(ukUsInput.hasNext()){
                String[] ukAndUsMapping = ukUsInput.nextLine().split(" ");
                builder.add(new CharsRef(ukAndUsMapping[0]), new CharsRef(ukAndUsMapping[1]), true);
            }
        	synMap = builder.build();
            
        } catch (Exception e) {
                e.printStackTrace();
        }
        citiesReader.close();
        ukUsInput.close();
        return synMap;
    }
}