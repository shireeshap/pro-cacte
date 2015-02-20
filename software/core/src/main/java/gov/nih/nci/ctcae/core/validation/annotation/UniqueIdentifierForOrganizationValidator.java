package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import gov.nih.nci.ctcae.core.repository.secured.OrganizationRepository;

import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author Amey
 * UniqueIdentifierForOrganizationValidator class
 */
public class UniqueIdentifierForOrganizationValidator extends AbstractValidator<UniqueIdentifierForOrganization> {
	String message;
	OrganizationRepository organizationRepository;

	public boolean validate(String organizationId, String nciInstituteCode) {
		OrganizationQuery query = new OrganizationQuery();
		query.filterByNciCodeExactMatch(nciInstituteCode);
		List<Organization> organizations = (List<Organization>) organizationRepository.find(query);
		
		if(organizations != null && !organizations.isEmpty()) {
			if(organizationId == null || StringUtils.isEmpty(organizationId)) {
				return true;
			} else {
				boolean flag = false;
				for(Organization organization : organizations) {
					if(organization.getId().equals(Integer.parseInt(organizationId))) {
						flag = true;
					}
				}
				if(!flag) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public void initialize(UniqueIdentifierForOrganization uniqueIdentifierForOrganization) {
		this.message = uniqueIdentifierForOrganization.message();
	}

	@Override
	public String message() {
		return message;
	}
	
	public void setOrganizationRepository(OrganizationRepository organizationRepository) {
		this.organizationRepository = organizationRepository;
	}
}