package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.repository.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.web.tools.ObjectTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.ArrayList;

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
    private FinderRepository finderRepository;

    public List<OrganizationClinicalStaff> matchOrganizationClinicalStaffByStudyOrganizationId(final String text, Integer studyOrganizationId) {

        logger.info(String.format("in match matchOrganizationClinicalStaffByOrganizationId method. Search string :%s and studyOrganizationId=%s", text, studyOrganizationId));
        List<OrganizationClinicalStaff> organizationClinicalStaffs = clinicalStaffRepository.findByStudyOrganizationId(text, studyOrganizationId);

        return ObjectTools.reduceAll(organizationClinicalStaffs, "id", "displayName");

    }

    public List<StudyOrganizationClinicalStaff> matchStudyOrganizationClinicalStaffByStudyOrganizationIdAndRole(final String text, Integer studyOrganizationId, final String roles) {

        List<Role> rolesList = new ArrayList<Role>();
        StringTokenizer st = new StringTokenizer(roles,"|");
        while(st.hasMoreTokens()){
            String roleCode = st.nextToken();
            rolesList.add(Role.getByCode(roleCode));
        }
        logger.info(String.format("in match matchStudyOrganizationClinicalStaffByStudyOrganizationIdAndRole method. Search string :%s and studyOrganizationId=%s and role=%s", text, studyOrganizationId, roles));
        List<StudyOrganizationClinicalStaff> studyOrganizationClinicalStaffs = clinicalStaffRepository.findByStudyOrganizationIdAndRole(text, studyOrganizationId, rolesList);
        return ObjectTools.reduceAll(studyOrganizationClinicalStaffs, "id", "displayName", "role");

    }

    /**
     * Search clinical staff.
     *
     * @param parameterMap  the parameter map
     * @param firstName     the first name
     * @param lastName      the last name
     * @param nciIdentifier the nci identifier
     * @param request       the request
     * @return the string
     */
    public String searchClinicalStaff(Map parameterMap, String firstName, String lastName, String nciIdentifier, HttpServletRequest request) {

        List<ClinicalStaff> clinicalStaffs = getObjects(firstName, lastName, nciIdentifier);
        ClinicalStaffTableModel clinicalStaffTableModel = new ClinicalStaffTableModel();
        String table = clinicalStaffTableModel.buildClinicalStaffTable(parameterMap, clinicalStaffs, request);
        return table;


    }

    /**
     * Gets the objects.
     *
     * @param firstName     the first name
     * @param lastName      the last name
     * @param nciIdentifier the nci identifier
     * @return the objects
     */
    private List<ClinicalStaff> getObjects(String firstName, String lastName, String nciIdentifier) {
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();

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

}