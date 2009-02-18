package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.repository.ClinicalStaffRepository;
import gov.nih.nci.ctcae.web.tools.ObjectTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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

    public List<ClinicalStaff> matchClinicalStaffByOrganizationId(final String text, Integer organizationId) {

        logger.info(String.format("in match matchClinicalStaffByOrganizationId method. Search string :%s and organizationId=%s", text, organizationId));
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByOrganization(organizationId);
        clinicalStaffQuery.filterByFirstNameOrLastNameOrNciIdentifier(text);
        List<ClinicalStaff> clinicalStaffs = (List<ClinicalStaff>) clinicalStaffRepository.find(clinicalStaffQuery);
        return ObjectTools.reduceAll(clinicalStaffs, "id", "lastName", "firstName");

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