package gov.nih.nci.ctcae.web.setup;

import gov.nih.nci.ctcae.core.SetupStatus;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;
import gov.nih.nci.ctcae.web.clinicalStaff.ClinicalStaffCommand;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;

/**
 * @author Vinay Kumar
 * @since Mar 18, 2009
 */
public class SetupTest extends AbstractWebIntegrationTestCase {

    SetupStatus setupStatus;


    public void testCommand() {
        ClinicalStaffCommand command = new ClinicalStaffCommand();

        assertNotNull(command.getClinicalStaff().getUser());
        assertEquals(Role.ADMIN, command.getClinicalStaff().getUser().getUserRoles().get(0).getRole());
    }

    public void testController() throws Exception {
        SetupController controller = new SetupController();
        controller.setWebControllerValidator(new WebControllerValidatorImpl());
        controller.setSetupStatus(setupStatus);
        try {
            controller.handleRequest(request, response);
            fail("expecting exception");
        } catch (Exception e) {
        }

    }


    public void setSetupStatus(SetupStatus setupStatus) {
        this.setupStatus = setupStatus;
    }
}