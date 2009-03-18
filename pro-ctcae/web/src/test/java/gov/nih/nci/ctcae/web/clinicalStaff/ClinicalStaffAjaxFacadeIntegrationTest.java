package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

import java.util.List;
import java.util.Map;

/**
 * @author Mehul Gulati
 *         Date: Nov 5, 2008
 */
public class ClinicalStaffAjaxFacadeIntegrationTest extends AbstractWebIntegrationTestCase {

    private ClinicalStaffAjaxFacade clinicalStaffAjaxFacade;
    protected Map parameterMap;


    public void testFind() {

        List<StudyOrganizationClinicalStaff> organizationClinicalStaffList = clinicalStaffAjaxFacade.matchStudyOrganizationClinicalStaffByStudyOrganizationIdAndRole
                ("%", defaultStudySite.getId(), "NURSE");

        assertFalse(organizationClinicalStaffList.isEmpty());
    }

    public void testSearchClinicalStaffByFirstName() {

        String table = clinicalStaffAjaxFacade.searchClinicalStaff(parameterMap, "Meh", "G", "", request);
        assertNotNull(table);
        assertTrue("must find atleast clinical staff matching with first name", table.contains(defaultClinicalStaff.getFirstName()));
        assertTrue("must find atleast clinical staff matching with first name", table.contains(defaultClinicalStaff.getLastName()));


        table = clinicalStaffAjaxFacade.searchClinicalStaff(parameterMap, "ehum", "lat", "", request);
        assertNotNull(table);
        assertFalse("must find atleast clinical staff matching with first name", table.contains(defaultClinicalStaff.getFirstName()));


    }


    public void setClinicalStaffAjaxFacade(ClinicalStaffAjaxFacade clinicalStaffAjaxFacade) {
        this.clinicalStaffAjaxFacade = clinicalStaffAjaxFacade;
    }

}

