package tcd.parse_to_index;

import static tcd.constants.FilePathPatterns.INDEX_DIRECTORY_CORPUS;
import static tcd.constants.QueryConstants.DOCID;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class CreateIndexDavid {
	private Directory directory;
	private IndexWriterConfig config;
	private IndexWriter iwriter;
	
	public CreateIndexDavid() throws IOException {
		directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY_CORPUS));
		
		config = new IndexWriterConfig(new EnglishAnalyzer());
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        
        iwriter = new IndexWriter(directory, config);
	}
	
	public void indexCorpus(List<CustomDocument> documents) throws IOException {
		
		ArrayList<Document> indexedDocuments = new ArrayList<Document>();
		for (CustomDocument tempDoc : documents) {
			Document document = new Document();
			document.add(new StringField(DOCID, tempDoc.getDocno(), Field.Store.YES));
			for(CustomTag tempOtherInfo : tempDoc.getOtherInfo())
			{
				document.add(new TextField("content", tempOtherInfo.getContentAsString(), Field.Store.YES));
			}
			indexedDocuments.add(document);
			iwriter.addDocuments(indexedDocuments);
			indexedDocuments.clear();
		}
		System.out.println("INDEXED Documents");
	}
	
	public void closeIndex() {
		try {
			iwriter.close();
			directory.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
}
