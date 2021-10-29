package tcd.constants;

import java.util.Arrays;
import java.util.List;

public final class SGMLTags {
	public static final String DOC = "DOC";
	public static final String DOCNO = "DOCNO";
	public static final String PROFILE = "PROFILE";
	public static final String DATE = "DATE";
	public static final String HEADLINE = "HEADLINE";
	public static final String TEXT = "TEXT";
	public static final String PUB = "PUB";
	public static final String PAGE = "PAGE";
	
	public static final String XML_DUMMY = "thistagisunique";

	public static final List<String> FT_TAGS = Arrays.asList(DOC, DOCNO, PROFILE, DATE, HEADLINE, TEXT, PUB, PAGE);

	public static String openingTag(String term) {
		return String.format("<%s>", term);
	}

	public static String closingTag(String term) {
		return String.format("</%s>", term);
	}
}
