package tcd.parse;

public class CustomTag {
	private String tag;
	private String contentAsString;
	private StringBuilder content = new StringBuilder();
	
	public CustomTag(String initTag) {
		setTag(initTag);
	}
	public CustomTag(String initTag, String content) {
		setTag(initTag);
		setContent(content);
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getContentAsString() {
		return content.toString();
	}
	public void appendContent(char ch[], int start, int length) {
		this.content.append(ch, start, length);
		this.contentAsString = getContentAsString();
	}
	public void setContent(String s) {
		this.content = new StringBuilder(s);
		this.contentAsString = getContentAsString();
	}
}
