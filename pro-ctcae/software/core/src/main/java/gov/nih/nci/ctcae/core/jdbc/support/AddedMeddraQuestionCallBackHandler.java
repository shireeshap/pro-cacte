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
 * AddedMeddraQuestionCallBackHandler class. 
 * Used in overall study report generation.
 */
public class AddedMeddraQuestionCallBackHandler implements RowCallbackHandler {
	
	public Map<Integer, List<AddedMeddraQuestionWrapper>> map;
	public static String SCHEDULE_ID = "scheduleId";
	public static String MEDDRA_QUESTION_ID = "meddraQuestionId";
	public static String TERM_ENGLIGH = "meddra_term_english";
	public static String QUESTION_TYPE = "question_type";
	public static String VALUE_ENGLISH = "value_english";
	public static String DISPLAY_ORDER = "displayOrder";
	
	public AddedMeddraQuestionCallBackHandler(Map<Integer, List<AddedMeddraQuestionWrapper>> map){
		this.map = map;
	}

	@Override
	public void processRow(ResultSet rs) throws SQLException {
		List<AddedMeddraQuestionWrapper> records;
		records = map.get(rs.getInt(SCHEDULE_ID));
		if(records == null){
			records = new ArrayList<AddedMeddraQuestionWrapper>();
			map.put(rs.getInt(SCHEDULE_ID), records);
		}		
		AddedMeddraQuestionWrapper wrapper = new AddedMeddraQuestionWrapper();
		wrapper.setScheduleId(rs.getInt(SCHEDULE_ID));
		wrapper.setMeddraQuestionId(rs.getInt(MEDDRA_QUESTION_ID));
		wrapper.setQuestionType(ProCtcQuestionType.getProCtcQuestionType(rs.getString(QUESTION_TYPE)));
		wrapper.setTermEnglish(rs.getString(TERM_ENGLIGH));
		wrapper.setValueEnglish(rs.getString(VALUE_ENGLISH));
		wrapper.setDisplayOrder(rs.getString(DISPLAY_ORDER));
		records.add(wrapper);
	}
}
