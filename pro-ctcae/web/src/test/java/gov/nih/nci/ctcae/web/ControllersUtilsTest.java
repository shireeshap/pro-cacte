package gov.nih.nci.ctcae.web;

import gov.nih.nci.cabig.ctms.web.tabs.StaticTabConfigurer;
import gov.nih.nci.cabig.ctms.web.tabs.TabConfigurer;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.web.form.BasicFormController;
import gov.nih.nci.ctcae.web.form.CreateFormCommand;
import gov.nih.nci.ctcae.web.form.EditFormController;
import gov.nih.nci.ctcae.web.study.CreateStudyController;
import gov.nih.nci.ctcae.web.study.StudyCommand;
import static org.easymock.EasyMock.expect;

/**
 * @author Vinay Kumar
 * @crated Oct 24, 2008
 */
public class ControllersUtilsTest extends WebTestCase {

    private BasicFormController basicFormController;
    private EditFormController editFormController;
    private CreateStudyController createStudyController;
    private ProCtcQuestionRepository proCtcQuestionRepository;
    private FinderRepository finderRepository;
    private CRFRepository crfRepository;
    private ProCtcTermRepository proCtcTermRepository;

    private TabConfigurer tabConfigurer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        basicFormController = new BasicFormController();
        finderRepository = registerMockFor(FinderRepository.class);
        crfRepository = registerMockFor(CRFRepository.class);
        proCtcQuestionRepository = registerMockFor(ProCtcQuestionRepository.class);
        proCtcTermRepository = registerMockFor(ProCtcTermRepository.class);
        crfRepository = registerMockFor(CRFRepository.class);

        tabConfigurer = new StaticTabConfigurer(proCtcQuestionRepository, proCtcTermRepository);

        createStudyController = new CreateStudyController();
        createStudyController.setTabConfigurer(tabConfigurer);

        editFormController = new EditFormController();
        editFormController.setTabConfigurer(tabConfigurer);
        editFormController.setFinderRepository(finderRepository);
        editFormController.setCrfRepository(crfRepository);
    }

    public void testNoCommandInCreateForm() {


        assertNull("no command should present in session", ControllersUtils.getFormCommand(request, basicFormController));


    }

    public void testNoCommandInCreateStudy() {


        assertNull("no command should present in session", ControllersUtils.getFormCommand(request, createStudyController));


    }

    public void testCommandInGetRequestOfCreateForm() throws Exception {
        basicFormController.handleRequest(request, response);
        Object command = ControllersUtils.getFormCommand(request, basicFormController);
        assertNotNull("command must present in session", command);
        assertTrue(command instanceof CreateFormCommand);

    }

    public void testCommandInGetRequestOfEditForm() throws Exception {
        request.addParameter("crfId", "1");
        expect(finderRepository.findAndInitializeCrf(Integer.valueOf(1))).andReturn(new CRF());
        replayMocks();

        editFormController.handleRequest(request, response);
        verifyMocks();
        Object command = ControllersUtils.getFormCommand(request);
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
