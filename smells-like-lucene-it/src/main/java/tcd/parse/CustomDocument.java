package tcd.parse;

import static tcd.constants.SGMLTags.*;

import java.util.ArrayList;
import java.util.List;

public class CustomDocument {
	private String docno;
	private List<CustomTag> otherInfo = new ArrayList<CustomTag>(); // All other tags and content apart from <DOCNO>
	
	public String getDocno() {
		return docno;
	}
	public void setDocno(String docno) {
		this.docno = docno;
	}
	
	public List<CustomTag> getOtherInfo() {
		return otherInfo;
	}
	
	public void addTaggedContent(CustomTag attrib) {
		switch (attrib.getTag()) {
			case DOCNO:		setDocno(attrib.getContentAsString());
							break;
			case XML_DUMMY: // Do nothing
							break;
			default:		addOtherInfo(attrib);
							break;
		}
	}
	
	private void addOtherInfo(CustomTag attrib) {
		otherInfo.add(attrib);
	}
}
