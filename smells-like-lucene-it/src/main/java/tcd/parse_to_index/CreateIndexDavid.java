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
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import tcd.analyzers.MyCustomAnalyzer;

public class CreateIndexDavid {
	private Directory directory;
	private IndexWriterConfig config;
	private IndexWriter iwriter;
	private String runIndex = INDEX_DIRECTORY_CORPUS;
	private Similarity indexSimilarity = new BM25Similarity();
	
	
	public CreateIndexDavid(String runName, Similarity runSimilarity) throws IOException {
		
		this.runIndex = runIndex+runName;
		this.indexSimilarity = runSimilarity;

		directory = FSDirectory.open(Paths.get(runIndex));
		
		config = new IndexWriterConfig(new MyCustomAnalyzer());
		//config = new IndexWriterConfig(new EnglishAnalyzer());
		config.setSimilarity(indexSimilarity);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        
        System.out.println("Index directory: "+directory.toString());
        System.out.println("Similarity: "+config.getSimilarity());
        
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
		//System.out.println("INDEXED Documents");
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
