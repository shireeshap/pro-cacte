package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.AppMode;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ResponseWrapper;
import gov.nih.nci.ctcae.core.domain.SpcrfsWrapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ResponsesRowMapper implements RowMapper {
	
	public static String SCHEDULE_ID = "scheduleId";
	public static String CRF_PAGE_ITEM_ID = "crfPageItemId";
	public static String PRO_QUESTION_ID = "proQuestionId";
	public static String QUESTION_ID = "question_type";
	public static String TERM_ENGLIGH = "term_english";
	public static String VALUE_ENGLISH = "value_english";
	

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResponseWrapper wrapper = new ResponseWrapper();
		wrapper.setScheduleId(rs.getInt(SCHEDULE_ID));
		wrapper.setCrfPageItemId(rs.getInt(CRF_PAGE_ITEM_ID));
		wrapper.setProQuestionId(rs.getInt(PRO_QUESTION_ID));
		wrapper.setQuestionType(ProCtcQuestionType.getByCode(rs.getString("question_type")));
		wrapper.setTermEnglish(rs.getString(TERM_ENGLIGH));
		wrapper.setValueEnglish(rs.getString(VALUE_ENGLISH));
		return wrapper;
	}

}
