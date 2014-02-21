package gov.nih.nci.ctcae.core.utils.ranking;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gov.nih.nci.ctcae.core.jdbc.support.MeddraAutoCompleterWrapper;

import org.apache.commons.lang.StringUtils;

/**
 * @author Amey
 * MeddraTermRanker Class
 * Used to rank the meddra terms based on the following criteria:
 *  1. Highest rank if the searchString exactly matches the meddraTerm or is a substring 
 *     of the meddraTerm (Rank of WHOLE_WORD_MATCH)
 *  2. Mid rank to meddraTerm if dmetaphone of any of the tokens in that meddraTerm matches exactly with the 
 *     dmetaphone of searchString. i.e if hasSimilarDmetaphoneTokens() function returns true (Rank of DMETAPHONE_MATCH)
 *  3. Lowest rank to a meddraTerm if is within the soundex threshold limit. i.e difference() 
 *     returns true (Rank of SOUNDEX_MATCH)
 */
public class MeddraTermRanker {
    private static final int WHOLE_SENTENCE_MATCH = 100000;
    private static final int BEGINING_OF_SENTENCE = 50000;
    private static final int WHOLE_WORD_MATCH = 10000;
    private static final int BEGINING_OF_WORD = 1000;
    private static final int PART_OF_SENTENCE = 500;
	private static final int DMETAPHONE_MATCH = 50;
	private static final int SOUNDEX_MATCH = 10;
	private String searchStr;
	private String escapedSearchStr;
	private int patternLength;
	private Pattern p;
	private static final String TRUE = "t";
	private static final String FALSE = "f";
	 
	 public MeddraTermRanker(String searchStr){
		 this.searchStr= searchStr;
		 if(!StringUtils.isEmpty(searchStr)){
			 patternLength = searchStr.length();
		 }
		 escapedSearchStr = searchStr;
		 char[] metachars = new char[]{'\\','(',')','[',']','^','$','.','?','+','*','|'};
	        if(StringUtils.containsAny(searchStr, metachars)){
	           String[] metaStr = {"\\","(",")","[","]","^","$",".","?","+","*","|"};
	           String[] metaEscapedStr = {"\\\\","\\(","\\)","\\[","\\]","\\^","\\$","\\.","\\?","\\+","\\*","\\|"};
	           escapedSearchStr = StringUtils.replaceEach(searchStr, metaStr, metaEscapedStr);
	        }
	     p = Pattern.compile(escapedSearchStr, Pattern.CASE_INSENSITIVE);
		 
	 }
	 
		public RankedObject rank(MeddraAutoCompleterWrapper obj, Serializer<String> serializer){
	        RankedObject<MeddraAutoCompleterWrapper> rankedObject = new RankedObject(obj);
	        String meddraTerm = serializer.serialize(obj.getMeddraTerm());
	        
	        Matcher m = p.matcher(meddraTerm);

	        // whole sentence match
	        if(StringUtils.equalsIgnoreCase(meddraTerm, searchStr)){
	            rankedObject.addToRank(WHOLE_SENTENCE_MATCH);
	        }
	        // is searchString a subString of the meddraTerm
	        if(m.find()){
	        	rankedObject = computeRankForContainedSearchString(rankedObject, meddraTerm, m);
	        } else if(TRUE.equals(obj.getdMetaphoneRank())){
	        	rankedObject.addToRank(DMETAPHONE_MATCH);
	        } else {
	        	rankedObject.addToRank(SOUNDEX_MATCH);
	        }
	        
	        return rankedObject;
	    }
		
		private RankedObject<MeddraAutoCompleterWrapper> computeRankForContainedSearchString(RankedObject<MeddraAutoCompleterWrapper> rankedObject, String meddraTerm, Matcher m){
			int meddraTermLength = meddraTerm.length();
            int start = m.start();
            if(start == 0){
                // beginning of sentence
                rankedObject.addToRank(BEGINING_OF_SENTENCE);
            } else {
                int i = start - 1;
                int j = start - 2;
                char iChar = meddraTerm.charAt(i);

                // beginning of sentence.
                if((j == 0 && meddraTerm.charAt(j) == '(') || (i == 0 && iChar == '(') ) rankedObject.addToRank(BEGINING_OF_SENTENCE);

                if(iChar == ' ' || iChar == '('){

                    int k = start + patternLength;
                    if(k == meddraTermLength ||  ( k < meddraTermLength && (meddraTerm.charAt(k) == ' ' || meddraTerm.charAt(k) == ')') )){
                       // whole word match within the sentence
                        rankedObject.addToRank(WHOLE_WORD_MATCH);
                    }

                    //beginning of a word
                    rankedObject.addToRank(BEGINING_OF_WORD);
                }
            }

            // part of sentence
            rankedObject.addToRank(PART_OF_SENTENCE);

            rankedObject.substractFromRank(start);
            return rankedObject;
		}
}