package tcd.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.search.Query;

//Takes a multi-field query, and returns the list of terms. Used to push in to the CommonTermsLowFreqBoost query.

public class MultiQueryTermExtractor {
	
	public static List<String> extractTerms(Query query) {
		
		String temp_queryString = query.toString();		
		Pattern p = Pattern.compile("t:.*?\\)");
		Matcher matcher = p.matcher(temp_queryString);
		
		List<String> terms = new ArrayList<String>();
		
		while (matcher.find()) {
			
			terms.add(matcher.group().substring(2, matcher.group().length()-1));
			
		}
		
		return terms;
		
		
	}

}
