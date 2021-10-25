package tcd.constants;

import static tcd.constants.FilePathPatterns.FT_PATTERNS;
import static tcd.constants.FilePathPatterns.getFinancialTimesFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FilePathPatterns {
	public static final String PATH_TO_MEDIA = "../Assignment Two/Assignment Two/";

	public static final String FINANCIAL_TIMES = "ft/";
	public static final String FEDERAL_REGISTER = "fr94/";
	public static final String FOREIGN_BROADCAST = "fbis/";
	public static final String LOST_ANGELOS_TIMES = "latimes/";

	// Financial Times File Patterns
	public static final String FT_911_PATTERN = "ft911";
	public static final String FT_921_PATTERN = "ft921";
	public static final String FT_922_PATTERN = "ft922";
	public static final String FT_923_PATTERN = "ft923";
	public static final String FT_924_PATTERN = "ft924";
	public static final String FT_931_PATTERN = "ft931";
	public static final String FT_932_PATTERN = "ft932";
	public static final String FT_933_PATTERN = "ft933";
	public static final String FT_934_PATTERN = "ft934";
	public static final String FT_941_PATTERN = "ft941";
	public static final String FT_942_PATTERN = "ft942";
	public static final String FT_943_PATTERN = "ft943";
	public static final String FT_944_PATTERN = "ft944";

	public static final List<String> FT_PATTERNS = Arrays.asList(FT_911_PATTERN, FT_921_PATTERN, FT_922_PATTERN,
			FT_923_PATTERN, FT_924_PATTERN, FT_931_PATTERN, FT_932_PATTERN, FT_933_PATTERN, FT_934_PATTERN,
			FT_941_PATTERN, FT_942_PATTERN, FT_943_PATTERN, FT_944_PATTERN);

	public static String getFinancialTimesFile(String pattern, int fileNum) {
		String folderPath = PATH_TO_MEDIA + FINANCIAL_TIMES + pattern + "/";
		return folderPath + pattern + "_" + Integer.toString(fileNum);
	}
	
	public static List<String> getFinancialTimesFiles() {
		List<String> fileNames = new ArrayList<String>();
		for (String pattern : FT_PATTERNS) {
        	int index = 1;
        	while(Files.exists(Paths.get(getFinancialTimesFile(pattern, index)))) {
        		fileNames.add(getFinancialTimesFile(pattern, index));
        		index++;
        	}
        }
		return fileNames;
	}

	// TODO: Add remaining files
}
