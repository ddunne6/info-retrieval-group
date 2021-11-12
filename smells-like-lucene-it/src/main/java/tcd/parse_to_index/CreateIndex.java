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
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
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
	
    FieldType ft = new FieldType(TextField.TYPE_STORED);

    public String MapTag(String tag)
    {
    	switch(tag)
    	{
    		case "PROFILE":
    		case "PUB":
    		case "DATE":
    		case "PAGE":
    		case "USDEPT":
    		case "USBUREAU":
    		case "CFRNO":
    		case "RINDOCK":
    		case "AGENCY":
    		case "ACTION":
    		case "DATE1":
    		case "HT":
    		case "H3":
    		case "H4":
    		case "H5":
    		case "H6":
    		case "AU":
    		case "F":
    		case "PARENT":
    		case "SIGNER":
    		case "SIGNJOB":
    		case "FRFILING":
    		case "BILLING":
    		case "FOOTCITE":
    		case "FOOTNAME":
    		case "FOOTNOTE":
    		case "IMPORT":
    		case "BYLINE":
    		case "DATELINE":
    			return "Other";
    		case "HEADLINE":
    		case "TI":
    		case "HEADER":
    		case "DOCTITLE":
    			return "Title";
    		case "TEXT":
    		case "SUMMARY":
    		case "FURTHER":
    		case "SUPPLEM":
    		case "TABLE":
    		case "ADDRESS":
    			return "Content";
    		default:
    				return "Other";
    			
    	}
    	
    }
	
	public CreateIndex() throws IOException
	{
	
		directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY_CORPUS));
		
		analyzerMap = new HashMap<String, Analyzer>();
		
		/* Leaving it here in case required later
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
		*/
 
		analyzerMap.put("Other", new WhitespaceAnalyzer());
		analyzerMap.put("Document Number", new KeywordAnalyzer());
		
		aWrapper = new PerFieldAnalyzerWrapper(new MyCustomAnalyzer(),analyzerMap);
		
	    ft.setTokenized(true);
	    ft.setStoreTermVectors(true);
	    ft.setStoreTermVectorPositions(true);
	    ft.setStoreTermVectorOffsets(true);
	    ft.setStoreTermVectorPayloads(true);
		
		config = new IndexWriterConfig(aWrapper);
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
		iwriter = new IndexWriter(directory, config);
	}
	
	public void Indexcorpus(List<CustomDocument> rawDocuments) throws IOException
	{
		String Mappedtag;
		for (CustomDocument tempDoc : rawDocuments)
		{
			Document document = new Document();
			document.add(new StringField("Document Number", tempDoc.getDocno(), Field.Store.YES));
			for(CustomTag tempOtherInfo : tempDoc.getOtherInfo())
			{
				Mappedtag = MapTag(tempOtherInfo.getTag());
				if (!Mappedtag.contains("Tag not found"))
				{
					document.add(new Field(Mappedtag, tempOtherInfo.getContentAsString(), ft));
				}
				else
				{
					System.out.println("Unhandled tag: " + tempOtherInfo.getTag() );
				}
			}
			documents.add(document);
			iwriter.addDocuments(documents);
			documents.clear();
		}		
	}

}
