package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.AddedProCtcQuestionWrapper;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowCallbackHandler;

public class AddedProCtcQuestionCallBackHandler implements RowCallbackHandler {
	
	public Map<Integer, List<AddedProCtcQuestionWrapper>> map;
	public static String SCHEDULE_ID = "scheduleId";
	public static String PRO_QUESTION_ID = "proQuestionId";
	public static String TERM_ENGLIGH = "term_english";
	public static String QUESTION_ID = "question_type";
	public static String VALUE_ENGLISH = "value_english";
	
	AddedProCtcQuestionCallBackHandler(Map<Integer, List<AddedProCtcQuestionWrapper>> map){
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
		wrapper.setQuestionType(ProCtcQuestionType.getByCode(rs.getString("question_type")));
		wrapper.setTermEnglish(rs.getString(TERM_ENGLIGH));
		wrapper.setValueEnglish(rs.getString(VALUE_ENGLISH));
		records.add(wrapper);
	}
}
