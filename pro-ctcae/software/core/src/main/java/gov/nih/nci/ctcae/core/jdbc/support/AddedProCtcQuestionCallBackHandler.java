package gov.nih.nci.ctcae.core.jdbc.support;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowCallbackHandler;

/**
 * @author AmeyS
 * AddedProCtcQuestionCallBackHandler class. 
 * Used in overall study report generation.
 */
public class AddedProCtcQuestionCallBackHandler implements RowCallbackHandler {
	
	public Map<Integer, List<AddedProCtcQuestionWrapper>> map;
	public static String SCHEDULE_ID = "scheduleId";
	public static String PRO_QUESTION_ID = "proQuestionId";
	public static String TERM_ENGLIGH = "term_english";
	public static String QUESTION_TYPE = "question_type";
	public static String VALUE_ENGLISH = "value_english";
	public static String RESPONSE_CODE = "responseCode";
	public static String QUESTION_POSISITION = "questionPosition";
	public static String GENDER = "gender";
	
	public AddedProCtcQuestionCallBackHandler(Map<Integer, List<AddedProCtcQuestionWrapper>> map){
		this.map = map;
	}
	
	@Override
	public void processRow(ResultSet rs) throws SQLException {
		List<AddedProCtcQuestionWrapper> records;
		records = map.get(rs.getInt(SCHEDULE_ID));
		if(records == null){
			records = new ArrayList<AddedProCtcQuestionWrapper>();
			map.put(rs.getInt(SCHEDULE_ID), records);
		}
		AddedProCtcQuestionWrapper wrapper = new AddedProCtcQuestionWrapper();
		wrapper.setScheduleId(rs.getInt(SCHEDULE_ID));
		wrapper.setProQuestionId(rs.getInt(PRO_QUESTION_ID));
		wrapper.setQuestionPosition(rs.getString(QUESTION_POSISITION));
		wrapper.setGender(rs.getString(GENDER));
		wrapper.setResponseCode(rs.getString(RESPONSE_CODE));
		wrapper.setQuestionType(ProCtcQuestionType.getProCtcQuestionType(rs.getString(QUESTION_TYPE)));
		wrapper.setTermEnglish(rs.getString(TERM_ENGLIGH));
		wrapper.setValueEnglish(rs.getString(VALUE_ENGLISH));
		records.add(wrapper);
	}
}
