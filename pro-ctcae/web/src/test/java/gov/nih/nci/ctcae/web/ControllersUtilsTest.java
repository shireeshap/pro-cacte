package gov.nih.nci.ctcae.web;

import gov.nih.nci.cabig.ctms.web.tabs.StaticTabConfigurer;
import gov.nih.nci.cabig.ctms.web.tabs.TabConfigurer;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import gov.nih.nci.ctcae.web.form.CreateFormCommand;
import gov.nih.nci.ctcae.web.form.CreateFormController;
import gov.nih.nci.ctcae.web.study.CreateStudyController;
import gov.nih.nci.ctcae.web.study.StudyCommand;

/**
 * @author Vinay Kumar
 * @crated Oct 24, 2008
 */
public class ControllersUtilsTest extends WebTestCase {

    private CreateFormController createFormController;
    private CreateStudyController createStudyController;
    private ProCtcQuestionRepository proCtcQuestionRepository;

    private TabConfigurer tabConfigurer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createFormController = new CreateFormController();
        // finderRepository = registerMockFor(FinderRepository.class);
        proCtcQuestionRepository = registerMockFor(ProCtcQuestionRepository.class);
        tabConfigurer = new StaticTabConfigurer(proCtcQuestionRepository);

        createStudyController = new CreateStudyController();
        createStudyController.setTabConfigurer(tabConfigurer);
    }

    public void testNoCommandInCreateForm() {


        assertNull("no command should present in session", ControllersUtils.getFormCommand(request, createFormController));


    }

    public void testNoCommandInCreateStudy() {


        assertNull("no command should present in session", ControllersUtils.getFormCommand(request, createStudyController));


    }

    public void testCommandInGetRequestOfCreateForm() throws Exception {
        createFormController.handleRequest(request, response);
        Object command = ControllersUtils.getFormCommand(request, createFormController);
        assertNotNull("command must present in session", command);
        assertTrue(command instanceof CreateFormCommand);

    }

    public void testCommandInGetRequestOfCreateStudy() throws Exception {

        createStudyController.handleRequest(request, response);
        Object command = ControllersUtils.getFormCommand(request, createStudyController);
        assertNotNull("command must present in session", command);
        assertTrue(command instanceof StudyCommand);

    }
}
