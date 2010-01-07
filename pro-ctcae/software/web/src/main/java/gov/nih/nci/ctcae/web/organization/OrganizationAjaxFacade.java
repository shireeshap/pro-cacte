package gov.nih.nci.ctcae.web.organization;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import gov.nih.nci.ctcae.core.repository.secured.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.web.tools.ObjectTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.context.SecurityContextHolder;

import java.util.*;

//
/**
 * The Class OrganizationAjaxFacade.
 *
 * @author Vinay Kumar
 * @since Oct 17, 2008
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class OrganizationAjaxFacade {

    /**
     * The organization repository.
     */
    private OrganizationRepository organizationRepository;
    private StudyOrganizationRepository studyOrganizationRepository;
    private GenericRepository genericRepository;
    private UserRepository userRepository;
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
        organizationQuery.setMaximumResults(25);
        List<Organization> organizations = (List<Organization>) organizationRepository.find(organizationQuery);
        return ObjectTools.reduceAll(organizations, "id", "name", "nciInstituteCode");

    }

    public List<Organization> matchOrganizationForStudySites(final String text) {
        logger.info("in match organization method. Search string :" + text);
        OrganizationQuery organizationQuery = new OrganizationQuery(false);
        organizationQuery.filterByOrganizationNameOrNciInstituteCode(text);
        organizationQuery.setMaximumResults(25);
        List<Organization> organizations = genericRepository.find(organizationQuery);
        return ObjectTools.reduceAll(organizations, "id", "name", "nciInstituteCode");

    }

    public List<Organization> matchOrganizationForStudySitesWithSecurity(final String text) {
        List<Organization> organizations = new ArrayList<Organization>();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ClinicalStaff clinicalStaff = userRepository.findClinicalStaffForUser(user);
        if (clinicalStaff != null) {
            Set<Organization> orgSet = new HashSet<Organization>();
            for (OrganizationClinicalStaff organizationClinicalStaff : clinicalStaff.getOrganizationClinicalStaffs()) {
                Organization organization = organizationClinicalStaff.getOrganization();
                if ("%".equals(text) || organization.getDisplayName().indexOf(text) > 1) {
                    orgSet.add(organizationClinicalStaff.getOrganization());
                }
                for (StudyOrganizationClinicalStaff socs : organizationClinicalStaff.getStudyOrganizationClinicalStaff()) {
                    if ((socs.getRole().equals(Role.LEAD_CRA) || socs.getRole().equals(Role.PI)) &&
                            socs.getRoleStatus().equals(RoleStatus.ACTIVE) && socs.getStatusDate().before(new Date())) {
                        for (StudyOrganization studyOrganization : socs.getStudyOrganization().getStudy().getStudyOrganizations()) {
                            organization = studyOrganization.getOrganization();
                            if ("%".equals(text) || organization.getDisplayName().indexOf(text) > 1) {
                                orgSet.add(studyOrganization.getOrganization());
                            }
                        }
                    }
                }
            }
            organizations = new ArrayList(orgSet);
            return ObjectTools.reduceAll(organizations, "id", "name", "nciInstituteCode");
        } else {
            return matchOrganizationForStudySites(text);
        }


    }

    public List<StudyOrganization> matchOrganizationByStudyId(final String text, Integer studyId) {
        List<StudyOrganization> organizations = studyOrganizationRepository.findByStudyId(text, studyId);
        return ObjectTools.reduceAll(organizations, "id", "displayName");

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

    @Required
    public void setStudyOrganizationRepository(final StudyOrganizationRepository studyOrganizationRepository) {
        this.studyOrganizationRepository = studyOrganizationRepository;
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
