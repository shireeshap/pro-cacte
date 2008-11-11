package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;

import java.util.Enumeration;

/**
 * @author Mehul Gulati
 *         Date: Nov 11, 2008
 */

public class ClinicalStaffControllerUtilsTest extends WebTestCase {

    private CreateClinicalStaffController createClinicalStaffController;
    private WebControllerValidator webControllerValidator;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        createClinicalStaffController = new CreateClinicalStaffController();
        webControllerValidator = registerMockFor(WebControllerValidator.class);
        createClinicalStaffController.setWebControllerValidator(webControllerValidator);
    }

    public void testNoCommandInCreateForm() {

        assertNull("no command present in session", ClinicalStaffControllerUtils.getClinicalStaffCommand(request));
    }

    public void testGetClinicalStaffCommand() throws Exception {

        request.setMethod("GET");
        createClinicalStaffController.handleRequest(request, response);
        Object command = ClinicalStaffControllerUtils.getClinicalStaffCommand(request);
    //    Enumeration e = request.getSession().getAttributeNames();
     //   while (e.hasMoreElements()) {
      //      System.out.println(e.nextElement());
      //  }
        assertNotNull("command must be present in session", command);

    }
}
