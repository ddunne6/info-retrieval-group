package tcd.index;

import static tcd.constants.FilePathPatterns.INDEX_DIRECTORY_CORPUS;
import static tcd.constants.QueryConstants.CONTENT;
import static tcd.constants.QueryConstants.DOCID;
import static tcd.constants.QueryConstants.OTHER;
import static tcd.constants.QueryConstants.TITLE;

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
import tcd.parse.CustomDocument;
import tcd.parse.CustomTag;

public class CreateIndex {
	private Directory directory;
	private IndexWriterConfig config;
	private IndexWriter iwriter;
	private Similarity indexSimilarity = new BM25Similarity();

	public String MapTag(String tag) {
		switch (tag) {
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
			// Moved FBIS 'HEADER' to 'Other' since it was just repeated 'Document
			// Type:Daily Report'
		case "HEADER":
			return "Other";
		case "HEADLINE":
		case "TI":
		case "DOCTITLE":
			return TITLE;
		case "TEXT":
		case "SUMMARY":
		case "FURTHER":
		case "SUPPLEM":
		case "TABLE":
		case "ADDRESS":
			return CONTENT;
		default:
			return OTHER;

		}

	}

	public CreateIndex(String runName, Similarity runSimilarity) throws IOException {

		// this.runIndex = runIndex+runName;
		this.indexSimilarity = runSimilarity;

		directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY_CORPUS));

		config = new IndexWriterConfig(new MyCustomAnalyzer());
		// config = new IndexWriterConfig(new EnglishAnalyzer());
		config.setSimilarity(indexSimilarity);
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

		System.out.println("Index directory: " + directory.toString());
		System.out.println("Similarity: " + config.getSimilarity());

		iwriter = new IndexWriter(directory, config);
	}

	public CreateIndex(String runName, Similarity runSimilarity, float stopwordThreshold) throws IOException {

		// this.runIndex = runIndex+runName;
		this.indexSimilarity = runSimilarity;

		directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY_CORPUS));

		config = new IndexWriterConfig(new MyCustomAnalyzer(stopwordThreshold));
		// config = new IndexWriterConfig(new EnglishAnalyzer());
		config.setSimilarity(indexSimilarity);
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

		System.out.println("Index directory: " + directory.toString());
		System.out.println("Similarity: " + config.getSimilarity());

		iwriter = new IndexWriter(directory, config);
	}

	public void indexCorpus(List<CustomDocument> documents) throws IOException {

		ArrayList<Document> indexedDocuments = new ArrayList<Document>();
		String Mappedtag;
		for (CustomDocument tempDoc : documents) {
			Document document = new Document();
			document.add(new StringField(DOCID, tempDoc.getDocno(), Field.Store.YES));
			for (CustomTag tempOtherInfo : tempDoc.getOtherInfo()) {

				Mappedtag = MapTag(tempOtherInfo.getTag());
				if (Mappedtag.equals(TITLE)) {
					// System.out.println("Title tag is: "+tempOtherInfo.getTag());
					// System.out.println("Title is: "+tempOtherInfo.getContentAsString());
					document.add(new TextField(TITLE, tempOtherInfo.getContentAsString(), Field.Store.YES));
				} else {
					if (tempOtherInfo.getTag().equals("H3")) {

						// System.out.println(tempOtherInfo.getTag());
						// System.out.println("Content is: "+tempOtherInfo.getContentAsString());
					}
					// System.out.println("Tag is: "+tempOtherInfo.getTag());
					// System.out.println("Content is: "+tempOtherInfo.getContentAsString());
					document.add(new TextField(CONTENT, tempOtherInfo.getContentAsString(), Field.Store.YES));
				}
			}
			indexedDocuments.add(document);
			iwriter.addDocuments(indexedDocuments);
			indexedDocuments.clear();
		}
		// System.out.println("INDEXED Documents");
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
