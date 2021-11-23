package tcd.constants;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FilePathPatterns {
	public static final String PATH_TO_MEDIA = "../Assignment Two/Assignment Two/";
	public static final String TEMP_FOLDER = "temp-sgml-to-xml/";
	public static final String INDEX_DIRECTORY_CORPUS = "../index/index_corpus";

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

	// Federal Register File Patterns
	public static final String FR_01_PATTERN = "01";
	public static final String FR_02_PATTERN = "02";
	public static final String FR_03_PATTERN = "03";
	public static final String FR_04_PATTERN = "04";
	public static final String FR_05_PATTERN = "05";
	public static final String FR_06_PATTERN = "06";
	public static final String FR_07_PATTERN = "07";
	public static final String FR_08_PATTERN = "08";
	public static final String FR_09_PATTERN = "09";
	public static final String FR_10_PATTERN = "10";
	public static final String FR_11_PATTERN = "11";
	public static final String FR_12_PATTERN = "12";

	public static final List<String> FR_DIRS = Arrays.asList(FR_01_PATTERN, FR_02_PATTERN, FR_03_PATTERN, FR_04_PATTERN,
			FR_05_PATTERN, FR_06_PATTERN, FR_07_PATTERN, FR_08_PATTERN, FR_09_PATTERN, FR_10_PATTERN, FR_11_PATTERN,
			FR_12_PATTERN);

	public static String getFinancialTimesFile(String pattern, int fileNum) {
		String folderPath = PATH_TO_MEDIA + FINANCIAL_TIMES + pattern + "/";
		return folderPath + pattern + "_" + Integer.toString(fileNum);
	}

	public static List<String> getFinancialTimesFiles() {
		List<String> fileNames = new ArrayList<String>();
		for (String pattern : FT_PATTERNS) {
			int index = 1;
			while (Files.exists(Paths.get(getFinancialTimesFile(pattern, index)))) {
				fileNames.add(getFinancialTimesFile(pattern, index));
				index++;
			}
		}
		return fileNames;
	}

	public static List<String> getFederalRegisterFilesInDir(String dir) {
		String folderPath = PATH_TO_MEDIA + FEDERAL_REGISTER + dir;
		File directoryPath = new File(folderPath);
		List<String> directoryFiles = Arrays.asList(directoryPath.list());
		for (int i=0; i<directoryFiles.size() ; i++) {
			directoryFiles.set(i, folderPath + "/" + directoryFiles.get(i));
		}
		return directoryFiles;
	}
	
	public static List<String> getAllFederalRegisterFiles() {
		List<String> fileNames = new ArrayList<String>();
		for (String pattern : FR_DIRS) {
			fileNames.addAll(getFederalRegisterFilesInDir(pattern));
		}
		return fileNames;
	}
	
	public static List<String> getAllForeignBroadcastISFiles() {
		String folderPath = PATH_TO_MEDIA + FOREIGN_BROADCAST;
		File directoryPath = new File(folderPath);
		List<String> directoryFiles = new ArrayList<String>(Arrays.asList(directoryPath.list()));
		for (int i=0; i<directoryFiles.size() ; i++) {
			directoryFiles.set(i, folderPath + directoryFiles.get(i));
		}
		// Remove readme files
		directoryFiles.remove(directoryFiles.size() - 1);
		directoryFiles.remove(directoryFiles.size() - 1);
		return directoryFiles;
	}
	
	public static List<String> getAllLATimesFiles() {
		String folderPath = PATH_TO_MEDIA + LOST_ANGELOS_TIMES;
		File directoryPath = new File(folderPath);
		List<String> directoryFiles = new ArrayList<String>(Arrays.asList(directoryPath.list()));
		for (int i=0; i<directoryFiles.size() ; i++) {
			directoryFiles.set(i, folderPath + directoryFiles.get(i));
		}
		// Remove readme files
		directoryFiles.remove(directoryFiles.size() - 1);
		directoryFiles.remove(directoryFiles.size() - 1);
		return directoryFiles;
	}

}
