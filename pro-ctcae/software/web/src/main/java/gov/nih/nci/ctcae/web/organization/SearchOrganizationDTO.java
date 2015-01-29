package gov.nih.nci.ctcae.web.organization;

/**
 * @author Amey
 * SearchOrganizationDTO class.
 * Data transfer object used in search organization screen.
 */
public class SearchOrganizationDTO {
	private String nciInstituteCode;
	private String organizationName;
	private String study;
	private String actions;

	public String getNciInstituteCode() {
		return nciInstituteCode;
	}
	public void setNciInstituteCode(String nciIdentifier) {
		this.nciInstituteCode = nciIdentifier;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getStudy() {
		return study;
	}
	public void setStudy(String study) {
		this.study = study;
	}

	public String getActions() {
		return actions;
	}
	public void setActions(String actions) {
		this.actions = actions;
	}
}
