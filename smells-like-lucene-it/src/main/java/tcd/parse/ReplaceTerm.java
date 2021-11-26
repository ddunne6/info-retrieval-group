package tcd.parse;

public class ReplaceTerm {
	private String replaceFrom;
	private String replaceTo;
	
	public ReplaceTerm(String from, String to) {
		setReplaceFrom(from);
		setReplaceTo(to);
	}
	public String getReplaceFrom() {
		return replaceFrom;
	}
	public void setReplaceFrom(String replaceFrom) {
		this.replaceFrom = replaceFrom;
	}
	public String getReplaceTo() {
		return replaceTo;
	}
	public void setReplaceTo(String replaceTo) {
		this.replaceTo = replaceTo;
	}
}
