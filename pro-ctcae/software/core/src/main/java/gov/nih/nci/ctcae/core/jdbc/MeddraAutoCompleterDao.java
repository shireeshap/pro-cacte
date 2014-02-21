package gov.nih.nci.ctcae.core.jdbc;

import gov.nih.nci.ctcae.core.jdbc.support.MatchingMeddraTermsCallBackHandler;
import gov.nih.nci.ctcae.core.jdbc.support.MeddraAutoCompleterWrapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;

public class MeddraAutoCompleterDao {
    JdbcTemplate jdbcTemplate;
    private final static String ENGLISH = "en";
    
    @SuppressWarnings("unchecked")
	public List<MeddraAutoCompleterWrapper> getMatchingMeddraTerms(String searchString, String language){
    	String fetchMatchingMeddraTerms;
    	if(ENGLISH.equals(language)){
    		fetchMatchingMeddraTerms = " SELECT mlltv.meddra_term_english, " +
    			" difference(lower(mlltv.meddra_term_english), '" + searchString + "') as soundex_rank, " +
    			" hasSimilarDmetaphoneTokens('"+ searchString +"', mlltv.meddra_term_english) as dmetaphone_rank " +
    			" FROM meddra_llt_vocab mlltv " +
    			" left join meddra_llt mllt on mllt.id = mlltv.meddra_llt_id " +
    			" where " +
    			" ((mllt.participant_added is NULL or mllt.participant_added = FALSE) and mllt.currency = 'Y') and " +
    			" (mlltv.meddra_term_english ilike '%" + searchString + "%' or difference(lower(mlltv.meddra_term_english), '" + searchString + "') >= 3 " +
    			" or hasSimilarDmetaphoneTokens('"+ searchString +"', mlltv.meddra_term_english) = true ) ";
    	} else {
    		fetchMatchingMeddraTerms = " SELECT mlltv.meddra_term_spanish, " +
    		" difference(lower(mlltv.meddra_term_spanish), '" + searchString + "') as soundex_rank, " +
			" hasSimilarDmetaphoneTokens('"+ searchString +"', mlltv.meddra_term_spanish) as dmetaphone_rank " +
    		" FROM meddra_llt_vocab mlltv " +
    		" left join meddra_llt mllt on mllt.id = mlltv.meddra_llt_id " +
    		" where " +
    		" ((mllt.participant_added is NULL or mllt.participant_added = FALSE) and mllt.currency = 'Y') and " +
    		" (unaccent(mlltv.meddra_term_spanish) ilike '%" + searchString + "%' or difference(lower(unaccent(mlltv.meddra_term_spanish)), '" + searchString + "') >= 3 " +
    		" or hasSimilarDmetaphoneTokens('"+ searchString +"', mlltv.meddra_term_spanish) = true ) ";
    	}

    	List<MeddraAutoCompleterWrapper> result = new ArrayList<MeddraAutoCompleterWrapper>();
   	 jdbcTemplate.query(fetchMatchingMeddraTerms, new MatchingMeddraTermsCallBackHandler(language, result));
   	 return result;
    }
    
    @Required
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
    	this.jdbcTemplate = jdbcTemplate;
    }
}