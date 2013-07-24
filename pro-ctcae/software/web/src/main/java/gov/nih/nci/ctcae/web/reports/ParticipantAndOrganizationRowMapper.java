package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.ParticipantAndOganizationWrapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
public class ParticipantAndOrganizationRowMapper implements RowMapper {
	
	public static String MRN_IDENTIFIER = "mrn_identifier";
	public static String FIRST_NAME = "first_name";
	public static String LAST_NAME = "last_name";
	public static String ORGANIZATION_NAME = "organizationName";
	

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		ParticipantAndOganizationWrapper wrapper = new ParticipantAndOganizationWrapper();
		wrapper.setMrnIdentifier(rs.getString(MRN_IDENTIFIER));
		wrapper.setFirstName(rs.getString(FIRST_NAME));
		wrapper.setLastName(rs.getString(LAST_NAME));
		wrapper.setOrganizationName(rs.getString(ORGANIZATION_NAME));
		return wrapper;
	}

}
