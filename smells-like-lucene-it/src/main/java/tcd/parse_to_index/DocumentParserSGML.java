package tcd.parse_to_index;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.xml.sax.SAXException;

import static tcd.constants.SGMLTags.*;
import static tcd.constants.FilePathPatterns.*;

public class DocumentParserSGML {
	// Parse file and return list of documents
	public List<CustomDocument> parse(String filePath) {
		List<CustomDocument> documents = new ArrayList<CustomDocument>();

		SAXParserFactory factory = SAXParserFactory.newInstance();

		try {

			SAXParser saxParser = factory.newSAXParser();

			CustomHandlerSAX handler = new CustomHandlerSAX();
			saxParser.parse(filePath, handler);
			documents = handler.getDocuments();

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		Path path = Paths.get(filePath);
		try {
			Files.deleteIfExists(path);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return documents;
	}
	
	public List<CustomDocument> parseFTLA(String filePath) {
		System.out.println("STARTED Parsing file >>> " + filePath);
		String fileAsXML = decorateFileToXML(filePath);
		System.out.println("FINISHED Parsing file >>> " + filePath);
		return parse(fileAsXML);
	}
	
	public List<CustomDocument> parseFBIS(String filePath) {
		System.out.println("STARTED Parsing file >>> " + filePath);
		String fileAsXML = decorateFileToXML(filePath);
		FileDecorator fileDecorator = new FileDecorator(fileAsXML);
		fileDecorator.replaceAll("&\\w+", "");
		fileDecorator.replaceAll("&", "");
		fileDecorator.replaceAll("P=\\w+", "");
		fileDecorator.replaceAll(" ID=\\w+-\\w+-\\w+\\w-\\w+", "");
		fileDecorator.replaceAll("<\\d>", "<digit>");
		fileDecorator.replaceAll("</\\d>", "</digit>");
		fileDecorator.decorate();
		System.out.println("FINISHED Parsing file >>> " + filePath);
		return parse(fileAsXML);
	}

	public List<CustomDocument> parseFR(String filePath) {
		System.out.println("STARTED Parsing file >>> " + filePath);
		String fileAsXML = decorateFileToXML(filePath);
		FileDecorator fileDecorator = new FileDecorator(fileAsXML);
		fileDecorator.replaceAll("&\\w+", "");
		fileDecorator.replaceAll("&", "");
		fileDecorator.decorate();
		System.out.println("FINISHED Parsing file >>> " + filePath);
		return parse(fileAsXML);
	}
	

	private String decorateFileToXML(String filePath) {
		String[] fileSplit = filePath.split("/");
		String tempFile = TEMP_FOLDER + fileSplit[fileSplit.length - 1] + ".xml"; // TODO return saved copy instead of redoing work
		Path path = Paths.get(tempFile);
		try {
			Files.deleteIfExists(path);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Copy this file to /temp-xml-conversion
		File copied = new File(tempFile);
		try {
			FileUtils.copyFile(new File(filePath), copied);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Edit new doc and add <root> to top and bottom
		try {
			addPrefixSuffix(copied, openingTag(XML_DUMMY), closingTag(XML_DUMMY));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Return new temp doc name
		return tempFile;
	}

	@SuppressWarnings("deprecation")
	public static void addPrefixSuffix(File input, String prefix, String suffix) throws IOException {
		LineIterator li = FileUtils.lineIterator(input);
		File tempFile = File.createTempFile("prependPrefix", ".tmp");
		BufferedWriter w = new BufferedWriter(new FileWriter(tempFile));
		try {
			w.write(prefix);
			while (li.hasNext()) {
				w.write(li.next());
				w.write("\n");
			}
			w.append(suffix);
		} finally {
			IOUtils.closeQuietly(w);
			LineIterator.closeQuietly(li);
		}
		FileUtils.deleteQuietly(input);
		FileUtils.moveFile(tempFile, input);
	}
}
