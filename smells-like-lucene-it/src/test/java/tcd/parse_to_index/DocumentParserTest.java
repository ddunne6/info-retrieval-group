package tcd.parse_to_index;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static tcd.constants.FilePathPatterns.*;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import tcd.parse.CustomDocument;
import tcd.parse.CustomTag;
import tcd.parse.DocumentParserSGML;

public class DocumentParserTest {
	DocumentParserSGML documentParser;
	List<CustomDocument> ft911docs, ft934docs, fr940104docs;
	CustomDocument expectedDoc;

	@Before
	public void setUp() {
		documentParser = new DocumentParserSGML();
		ft911docs = documentParser.parseFT(getFinancialTimesFile(FT_911_PATTERN, 1));
		ft934docs = documentParser.parseFT(getFinancialTimesFile(FT_934_PATTERN, 7));
		fr940104docs = documentParser.parseFR(getAllFederalRegisterFiles().get(0));
		expectedDoc = new CustomDocument();
	}

	@Ignore
	@Test
	public void financialTimes_ft943_7_FirstDocTest() {
		System.out.println("Begin Test >>> financialTimes_ft943_7_FirstDocTest()");
		String docno = "FT934-2159";
		String profile = "_AN-DLOCPAEQFT";
		String date = "931215\n";
		String headline = "\nFT  15 DEC 93 / UK Company News: Intrum Justitia sees static result\n";
		String byline = "\n   By DAVID BLACKWELL\n";
		String text = "\nShares in Intrum Justitia, Europe's leading debt collector, yesterday fell\n"
				+ "28p to close at 100p after warning that the declining value of Nordic\n"
				+ "currencies and a delay in recovery in Switzerland would hit profits for this\n" + "year.\n"
				+ "In September the group, based in the Netherlands and listed in London,\n"
				+ "reported pre-tax profits up from Pounds 6.81m to Pounds 7.47m for the six\n" + "months to June 30.\n"
				+ "However, the group said annual profits after tax would be 'approximately the\n"
				+ "same as for 1992, which is below current market expectations'.\n"
				+ "Last year pre-tax profits were ahead 26 per cent at Pounds 16m. The final\n"
				+ "dividend of 2p made a total for the year of 3p. The group still expects to\n"
				+ "recommend an increase in the final dividend this year.\n";
		String xx1 = "\nCompanies:-\n";
		String co = "Intrum Justitia.\n";
		String xx2 = "\nCountries:-\n";
		String cn = "NLZ  Netherlands, EC.\n";
		String xx3 = "\nIndustries:-\n";
		String in = "P7322 Adjustment and Collection Services.\n";
		String xx4 = "\nTypes:-\n";
		String tp = "FIN  Interim results.\n";
		String pub = "The Financial Times\n";
		String page = "\nLondon Page 20\n";

		initExpectedDocft934(docno, profile, date, headline, byline, text, xx1, co, xx2, cn, xx3, in, xx4, tp, pub,
				page);
		assertDocsEqual(ft934docs.get(0), expectedDoc);

		System.out.println("Test Complete >>> financialTimes_ft943_7_FirstDocTest()");
	}
	@Ignore
	@Test
	public void financialTimes_ft911_1_FirstDocTest() {
		System.out.println("Begin Test >>> financialTimes_ft911_1_FirstDocTest()");
		String docno = "FT911-1";
		String profile = "_AN-BENBQAD8FT";
		String date = "910514\n";
		String headline = "\nFT  14 MAY 91 / (CORRECTED) Jubilee of a jet that did what it was designed\n" + "to do\n";
		String text = "\nCorrection (published 16th May 1991) appended to this article.\n"
				+ "'FRANK, it flies]' shouted someone at Sir Frank Whittle during the maiden\n"
				+ "flight of a British jet. 'Of course it does,' replied Sir Frank, who\n"
				+ "patented the first aircraft gas turbine. 'That's what it was bloody well\n"
				+ "designed to do, wasn't it?'\n"
				+ "Exactly 50 years ago yesterday, the first British jet made a brief 17-minute\n"
				+ "flight from RAF Cranwell in Lincolnshire. To celebrate the event, Mr Eric\n"
				+ "'Winkle' Brown, a 72-year-old test pilot of the prototype Gloster Whittle\n"
				+ "jet, Mr Geoffrey Bone, a 73-year-old engineer, and Mr Charles McClure, a\n"
				+ "75-year-old pilot, returned to RAF Cranwell. They are seen in front of a\n"
				+ "restored Meteor NF 11. Sir Frank was unable to attend because of ill-health.\n"
				+ "The Gloster Whittle was not the first jet to fly: a Heinkel 178 had its\n"
				+ "maiden flight in August 1939, 21 months before the British aircraft.\n"
				+ "Correction (published 16th May 1991).\n"
				+ "THE PICTURE of a Gloster Whittle jet on Page 7 of the issue of Tuesday May\n"
				+ "14, was taken at Bournemouth Airport and not at RAF Cranwell as stated in\n" + "the caption.\n";
		String pub = "The Financial Times\n";
		String page = "\nLondon Page 7 Photograph (Omitted).\n";

		initExpectedDocft911(docno, profile, date, headline, text, pub, page);
		assertDocsEqual(ft911docs.get(0), expectedDoc);

		System.out.println("Test Complete >>> financialTimes_ft911_1_FirstDocTest()");
	}
	@Ignore
	@Test
	public void federalRegister_fr940104_0_Test() {
		System.out.println("Begin Test >>> federalRegister_fr940104_0_Test()");
		String docno = " FR940104-0-00001 ";
		String parent = " FR940104-0-00001 ";
		String text = "\n \n\n\n\n\n\n\n\n\n\nFederal Register"
				+ "\n\n\n\n;/;Vol. 59, No. 2;/;Tuesday, January 4, 1994;/;"
				+ "Rules and Regulations\n\n\n\n\n\n\nVol. 59, No. 2\n\n\n\n\n\nTuesday, January 4, 1994"
				+ "\n\n\n\n\n\n\n\n\n\n";
		initExpectedDoc_fr940104_0(docno, parent, text);
		assertDocsEqual(fr940104docs.get(0), expectedDoc);

		System.out.println("Test Complete >>> federalRegister_fr940104_0_Test()");
	}

