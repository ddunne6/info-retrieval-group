package tcd.constants;

import org.junit.Ignore;
import org.junit.Test;

import static tcd.constants.CustomStopWords.getStopWords;;

public class CustomStopWordsTest {
	@Ignore
	@Test
	public void customStopWordsTest_at_75() {
		System.out.println("Begin Test >>> customStopWordsTest_at_80()");
		
		String[] stopWords = getStopWords(75f);
		// TODO implement assertion logic
		
		System.out.println("Test Complete >>> customStopWordsTest_at_80()");
	}
}
