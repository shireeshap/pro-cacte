package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.repository.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author Mehul Gulati
 * Date: Oct 22, 2008
 */
public class ClinicalStaffAjaxFacade {
    private ClinicalStaffRepository clinicalStaffRepository;

   public String searchClinicalStaff(Map parameterMap, String firstName, String lastName, String nciIdentifier, HttpServletRequest request) {

        List<ClinicalStaff> clinicalStaffs = getObjects(firstName, lastName, nciIdentifier);
        gov.nih.nci.ctcae.web.clinicalStaff.ClinicalStaffTableModel clinicalStaffTableModel = new gov.nih.nci.ctcae.web.clinicalStaff.ClinicalStaffTableModel();
        String table = clinicalStaffTableModel.buildClinicalStaffTable(parameterMap, clinicalStaffs, request);
        return table;


    }

    private List<ClinicalStaff> getObjects(String firstName, String lastName, String nciIdentifier) {
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();

        if (firstName != null && !"".equals(firstName)){
            clinicalStaffQuery.filterByClinicalStaffFirstName(firstName);
             }
        if (lastName != null && !"".equals(lastName)){
            clinicalStaffQuery.filterByClinicalStaffLastName(lastName);
             }
        if (nciIdentifier != null && !"".equals(nciIdentifier)){
            clinicalStaffQuery.filterByNciIdentifier(nciIdentifier);
        }
        List<ClinicalStaff> clinicalStaffs = (List<ClinicalStaff>) clinicalStaffRepository.find(clinicalStaffQuery);
        return clinicalStaffs;
    }

    @Required
    public void setClinicalStaffRepository(ClinicalStaffRepository clinicalStaffRepository) {
        this.clinicalStaffRepository = clinicalStaffRepository;
    }

}