	private void initExpectedDoc_fr940104_0(String docno, String parent, String text) {
		expectedDoc.setDocno(docno);
		expectedDoc.addTaggedContent(new CustomTag("PARENT", parent));
		expectedDoc.addTaggedContent(new CustomTag("TEXT", text));
	}

	private void assertDocsEqual(CustomDocument actual, CustomDocument expected) {
		assertEquals(expected.getDocno(), actual.getDocno());
		assertEquals(expected.getOtherInfo().size(), actual.getOtherInfo().size());
		for (int i = 0; i < actual.getOtherInfo().size(); i++) {
			assertEquals(expected.getOtherInfo().get(i).getTag(), actual.getOtherInfo().get(i).getTag());
			assertEquals(expected.getOtherInfo().get(i).getContentAsString(),
					actual.getOtherInfo().get(i).getContentAsString());
		}
	}

	private void initExpectedDocft911(String docno, String profile, String date, String headline, String text,
			String pub, String page) {
		expectedDoc.setDocno(docno);
		expectedDoc.addTaggedContent(new CustomTag("PROFILE", profile));
		expectedDoc.addTaggedContent(new CustomTag("DATE", date));
		expectedDoc.addTaggedContent(new CustomTag("HEADLINE", headline));
		expectedDoc.addTaggedContent(new CustomTag("TEXT", text));
		expectedDoc.addTaggedContent(new CustomTag("PUB", pub));
		expectedDoc.addTaggedContent(new CustomTag("PAGE", page));
	}

	private void initExpectedDocft934(String docno, String profile, String date, String headline, String byline,
			String text, String xx1, String co, String xx2, String cn, String xx3, String in, String xx4, String tp,
			String pub, String page) {
		expectedDoc.setDocno(docno);
		expectedDoc.addTaggedContent(new CustomTag("PROFILE", profile));
		expectedDoc.addTaggedContent(new CustomTag("DATE", date));
		expectedDoc.addTaggedContent(new CustomTag("HEADLINE", headline));
		expectedDoc.addTaggedContent(new CustomTag("BYLINE", byline));
		expectedDoc.addTaggedContent(new CustomTag("TEXT", text));
		expectedDoc.addTaggedContent(new CustomTag("XX", xx1));
		expectedDoc.addTaggedContent(new CustomTag("CO", co));
		expectedDoc.addTaggedContent(new CustomTag("XX", xx2));
		expectedDoc.addTaggedContent(new CustomTag("CN", cn));
		expectedDoc.addTaggedContent(new CustomTag("XX", xx3));
		expectedDoc.addTaggedContent(new CustomTag("IN", in));
		expectedDoc.addTaggedContent(new CustomTag("XX", xx4));
		expectedDoc.addTaggedContent(new CustomTag("TP", tp));
		expectedDoc.addTaggedContent(new CustomTag("PUB", pub));
		expectedDoc.addTaggedContent(new CustomTag("PAGE", page));
	}
}
