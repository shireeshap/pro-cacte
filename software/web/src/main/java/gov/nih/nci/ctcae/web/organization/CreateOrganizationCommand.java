package gov.nih.nci.ctcae.web.organization;

import gov.nih.nci.ctcae.core.domain.Organization;

/**
 * @author Amey
 * CreateOrganizationCommand class.
 * Command object for createOrganization flow
 */
public class CreateOrganizationCommand {
	String description;
	Organization organization;
	
	public CreateOrganizationCommand() {
		organization = new Organization();
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
}