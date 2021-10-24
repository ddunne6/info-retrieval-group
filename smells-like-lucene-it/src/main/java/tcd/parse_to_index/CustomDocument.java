package tcd.parse_to_index;

import static tcd.constants.SGMLTags.*;

public class CustomDocument {
	private String docno;
	private String profile;
	private String date;
	private String headline;
	private String text;
	private String pub;
	private String page;
	
	public String getDocno() {
		return docno;
	}
	public void setDocno(String docno) {
		this.docno = docno;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getPub() {
		return pub;
	}
	public void setPub(String pub) {
		this.pub = pub;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public void addTaggedContent(String tag, String tagContent) {
		switch (tag) {
			case DOCNO:		setDocno(tagContent);
							break;
			case PROFILE:	setProfile(tagContent);
							break;
			case DATE:		setDate(tagContent);
							break;
			case HEADLINE:	setHeadline(tagContent);
							break;
			case TEXT:		setText(tagContent);
							break;
			case PUB:		setPub(tagContent);
							break;
			case PAGE:		setPage(tagContent);
							break;
		}
	}
}
