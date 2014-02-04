package gov.nih.nci.ctcae.core.utils.ranking;

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
	private static final int WHOLE_WORD_MATCH = 10000;
	private static final int DMETAPHONE_MATCH = 1000;
	private static final int SOUNDEX_MATCH = 500;
	private String searchStr;
	private static final String TRUE = "t";
	private static final String FALSE = "f";
	 
	 public MeddraTermRanker(String searchStr){
		 this.searchStr= searchStr;
	 }
	 
		public RankedObject rank(MeddraAutoCompleterWrapper obj, Serializer<String> serializer){
	        RankedObject<MeddraAutoCompleterWrapper> rankedObject = new RankedObject(obj);
	        String meddraTerm = serializer.serialize(obj.getMeddraTerm());
	        
	        // whole word match or substring match by database like query
	        if(StringUtils.equalsIgnoreCase(meddraTerm, searchStr) || meddraTerm.contains(searchStr)){
	        	rankedObject.addToRank(WHOLE_WORD_MATCH);
	        } else if(TRUE.equals(obj.getdMetaphoneRank())){
	        	rankedObject.addToRank(DMETAPHONE_MATCH);
	        } else {
	        	rankedObject.addToRank(SOUNDEX_MATCH);
	        }
	        
	        return rankedObject;
	    }
}