package gov.nih.nci.ctcae.core.jdbc.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import org.springframework.jdbc.core.RowCallbackHandler;

/**
 * @author AmeyS
 * FirstResponseDateCallBackHandler class. 
 * Used in overall study report generation.
 */
public class FirstResponseDateCallBackHandler implements RowCallbackHandler {
	
	public Map<Integer, Date> map;
	public static String SCHEDULE_ID = "scheduleId";
	public static String FIRST_RESPONSE_DATE = "firstResponseDate";
	
	
	public FirstResponseDateCallBackHandler(Map<Integer, Date> map){
		this.map = map;
	}

	@Override
	public void processRow(ResultSet rs) throws SQLException {
		Date firstResponseDate = map.get(rs.getInt(SCHEDULE_ID));
		if(firstResponseDate == null){
			map.put(rs.getInt(SCHEDULE_ID), rs.getDate(FIRST_RESPONSE_DATE));
		}
	}
}
