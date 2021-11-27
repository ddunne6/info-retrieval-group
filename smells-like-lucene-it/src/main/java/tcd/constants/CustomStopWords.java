package tcd.constants;

import java.io.File;

//reading value of a particular cell  
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.EncryptedDocumentException;
//Workbook wb = WorkbookFactory.create(new File(currentRelativePath+"/STOPWORD_LIST.xlsx"));

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CustomStopWords {
	private final static Path currentRelativePath = Paths.get("").toAbsolutePath();
	private final static String STOPWORD_FILENAME = "/STOPWORD_LIST.xlsx";

	static List<String> stopWordsAsList = new ArrayList<String>();
	static String[] exception;

	public static String[] getStopWords(float threshold) {
		try {
			Workbook wb = WorkbookFactory.create(new File(currentRelativePath + STOPWORD_FILENAME));
			DataFormatter df = new DataFormatter();
			String[] stopWordsAsArray = null;
			Sheet s = wb.getSheetAt(0);
			int rowCount = s.getLastRowNum();

			for (int i = 1; i < rowCount; i++) {
				Row r1 = s.getRow(i);
				Cell cA1 = r1.getCell(3);

				String asItLooksInExcel = df.formatCellValue(cA1);
				float fre = Float.parseFloat(asItLooksInExcel);

				if (fre >= threshold) {

					Cell ca2 = r1.getCell(0);
					String asItLooksInExcel2 = df.formatCellValue(ca2);
					stopWordsAsList.add(asItLooksInExcel2);

				}
				stopWordsAsArray = stopWordsAsList.toArray(new String[0]);

			}

			return stopWordsAsArray;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
