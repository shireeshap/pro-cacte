package gov.nih.nci.ctcae.core.jdbc.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author AmeyS
 * Used by Auto-completer to added additional symptoms in participant interface.
 * The symptoms are matched using fuzzy matching algorithm and ranked based on set thresholds.
 */
public class MatchingMeddraTermsCallBackHandler implements RowCallbackHandler {
	
	private static String MEDDRA_TERM_ENGLISH = "meddra_term_english";
	private static String MEDDRA_TERM_SPANISH = "meddra_term_spanish";
	private static String SOUNDEX_RANK = "soundex_rank";
	private static String DMETAPHONE_RANK = "dmetaphone_rank"; 
	private static String ENGLIGH = "en";
	private static String NOS_ENGLISH = "NOS";
	private static String NOS_SPANISH = "NEOM";
	String term;
	String language;
	MeddraAutoCompleterWrapper wrapper;
	List<MeddraAutoCompleterWrapper> result;
	
	public MatchingMeddraTermsCallBackHandler(String language, List<MeddraAutoCompleterWrapper> result){
		this.language = language;
		if(ENGLIGH.equals(language)){
			term = MEDDRA_TERM_ENGLISH;
		} else {
			term = MEDDRA_TERM_SPANISH;
		}
		this.result = result;
	}

	@Override
	public void processRow(ResultSet rs) throws SQLException {
	/*	// need to omit the results fetched by dmetaphone as a result of trailing NOS
    	// meddraTerm = meddraTerm.trim();
        // meddraTerm = StringUtils.chompLast(meddraTerm, NOS);
		if(term.contains(NOS_ENGLISH)){
			if(ENGLIGH.equals(language)){
				
			}
			
		}*/
		wrapper = new MeddraAutoCompleterWrapper();
		wrapper.setMeddraTerm(rs.getString(term));
		wrapper.setSoundexRank(rs.getString(SOUNDEX_RANK));
		wrapper.setdMetaphoneRank(rs.getString(DMETAPHONE_RANK));
		result.add(wrapper);
	}
}
