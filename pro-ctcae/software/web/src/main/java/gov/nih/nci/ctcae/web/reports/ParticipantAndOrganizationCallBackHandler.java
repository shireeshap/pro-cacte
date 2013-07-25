package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.ParticipantAndOganizationWrapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.RowCallbackHandler;

public class ParticipantAndOrganizationCallBackHandler implements RowCallbackHandler {
	
	public Map<Integer, List<ParticipantAndOganizationWrapper>> map;
	public static String SCHEDULE_ID = "scheduleId";
	public static String MRN_IDENTIFIER = "mrn_identifier";
	public static String FIRST_NAME = "first_name";
	public static String LAST_NAME = "last_name";
	public static String ORGANIZATION_NAME = "organizationName";
	
	ParticipantAndOrganizationCallBackHandler(Map<Integer, List<ParticipantAndOganizationWrapper>> map){
		this.map = map;
	}

	@Override
	public void processRow(ResultSet rs) throws SQLException {
		List<ParticipantAndOganizationWrapper> records;
		records = map.get(rs.getInt(SCHEDULE_ID));
		if(records == null){
			records = new ArrayList<ParticipantAndOganizationWrapper>();
			map.put(rs.getInt(SCHEDULE_ID), records);
		}
		ParticipantAndOganizationWrapper wrapper = new ParticipantAndOganizationWrapper();
		wrapper.setScheduleId(rs.getInt(SCHEDULE_ID));
		wrapper.setMrnIdentifier(rs.getString(MRN_IDENTIFIER));
		wrapper.setFirstName(rs.getString(FIRST_NAME));
		wrapper.setLastName(rs.getString(LAST_NAME));
		wrapper.setOrganizationName(rs.getString(ORGANIZATION_NAME));
		records.add(wrapper);
	}
}
