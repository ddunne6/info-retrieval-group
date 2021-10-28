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

// Create index using objects from DocumentParser.parse()
public class CreateIndex {
	
	String INDEX_DIRECTORY_CORPUS = "../index_corpus";
	ArrayList<Document> documents = new ArrayList<Document>();
	
	public void IndexFT(List<CustomDocument> rawDocuments) throws IOException
	{
		
		Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY_CORPUS));
		
		
		Map<String, Analyzer> analyzerMap = new HashMap<String, Analyzer>();
		
		analyzerMap.put("PROFILE", new KeywordAnalyzer());
		analyzerMap.put("PUB", new KeywordAnalyzer());
		analyzerMap.put("DATE", new KeywordAnalyzer());
		analyzerMap.put("PAGE", new KeywordAnalyzer());
		
		PerFieldAnalyzerWrapper aWrapper = new PerFieldAnalyzerWrapper(new EnglishAnalyzer(),analyzerMap);
		
		IndexWriterConfig config = new IndexWriterConfig(aWrapper);
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		IndexWriter iwriter = new IndexWriter(directory, config);
		
		ListIterator<CustomDocument> rawDocs = rawDocuments.listIterator();
		while(rawDocs.hasNext())
		{
			CustomDocument tempDoc = rawDocs.next();
			Document document = new Document();
			document.add(new StringField("Document Number", tempDoc.getDocno(), Field.Store.YES));
			ListIterator<CustomTag> rawTags = tempDoc.getOtherInfo().listIterator();
			
			while(rawTags.hasNext())
			{
				CustomTag tempOtherInfo = rawTags.next();
				document.add(new TextField(tempOtherInfo.getTag(), tempOtherInfo.getContent(), Field.Store.YES));
			}
			documents.add(document);
		}
		
		iwriter.addDocuments(documents);
		iwriter.close();
		directory.close();	
		
	}
	
}
