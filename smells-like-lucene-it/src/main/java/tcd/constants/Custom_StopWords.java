
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
public class Custom_StopWords {
	private   final static Path currentRelativePath = Paths.get("").toAbsolutePath();
	static List<String>  stopWords_list =new ArrayList<String>(); 
	static String[] exception;
	// throws EncryptedDocumentException, IOException
public  static String[] getStopWords(float threshold) {
	try {
		
		//Currently made of words that appear in more than 70% of the documents
		
		//Workbook wb = WorkbookFactory.create(new File("C:\\Users\\Saubhagya\\Desktop\\sample-code\\STOPWORD_LIST.xlsx"));
		 Workbook wb = WorkbookFactory.create(new File(currentRelativePath+"/STOPWORD_LIST.xlsx"));
		 DataFormatter df = new DataFormatter();
		 String[] list_stopWords = null;
	   	 Sheet s = wb.getSheetAt(0);
	   	 int rowCount= s.getLastRowNum();
	
		 for (int i = 1; i < rowCount; i++) {
			 Row r1 = s.getRow(i);
			 Cell cA1 = r1.getCell(3);
			 
			
			 String asItLooksInExcel = df.formatCellValue(cA1);
			 float fre = Float.parseFloat(asItLooksInExcel);
			
			 if(fre>=threshold) {
			
				 Cell ca2=r1.getCell(0);
				 String asItLooksInExcel2 = df.formatCellValue(ca2);
				 stopWords_list.add(asItLooksInExcel2);
				 
			 }
			list_stopWords = stopWords_list.toArray(new String[0]);
			
			}
		
		return list_stopWords;
	}catch (Exception e) {
		return exception;
	}
		
	}

//public static void main(String []args) 

//{
//	try {
//	System.out.println(Arrays.asList(getStopWords(90)));
//	}catch (Exception e) {
//		System.out.println(Arrays.asList(getStopWords(90)));
//	}
	
//}





	
}

