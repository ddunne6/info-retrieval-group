package tcd.parse_to_index;

public class CustomTag {
	private String tag;
	private String content;
	
	public CustomTag(String initTag, String initContent) {
		setTag(initTag);
		setContent(initContent);
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
