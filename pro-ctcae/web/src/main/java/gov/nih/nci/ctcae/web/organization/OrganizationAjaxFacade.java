package gov.nih.nci.ctcae.web.organization;

import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import gov.nih.nci.ctcae.core.repository.OrganizationRepository;
import gov.nih.nci.ctcae.web.tools.ObjectTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class OrganizationAjaxFacade.
 *
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class OrganizationAjaxFacade {

    /**
     * The organization repository.
     */
    private OrganizationRepository organizationRepository;

    /**
     * The log.
     */
    protected final Log logger = LogFactory.getLog(getClass());


    /**
     * Match organization.
     *
     * @param text the text
     * @return the list< organization>
     */
    public List<Organization> matchOrganization(final String text) {
        logger.info("in match organization method. Search string :" + text);
        OrganizationQuery organizationQuery = new OrganizationQuery();
        organizationQuery.filterByOrganizationNameOrNciInstituteCode(text);
        List<Organization> organizations = (List<Organization>) organizationRepository.find(organizationQuery);
        return ObjectTools.reduceAll(organizations, "id", "name", "nciInstituteCode");

    }


    /**
     * Sets the organization repository.
     *
     * @param organizationRepository the new organization repository
     */
    @Required
    public void setOrganizationRepository(final OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

}
