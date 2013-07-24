package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.AddedMeddraQuestionWrapper;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ResponseWrapper;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class AddedMeddraQuestionRowMapper implements RowMapper {
	
	public static String SCHEDULE_ID = "scheduleId";
	public static String MEDDRA_QUESTION_ID = "meddraQuestionId";
	public static String TERM_ENGLIGH = "meddra_term_english";
	public static String QUESTION_ID = "question_type";
	public static String VALUE_ENGLISH = "value_english";
	

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		AddedMeddraQuestionWrapper wrapper = new AddedMeddraQuestionWrapper();
		wrapper.setScheduleId(rs.getInt(SCHEDULE_ID));
		wrapper.setProQuestionId(rs.getInt(MEDDRA_QUESTION_ID));
		wrapper.setQuestionType(ProCtcQuestionType.getByCode(rs.getString("question_type")));
		wrapper.setTermEnglish(rs.getString(TERM_ENGLIGH));
		wrapper.setValueEnglish(rs.getString(VALUE_ENGLISH));
		return wrapper;
	}

}
