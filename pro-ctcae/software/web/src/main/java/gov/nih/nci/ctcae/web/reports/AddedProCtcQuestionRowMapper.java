package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.AddedProCtcQuestionWrapper;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ResponseWrapper;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class AddedProCtcQuestionRowMapper implements RowMapper {
	
	public static String SCHEDULE_ID = "scheduleId";
	public static String PRO_QUESTION_ID = "proQuestionId";
	public static String TERM_ENGLIGH = "term_english";
	public static String QUESTION_ID = "question_type";
	public static String VALUE_ENGLISH = "value_english";
	

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		AddedProCtcQuestionWrapper wrapper = new AddedProCtcQuestionWrapper();
		wrapper.setScheduleId(rs.getInt(SCHEDULE_ID));
		wrapper.setProQuestionId(rs.getInt(PRO_QUESTION_ID));
		wrapper.setQuestionType(ProCtcQuestionType.getByCode(rs.getString("question_type")));
		wrapper.setTermEnglish(rs.getString(TERM_ENGLIGH));
		wrapper.setValueEnglish(rs.getString(VALUE_ENGLISH));
		return wrapper;
	}

}
