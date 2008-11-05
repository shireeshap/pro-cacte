package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;
import gov.nih.nci.ctcae.core.repository.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.Fixture;

import java.util.Map;

/**
 * @author Mehul Gulati
 * Date: Nov 5, 2008
 */
public class ClinicalStaffAjaxFacadeIntegrationTest extends AbstractWebIntegrationTestCase {

    private ClinicalStaffAjaxFacade clinicalStaffAjaxFacade;
    protected Map parameterMap;
    private ClinicalStaffRepository clinicalStaffRepository;
    private ClinicalStaff clinicalStaff;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();

        clinicalStaff = Fixture.createClinicalStaff("Mehul", "Gulati", "1234");
        clinicalStaff = clinicalStaffRepository.save(clinicalStaff);
    }

    public void testSearchClinicalStaffByFirstName() {

        String table = clinicalStaffAjaxFacade.searchClinicalStaff(parameterMap,"Meh","G","", request);
        assertNotNull(table);
        assertTrue("must find atleast clinical staff matching with first name", table.contains(clinicalStaff.getFirstName()));
        assertTrue("must find atleast clinical staff matching with first name", table.contains(clinicalStaff.getLastName()));


        table = clinicalStaffAjaxFacade.searchClinicalStaff(parameterMap,"ehum","lat","", request);
        assertNotNull(table);
        assertFalse("must find atleast clinical staff matching with first name", table.contains(clinicalStaff.getFirstName()));


    }


    public void setClinicalStaffAjaxFacade(ClinicalStaffAjaxFacade clinicalStaffAjaxFacade) {
        this.clinicalStaffAjaxFacade = clinicalStaffAjaxFacade;
    }

    public void setClinicalStaffRepository(ClinicalStaffRepository clinicalStaffRepository) {
        this.clinicalStaffRepository = clinicalStaffRepository;
    }
}

