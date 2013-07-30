package gov.nih.nci.ctcae.web.reports;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowCallbackHandler;

/**
 * @author AmeyS
 * ResponseModesCallBackHandler class. 
 * Used in overall study report generation.
 */
public class ResponseModesCallBackHandler implements RowCallbackHandler {
	
	public Map<Integer, String> map;
	public static String SCHEDULE_ID = "scheduleId";
	public static String RESPONSE_MODE = "responseMode";
	
	
	ResponseModesCallBackHandler(Map<Integer, String> map){
		this.map = map;
	}

	@Override
	public void processRow(ResultSet rs) throws SQLException {
		String responseMode = map.get(rs.getInt(SCHEDULE_ID));
		if(responseMode == null){
			responseMode = rs.getString(RESPONSE_MODE);
			map.put(rs.getInt(SCHEDULE_ID), responseMode);
		} else {
			responseMode = responseMode + ", " + rs.getString(RESPONSE_MODE);
			map.put(rs.getInt(SCHEDULE_ID), responseMode);
		}
	}
}
