package tcd.parse_to_index;

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
	
	public void addTaggedContent(String tag, String tagContent) {
		switch (tag) {
			case DOCNO:		setDocno(tagContent);
							break;
			case XML_DUMMY: // Do nothing
							break;
			default:		addOtherInfo(tag, tagContent);
							break;
		}
	}
	
	private void addOtherInfo(String tag, String tagContent) {
		otherInfo.add(new CustomTag(tag, tagContent));
	}
}
