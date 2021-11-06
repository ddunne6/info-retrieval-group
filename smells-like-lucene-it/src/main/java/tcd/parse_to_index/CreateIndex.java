package tcd.parse_to_index;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import tcd.analyzers.MyCustomAnalyzer;

// Create index using objects from DocumentParser.parse()
public class CreateIndex {
	
	String INDEX_DIRECTORY_CORPUS = "../index_corpus";
	ArrayList<Document> documents = new ArrayList<Document>();
	
	IndexWriterConfig config;
	IndexWriter iwriter;
	Directory directory;
	PerFieldAnalyzerWrapper aWrapper;
	Map<String, Analyzer> analyzerMap;
	
	public CreateIndex() throws IOException
	{
	
		directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY_CORPUS));
		
		analyzerMap = new HashMap<String, Analyzer>();
		
		//For Financial times Corpus
		analyzerMap.put("PROFILE", new KeywordAnalyzer());
		analyzerMap.put("PUB", new KeywordAnalyzer());
		analyzerMap.put("DATE", new KeywordAnalyzer());
		analyzerMap.put("PAGE", new KeywordAnalyzer());
		
		//For Financial Register Corpus
		analyzerMap.put("USDEPT", new KeywordAnalyzer());
		analyzerMap.put("USBUREAU", new KeywordAnalyzer());
		analyzerMap.put("CFRNO", new KeywordAnalyzer());
		analyzerMap.put("RINDOCK", new KeywordAnalyzer());
		analyzerMap.put("AGENCY", new KeywordAnalyzer());
		analyzerMap.put("ACTION", new KeywordAnalyzer());
	
		
		aWrapper = new PerFieldAnalyzerWrapper(new MyCustomAnalyzer(),analyzerMap);
		
		config = new IndexWriterConfig(aWrapper);
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
		iwriter = new IndexWriter(directory, config);
	}
	
	public void IndexFT(List<CustomDocument> rawDocuments) throws IOException
	{

		for (CustomDocument tempDoc : rawDocuments)
		{
			Document document = new Document();
			document.add(new StringField("Document Number", tempDoc.getDocno(), Field.Store.YES));
			for(CustomTag tempOtherInfo : tempDoc.getOtherInfo())
			{
				document.add(new TextField(tempOtherInfo.getTag(), tempOtherInfo.getContentAsString(), Field.Store.YES));
			}
			documents.add(document);
			iwriter.addDocuments(documents);
			documents.clear();
		}	
		
	}
	
	public void IndexFR(List<CustomDocument> rawDocuments) throws IOException
	{

		for (CustomDocument tempDoc : rawDocuments)
		{
			Document document = new Document();
			document.add(new StringField("Document Number", tempDoc.getDocno(), Field.Store.YES));
			for(CustomTag tempOtherInfo : tempDoc.getOtherInfo())
			{
				document.add(new TextField(tempOtherInfo.getTag(), tempOtherInfo.getContentAsString(), Field.Store.YES));
			}
			documents.add(document);
			iwriter.addDocuments(documents);
			documents.clear();
		}	
		
	}
	
}
