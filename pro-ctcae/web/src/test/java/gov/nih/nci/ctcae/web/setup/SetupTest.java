package gov.nih.nci.ctcae.web.setup;

import gov.nih.nci.ctcae.core.SetupStatus;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import static org.easymock.EasyMock.expect;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.FilterChain;

import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 * @crated Mar 18, 2009
 */
public class SetupTest extends AbstractWebIntegrationTestCase {

    SetupStatus setupStatus;


    public void testCommand() {
        SetupCommand command = new SetupCommand();

        assertNotNull(command.getUser());
        assertEquals(Role.ADMIN, command.getUser().getUserRoles().get(0).getRole());
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