package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.StaticTabConfigurer;
import gov.nih.nci.cabig.ctms.web.tabs.TabConfigurer;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import gov.nih.nci.ctcae.web.WebTestCase;

/**
 * @author Vinay Kumar
 * @crated Oct 24, 2008
 */
public class ControllersUtilsTest extends WebTestCase {

    private CreateFormController createFormController;
    private ProCtcQuestionRepository proCtcQuestionRepository;

    private TabConfigurer tabConfigurer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createFormController = new CreateFormController();
        // finderRepository = registerMockFor(FinderRepository.class);
        proCtcQuestionRepository = registerMockFor(ProCtcQuestionRepository.class);
        tabConfigurer = new StaticTabConfigurer(proCtcQuestionRepository);
    }

    public void testNoCommandInCreateForm() {


        assertNull("no command should present in session", ControllersUtils.getFormCommand(request, createFormController));


    }

    public void testCommandInGetRequestOfCreateForm() throws Exception {
        createFormController.handleRequest(request, response);
        Object command = ControllersUtils.getFormCommand(request, createFormController);
        assertNotNull("command must present in session", command);
        assertTrue(command instanceof CreateFormCommand);

    }
}
