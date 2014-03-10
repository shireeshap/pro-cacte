package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;

/**
 * @author Mehul Gulati
 *         Date: Nov 11, 2008
 */

public class ClinicalStaffControllerUtilsTest extends AbstractWebIntegrationTestCase {
    private CreateClinicalStaffController createClinicalStaffController;
    private WebControllerValidator webControllerValidator;
    private final static String SYSTEM_ADMIN = "system_admin";

    @Override
    protected void onSetUp() throws Exception {
    	super.onSetUp();
    	createClinicalStaffController = new CreateClinicalStaffController();
    	webControllerValidator = registerMockFor(WebControllerValidator.class);
    	createClinicalStaffController.setWebControllerValidator(webControllerValidator);
    }

    public void testNoCommandInCreateForm() {
        assertNull("no command present in session", ClinicalStaffControllerUtils.getClinicalStaffCommand(request));
    }

    public void testGetClinicalStaffCommand() throws Exception {
        login(SYSTEM_ADMIN);
    	request.setMethod("GET");
        
        createClinicalStaffController.handleRequest(request, response);
        Object command = ClinicalStaffControllerUtils.getClinicalStaffCommand(request);
        assertNotNull("command must be present in session", command);
    }
}
