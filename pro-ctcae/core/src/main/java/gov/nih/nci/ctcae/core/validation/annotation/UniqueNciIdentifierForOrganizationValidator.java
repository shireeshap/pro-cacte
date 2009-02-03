package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import gov.nih.nci.ctcae.core.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;

// TODO: Auto-generated Javadoc
/**
 * The Class UniqueNciIdentifierForOrganizationValidator.
 * 
 * @author Vinay Kumar
 * @crated Oct 27, 2008
 */
public class UniqueNciIdentifierForOrganizationValidator extends AbstractValidator<UniqueNciIdentifierForOrganization> {

	/** The message. */
	String message;

	/** The organization repository. */
	private OrganizationRepository organizationRepository;

	/* (non-Javadoc)
	 * @see gov.nih.nci.ctcae.core.validation.annotation.AbstractValidator#validate(java.lang.Object)
	 */
	public boolean validate(final Object value) {
		if (value instanceof String) {
			OrganizationQuery organizationQuery = new OrganizationQuery();
			organizationQuery.filterByNciCodeExactMatch((String) value);
			Collection<Organization> organizationList = organizationRepository
				.find(organizationQuery);
			return (organizationList == null || organizationList.isEmpty()) ? true : false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.ctcae.core.validation.annotation.Validator#initialize(java.lang.annotation.Annotation)
	 */
	public void initialize(UniqueNciIdentifierForOrganization uniqueNciIdentifierForResearchStaff) {
		message = uniqueNciIdentifierForResearchStaff.message();
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.ctcae.core.validation.annotation.Validator#message()
	 */
	public String message() {
		return message;
	}

	/**
	 * Sets the organization repository.
	 * 
	 * @param organizationRepository the new organization repository
	 */
	@Required
	public void setOrganizationRepository(OrganizationRepository organizationRepository) {
		this.organizationRepository = organizationRepository;
	}
}