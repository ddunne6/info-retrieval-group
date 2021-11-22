package tcd.analyzers;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.CapitalizationFilter;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import tcd.constants.Custom_StopWords;

// Adapted from https://www.baeldung.com/lucene-analyzers

//Usage:
// MyCustomAnalyzer snowballAnalyzer = new MyCustomAnalyzer("Snowball"); Creates an English analyzer with the "Snowball" stemming algorithm.
//We can easily add more string inputs if we want to add different token filters, but from my experimentation the EnglishAnalyzer config works best.

//Default (with no input) is Porter stemming
//"Snowball" uses the Snoball stemming algorithm
//"KStem" uses the K-Stem stemming algorithm

public class MyCustomAnalyzer extends Analyzer {
	
	private String stemmer;
	
	public MyCustomAnalyzer() {		
		super();
		this.stemmer = "Porter";			
	}
		
	public MyCustomAnalyzer(String stem_name) {		
		super();
		this.stemmer = stem_name;		
	}
    
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
        StandardTokenizer src = new StandardTokenizer();
        TokenStream result = new EnglishPossessiveFilter(src);
        result = new LowerCaseFilter(result);
        result = new StopFilter(result,  EnglishAnalyzer.ENGLISH_STOP_WORDS_SET);
             
        //result = new StopFilter(result,  EnglishAnalyzer.ENGLISH_STOP_WORDS_SET);
        
        //CharArraySet custom_stopwords = StopFilter.makeStopSet(Custom_StopWords.getStopWords());
        //result = new StopFilter(result, custom_stopwords);

        
        if(stemmer == "Porter") {
        	result = new PorterStemFilter(result);
        	System.out.println("Using Porter Stemming");
        	
        } else if(stemmer == "KStem") {
        	result = new KStemFilter(result);
        	System.out.println("Using KStem Stemming");
        	
        } else if (stemmer == "Snowball") {
        	
        	result = new SnowballFilter(result, "English");
        	System.out.println("Using Snowball Stemming");
        }

        //result = new ShingleFilter(result);
        //result = new CapitalizationFilter(result);
        return new TokenStreamComponents(src, result);
    }
}

