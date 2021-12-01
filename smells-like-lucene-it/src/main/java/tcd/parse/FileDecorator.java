package tcd.parse;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileDecorator {
	private Path path;
	private List<ReplaceTerm> replaceTerms = new ArrayList<ReplaceTerm>();
	
	public FileDecorator(String fileName) {
		this.path = Paths.get(fileName);
	}
	
	public void replaceAll(String replaceFrom, String replaceTo) {
		this.replaceTerms.add(new ReplaceTerm(replaceFrom, replaceTo));
	}
	
	public void decorate() {
		Charset charset = StandardCharsets.UTF_8;
		String content;
		try {
			content = new String(Files.readAllBytes(getPath()), charset);
			for(ReplaceTerm entry : replaceTerms){ 
			    content = content.replaceAll(entry.getReplaceFrom(), entry.getReplaceTo());
			}  
			Files.write(getPath(), content.getBytes(charset));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void replaceAcronyms() {
		replaceAll("\\sU\\.S\\.", " United States of America");
		replaceAll("\\sU\\.K\\.\\s", " United Kingdom ");
	}
	
	public Path getPath() {
		return path;
	}
}
