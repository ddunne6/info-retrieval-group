package tcd.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermStates;
import org.apache.lucene.queries.CommonTermsQuery;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.BooleanClause.Occur;

//Custom query class, finds low-frequency (rare) terms in the query and adds boosts to them, increasing their supposed relevance.
//Don't worry about intergrating now.

public class CommonTermsLowFreqBoostQuery extends CommonTermsQuery {
	
	
	public CommonTermsLowFreqBoostQuery(Occur highFreqOccur, Occur lowFreqOccur, float maxTermFrequency,  float customLowFreqBoost) {
		
		//Allows for the manipulation of the lowFreqBoost protected variables from the parent class
		super(highFreqOccur, lowFreqOccur, maxTermFrequency);
		this.lowFreqBoost = customLowFreqBoost;

	}

	public void setLowFreqBoost(float customLowFreqBoost) {
		
		this.lowFreqBoost = customLowFreqBoost;
		
	}
	
	protected Query buildQuery(final int maxDoc,
                             final TermStates[] contextArray, final Term[] queryTerms) {
    List<Query> lowFreqQueries = new ArrayList<>();
    List<Query> highFreqQueries = new ArrayList<>();
    for (int i = 0; i < queryTerms.length; i++) {
      TermStates termStates = contextArray[i];
      if (termStates == null) {
        lowFreqQueries.add(newTermQuery(queryTerms[i], null));
      } else {
        if ((maxTermFrequency >= 1f && termStates.docFreq() > maxTermFrequency)
            || (termStates.docFreq() > (int) Math.ceil(maxTermFrequency
                * (float) maxDoc))) {
          highFreqQueries
              .add(newTermQuery(queryTerms[i], termStates));
        } else {
          lowFreqQueries.add(newTermQuery(queryTerms[i], termStates));
        }
      }
    }
    final int numLowFreqClauses = lowFreqQueries.size();
    final int numHighFreqClauses = highFreqQueries.size();
    Occur lowFreqOccur = this.lowFreqOccur;
    Occur highFreqOccur = this.highFreqOccur;
    int lowFreqMinShouldMatch = 0;
    int highFreqMinShouldMatch = 0;
    if (lowFreqOccur == Occur.SHOULD && numLowFreqClauses > 0) {
      lowFreqMinShouldMatch = calcLowFreqMinimumNumberShouldMatch(numLowFreqClauses);
    }
    if (highFreqOccur == Occur.SHOULD && numHighFreqClauses > 0) {
      highFreqMinShouldMatch = calcHighFreqMinimumNumberShouldMatch(numHighFreqClauses);
    }
    if (lowFreqQueries.isEmpty()) {
      /*
       * if lowFreq is empty we rewrite the high freq terms in a conjunction to
       * prevent slow queries.
       */
      if (highFreqMinShouldMatch == 0 && highFreqOccur != Occur.MUST) {
        highFreqOccur = Occur.MUST;
      }
    }
    BooleanQuery.Builder builder = new BooleanQuery.Builder();

    if (lowFreqQueries.isEmpty() == false) {
      BooleanQuery.Builder lowFreq = new BooleanQuery.Builder();
      for (Query query : lowFreqQueries) {
        lowFreq.add(query, lowFreqOccur);
      }
      lowFreq.setMinimumNumberShouldMatch(lowFreqMinShouldMatch);
      Query lowFreqQuery = lowFreq.build();
      builder.add(new BoostQuery(lowFreqQuery, lowFreqBoost), Occur.SHOULD); //Changed to SHOULD from MUST, ensure all terms are used.
    }
    if (highFreqQueries.isEmpty() == false) {
      BooleanQuery.Builder highFreq = new BooleanQuery.Builder();
      for (Query query : highFreqQueries) {
        highFreq.add(query, highFreqOccur);
      }
      highFreq.setMinimumNumberShouldMatch(highFreqMinShouldMatch);
      Query highFreqQuery = highFreq.build();
      builder.add(new BoostQuery(highFreqQuery, highFreqBoost), Occur.SHOULD);
    }
    return builder.build();
  }
	
}
