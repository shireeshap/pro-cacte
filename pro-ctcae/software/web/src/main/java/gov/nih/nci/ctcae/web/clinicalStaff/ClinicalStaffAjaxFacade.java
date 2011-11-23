package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.secured.OrganizationClinicalStaffRepository;
import gov.nih.nci.ctcae.core.utils.ranking.RankBasedSorterUtils;
import gov.nih.nci.ctcae.core.utils.ranking.Serializer;
import gov.nih.nci.ctcae.web.tools.ObjectTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

//
/**
 * The Class ClinicalStaffAjaxFacade.
 *
 * @author Mehul Gulati
 *         Date: Oct 22, 2008
 */
public class ClinicalStaffAjaxFacade {

    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * The clinical staff repository.
     */
    private ClinicalStaffRepository clinicalStaffRepository;
    private OrganizationClinicalStaffRepository organizationClinicalStaffRepository;

    public List<OrganizationClinicalStaff> matchOrganizationClinicalStaffByStudyOrganizationId(final String text, Integer studyOrganizationId) {

        logger.info(String.format("in match matchOrganizationClinicalStaffByOrganizationId method. Search string :%s and studyOrganizationId=%s", text, studyOrganizationId));
        List<OrganizationClinicalStaff> organizationClinicalStaffs = organizationClinicalStaffRepository.findByStudyOrganizationId(text, studyOrganizationId);
         organizationClinicalStaffs = RankBasedSorterUtils.sort(organizationClinicalStaffs, text, new Serializer<OrganizationClinicalStaff>() {
             public String serialize(OrganizationClinicalStaff object) {
                 return object.getDisplayName();
             }
         });
        return ObjectTools.reduceAll(organizationClinicalStaffs, "id", "displayName");

    }

    public List<StudyOrganizationClinicalStaff> matchStudyOrganizationClinicalStaffByStudyOrganizationIdAndRole(final String text, Integer studyOrganizationId, final String roles) {

        List<Role> rolesList = new ArrayList<Role>();
        StringTokenizer st = new StringTokenizer(roles, "|");
        while (st.hasMoreTokens()) {
            String roleCode = st.nextToken();
            rolesList.add(Role.getByCode(roleCode));
        }
        logger.info(String.format("in match matchStudyOrganizationClinicalStaffByStudyOrganizationIdAndRole method. Search string :%s and studyOrganizationId=%s and role=%s", text, studyOrganizationId, roles));
        List<StudyOrganizationClinicalStaff> studyOrganizationClinicalStaffs = clinicalStaffRepository.findByStudyOrganizationIdAndRole(text, studyOrganizationId, rolesList);
        studyOrganizationClinicalStaffs = RankBasedSorterUtils.sort(studyOrganizationClinicalStaffs, text, new Serializer<StudyOrganizationClinicalStaff>() {
             public String serialize(StudyOrganizationClinicalStaff object) {
                 return object.getDisplayName();
             }
         });
        return ObjectTools.reduceAll(studyOrganizationClinicalStaffs, "id", "displayName", "role");

    }

    /**
     * Search clinical staff.
     *
     * @param firstName     the first name
     * @param lastName      the last name
     * @param nciIdentifier the nci identifier
     * @return the string
     */
    public List<ClinicalStaff> searchClinicalStaff(String firstName, String lastName, String nciIdentifier) {
        List<ClinicalStaff> clinicalStaffs = getObjects(firstName, lastName, nciIdentifier, true);
        return clinicalStaffs;
    }

    /**
     * Gets the objects.
     *
     * @param firstName     the first name
     * @param lastName      the last name
     * @param nciIdentifier the nci identifier
     * @return the objects
     */
    private List<ClinicalStaff> getObjects(String firstName, String lastName, String nciIdentifier, boolean showInactive) {
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery(showInactive);

        if (firstName != null && !"".equals(firstName)) {
            clinicalStaffQuery.filterByClinicalStaffFirstName(firstName);
        }
        if (lastName != null && !"".equals(lastName)) {
            clinicalStaffQuery.filterByClinicalStaffLastName(lastName);
        }
        if (nciIdentifier != null && !"".equals(nciIdentifier)) {
            clinicalStaffQuery.filterByNciIdentifier(nciIdentifier);
        }
        List<ClinicalStaff> clinicalStaffs = (List<ClinicalStaff>) clinicalStaffRepository.find(clinicalStaffQuery);
        return clinicalStaffs;
    }

    /**
     * Sets the clinical staff repository.
     *
     * @param clinicalStaffRepository the new clinical staff repository
     */
    @Required
    public void setClinicalStaffRepository(ClinicalStaffRepository clinicalStaffRepository) {
        this.clinicalStaffRepository = clinicalStaffRepository;
    }

    @Required
    public void setOrganizationClinicalStaffRepository(OrganizationClinicalStaffRepository organizationClinicalStaffRepository) {
        this.organizationClinicalStaffRepository = organizationClinicalStaffRepository;
    }
}