package gov.nih.nci.ctcae.core.jdbc.support;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author AmeyS
 * Used by Auto-completer to added additional symptoms in participant interface.
 * The symptoms are matched using fuzzy matching algorithm and ranked based on set thresholds.
 */
public class MatchingMeddraTermsRowMapper implements RowMapper {
	
	private static String MEDDRA_TERM_ENGLISH = "meddra_term_english";
	private static String MEDDRA_TERM_SPANISH = "meddra_term_spanish";
	private static String SOUNDEX_RANK = "soundex_rank";
	private static String DMETAPHONE_RANK = "dmetaphone_rank"; 
	private static String ENGLIGH = "en";
	String term;
	MeddraAutoCompleterWrapper wrapper;
	
	public MatchingMeddraTermsRowMapper(String language){
		if(ENGLIGH.equals(language)){
			term = MEDDRA_TERM_ENGLISH;
		} else {
			term = MEDDRA_TERM_SPANISH;
		}
	}

	@Override
	public MeddraAutoCompleterWrapper mapRow(ResultSet rs, int rowNum) throws SQLException {
		wrapper = new MeddraAutoCompleterWrapper();
		wrapper.setMeddraTerm(rs.getString(term));
		wrapper.setSoundexRank(rs.getString(SOUNDEX_RANK));
		wrapper.setdMetaphoneRank(rs.getString(DMETAPHONE_RANK));
		
		return wrapper; 
	}
}
