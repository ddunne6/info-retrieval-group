package tcd.parse_to_index;

import static tcd.constants.SGMLTags.*;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.org.apache.xerces.internal.xni.QName;

public class CustomHandlerSAX extends DefaultHandler {
	private StringBuilder currentValue = new StringBuilder();
	private CustomDocument currentDoc;
	private List<CustomDocument> documents = new ArrayList<CustomDocument>();

	@Override
	public void startDocument() {
		// Optional
	}

	@Override
	public void endDocument() {
		// Optional
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {

		resetCurrentValue();
		
		if(qName.equalsIgnoreCase(DOC)) {
			setCurrentDoc(new CustomDocument());
		}

	}

	private void resetCurrentValue() {
		currentValue.setLength(0);
	}

	@Override
	public void endElement(String uri, String localName, String qName) {
		
		if (qName.equalsIgnoreCase(DOC)) {
			addDocToDocuments(getCurrentDoc());
		}
		else {
			addTaggedContentToDoc(qName, currentValue.toString());
		}

	}

	private void addTaggedContentToDoc(String tagName, String content) {
		this.currentDoc.addTaggedContent(tagName, content);
	}

	@Override
	public void characters(char ch[], int start, int length) {
		currentValue.append(ch, start, length);

	}

	public CustomDocument getCurrentDoc() {
		return currentDoc;
	}

	public void setCurrentDoc(CustomDocument currentDoc) {
		this.currentDoc = currentDoc;
	}

	public List<CustomDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(List<CustomDocument> documents) {
		this.documents = documents;
	}
	
	public void addDocToDocuments(CustomDocument doc) {
		this.documents.add(doc);
	}
}
