package tcd.constants;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.text.ParseException;

import org.apache.lucene.analysis.hunspell.Dictionary;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public final class Dictionaries {
	private final static String EN_US_AFF = "../dictionaries/en_US.aff";
	private final static String EN_US_DIC = "../dictionaries/en_US.dic";
	private final static String DICTIONARY_DIRECTORY = "temp-spellcheck/";
	
	public static Dictionary getUSDictionary() {
		try {
			Directory directory = FSDirectory.open(Paths.get(DICTIONARY_DIRECTORY));
			InputStream inputStream_en_US_aff = new FileInputStream(EN_US_AFF);
			InputStream inputStream_en_US_dic = new FileInputStream(EN_US_DIC);
			return new Dictionary(directory, "temp", inputStream_en_US_aff, inputStream_en_US_dic);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}
}
