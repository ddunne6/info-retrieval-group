package tcd.parse_to_index;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static tcd.constants.SGMLTags.*;

public class DocumentParser {
	// Parse file and return list of documents
	public List<CustomDocument> parse(String filePath) {
		System.out.println("STARTED Parsing file >>> " + filePath);

		List<CustomDocument> documents = new ArrayList<CustomDocument>();

		try {
			// Open the file
			FileInputStream fstream = new FileInputStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine;

			String tagContent = "";
			String currentTag = "DOC";

			CustomDocument doc = new CustomDocument();

			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				if (isNewDocument(strLine)) {
					doc = new CustomDocument();
				} else if (isEndOfDocument(strLine)) {
					documents.add(doc);
				} else if (isOpeningTag(strLine)) {
					currentTag = extractTag(strLine);
					if (isAlsoContent(strLine, currentTag) && hasClosingTags(strLine, currentTag)) {
						tagContent = addLineToBuffer(tagContent, removeTags(strLine, currentTag));
						doc.addTaggedContent(currentTag, tagContent);
						tagContent = "";
						currentTag = "";
					} else if (isAlsoContent(strLine, currentTag)) {
						tagContent = addLineToBuffer(tagContent, removeOpeningTag(strLine, currentTag));
					}
				} else if (isClosingTag(strLine)) {
					// Check opening tag matches closing tag
					closingAndOpeningTagsMatch(currentTag, extractTag(strLine));
					if (!isOnlyClosingTag(strLine, currentTag)) {
						tagContent = addLineToBuffer(tagContent, removeClosingTags(strLine, currentTag));
					}
					doc.addTaggedContent(currentTag, tagContent);
					tagContent = "";
					currentTag = "";
				} else {
					tagContent = addLineToBuffer(tagContent, strLine);
				}
			}

			// Close the input stream
			fstream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("FINISHED Parsing file >>> " + filePath);
		return documents;
	}

	private boolean isOnlyClosingTag(String strLine, String tag) {
		return closingTag(tag).equals(strLine);
	}

	private void closingAndOpeningTagsMatch(String closingTag, String openingTag) throws Exception {
		if (!closingTag.equals(openingTag)) {
			throw new Exception(
					String.format("Opening Tag '%s' and Closing Tag '%s' don't match", openingTag, closingTag));
		}

	}

	private String removeClosingTags(String strLine, String currentTag) {
		return strLine.replace(closingTag(currentTag), "");
	}

	private String removeOpeningTag(String strLine, String currentTag) {
		return strLine.replace(openingTag(currentTag), "");
	}

	private String removeTags(String strLine, String currentTag) {
		String cleanedLine = strLine.replace(openingTag(currentTag), "");
		cleanedLine = cleanedLine.replace(closingTag(currentTag), "");
		return cleanedLine;
	}

	private boolean hasClosingTags(String strLine, String currentTag) {
		return strLine.contains(closingTag(currentTag));
	}

	private boolean isAlsoContent(String strLine, String currentTag) {
		return strLine.length() > openingTag(currentTag).length();
	}

	private String addLineToBuffer(String tagContent, String strLine) {
		if ("".equals(tagContent)) {
			tagContent = strLine;
		}
		else {
			tagContent = tagContent + "\n" + strLine;
		}
		return tagContent;
	}

	private String extractTag(String strLine) throws Exception {
		for (String tag : FT_TAGS) {
			if (strLine.contains(closingTag(tag)) || strLine.contains(openingTag(tag))) {
				return tag;
			}
		}
		throw new Exception(String.format("Registered tag not found in line %s", strLine));
	}

	private boolean isClosingTag(String strLine) {
		for (String tag : FT_TAGS) {
			if (strLine.endsWith(closingTag(tag))) {
				return true;
			}
		}
		return false;
	}

	private boolean isOpeningTag(String strLine) {
		for (String tag : FT_TAGS) {
			if(strLine.startsWith(openingTag(tag))) {
				return true;
			}
		}
		return false;
	}

	private boolean isEndOfDocument(String strLine) {
		return closingTag(DOC).equals(strLine);
	}

	private boolean isNewDocument(String strLine) {
		return openingTag(DOC).equals(strLine);
	}
}
