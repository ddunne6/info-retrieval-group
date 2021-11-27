package tcd.parse;

import static tcd.constants.SGMLTags.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.org.apache.xerces.internal.xni.QName;

public class CustomHandlerSAX extends DefaultHandler {
	private StringBuilder currentValue = new StringBuilder();
	private CustomDocument currentDoc;
	private List<CustomDocument> documents = new ArrayList<CustomDocument>();
	private Stack<CustomTag> attributes = new Stack<CustomTag>();

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
		if(qName.equalsIgnoreCase(DOC)) {
			setCurrentDoc(new CustomDocument());
		}
		else {
			CustomTag element = new CustomTag(qName);
			this.attributes.push(element);
		}

	}

	private void resetCurrentValue() {
		currentValue.setLength(0);
	}

	@Override
	public void endElement(String uri, String localName, String qName) {
		
		String temp = currentValue.toString();
		
		if (qName.equalsIgnoreCase(DOC)) {
			addDocToDocuments(getCurrentDoc());
		}
		else {
			CustomTag element = attributes.pop();
			addTaggedContentToDoc(element);
		}

	}

	private void addTaggedContentToDoc(CustomTag attrib) {
		this.currentDoc.addTaggedContent(attrib);
	}

	@Override
	public void characters(char ch[], int start, int length) {
		CustomTag element = attributes.pop();
		element.appendContent(ch, start, length);
		this.attributes.push(element);
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
