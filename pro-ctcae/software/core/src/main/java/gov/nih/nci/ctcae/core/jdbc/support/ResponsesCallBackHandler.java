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
 * ResponsesCallBackHandler class. 
 * Used in overall study report generation.
 */
public class ResponsesCallBackHandler implements RowCallbackHandler {
	
	public Map<Integer, List<ResponseWrapper>> map;
	public static String SCHEDULE_ID = "scheduleId";
	public static String CRF_PAGE_ITEM_ID = "crfPageItemId";
	public static String PRO_QUESTION_ID = "proQuestionId";
	public static String QUESTION_ID = "question_type";
	public static String TERM_ENGLIGH = "term_english";
	public static String VALUE_ENGLISH = "value_english";
	public static String RESPONSE_DATE = "responseDate";
	public static String RESPONSE_CODE = "responseCode";
	public static String QUESTION_POSITION = "questionPosition";
	public static String GENDER = "gender";
	
	public ResponsesCallBackHandler(Map<Integer, List<ResponseWrapper>> map){
		this.map = map;
	}
	
	@Override
	public void processRow(ResultSet rs) throws SQLException {
		List<ResponseWrapper> records;
		records = map.get(rs.getInt(SCHEDULE_ID));
		if(records == null){
			records = new ArrayList<ResponseWrapper>();
			map.put(rs.getInt(SCHEDULE_ID), records);
		}
		ResponseWrapper wrapper = new ResponseWrapper();
		wrapper.setScheduleId(rs.getInt(SCHEDULE_ID));
		wrapper.setCrfPageItemId(rs.getInt(CRF_PAGE_ITEM_ID));
		wrapper.setProQuestionId(rs.getInt(PRO_QUESTION_ID));
		wrapper.setGender(rs.getString(GENDER));
		wrapper.setQuestionPosition(rs.getString(QUESTION_POSITION));
		wrapper.setResponseDate(rs.getDate(RESPONSE_DATE));
		wrapper.setQuestionType(ProCtcQuestionType.getProCtcQuestionType(rs.getString("question_type")));
		wrapper.setTermEnglish(rs.getString(TERM_ENGLIGH));
		wrapper.setValueEnglish(rs.getString(VALUE_ENGLISH));
		wrapper.setResponseCode(rs.getString(RESPONSE_CODE));
		records.add(wrapper);
	}

}
