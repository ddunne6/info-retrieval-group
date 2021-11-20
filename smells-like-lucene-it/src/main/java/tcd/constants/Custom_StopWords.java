package tcd.constants;

public class Custom_StopWords {

	public static String[] getStopWords() {
		
		//Currently made of words that appear in more than 70% of the documents
		String[] stopwords_list = new String[] {
				"the", 
				"of ",
				"to", 
				"in",
				"and", 
				"a",
				"for",
				"on",
				"s",
				"by",
				"that", 
				"is", 
				"with" 
		};
		
		return stopwords_list;
		
	}
}
