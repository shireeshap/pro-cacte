package gov.nih.nci.ctcae.core.jdbc.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;

import org.springframework.jdbc.core.RowCallbackHandler;

/**
 * @author AmeyS
 * ResponseModesCallBackHandler class. 
 * Used in overall study report generation.
 */
public class ResponseModesCallBackHandler implements RowCallbackHandler {
	
	public Map<Integer, HashSet<String>> map;
	public static String SCHEDULE_ID = "scheduleId";
	public static String RESPONSE_MODE = "responseMode";
	
	
	public ResponseModesCallBackHandler(Map<Integer, HashSet<String>> map){
		this.map = map;
	}

	@Override
	public void processRow(ResultSet rs) throws SQLException {
		HashSet<String> responseModes = map.get(rs.getInt(SCHEDULE_ID));
		if(responseModes == null){
			responseModes = new HashSet<String>();
			map.put(rs.getInt(SCHEDULE_ID), responseModes);
		} 
		String mode = rs.getString(RESPONSE_MODE);
		if(mode != null){
			responseModes.add(mode);
		}
	}
}
