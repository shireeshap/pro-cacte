package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.web.WebTestCase;

/**
 * @author Mehul Gulati
 * Date: Nov 24, 2008
 */
public class SearchClinicalStaffControllerTest extends WebTestCase {

    private SearchClinicalStaffController controller;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new SearchClinicalStaffController();
    }

    public void testSupportedMethod() {
        assertEqualArrays("only get is supported", new String[]{"GET"}, controller.getSupportedMethods());
   
    }
}
