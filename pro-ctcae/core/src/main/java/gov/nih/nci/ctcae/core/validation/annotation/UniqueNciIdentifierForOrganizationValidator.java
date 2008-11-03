package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import gov.nih.nci.ctcae.core.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;

/**
 * @author Saurabh Agrawal
 * @crated Oct 27, 2008
 */
public class UniqueNciIdentifierForOrganizationValidator implements
        Validator<UniqueNciIdentifierForOrganization> {

    String message;

    private OrganizationRepository organizationRepository;

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

    public void initialize(UniqueNciIdentifierForOrganization uniqueNciIdentifierForResearchStaff) {
        message = uniqueNciIdentifierForResearchStaff.message();
    }

    public String message() {
        return message;
    }

    @Required
    public void setOrganizationRepository(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }
}