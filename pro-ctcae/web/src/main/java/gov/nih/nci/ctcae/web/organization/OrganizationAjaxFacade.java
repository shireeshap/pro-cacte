package gov.nih.nci.ctcae.web.organization;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import gov.nih.nci.ctcae.core.repository.OrganizationRepository;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import gov.nih.nci.ctcae.core.domain.Organization;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class OrganizationAjaxFacade {

    private OrganizationRepository organizationRepository;
    protected final Log log = LogFactory.getLog(getClass());

    public List<Organization> matchOrganization(final String text) {
        log.info("in match organization method. Search string :" + text);
        OrganizationQuery organizationQuery = new OrganizationQuery();
        organizationQuery.filterByOrganizationNameOrNciInstituteCode(text);
        List<Organization> organizations = (List<Organization>) organizationRepository.find(organizationQuery);
        return organizations;
    }


    @Required
    public void setOrganizationRepository(final OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

}
