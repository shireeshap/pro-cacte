package gov.nih.nci.ctcae.web;

import gov.nih.nci.cabig.ctms.web.tabs.StaticTabConfigurer;
import gov.nih.nci.cabig.ctms.web.tabs.TabConfigurer;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.web.form.BasicFormController;
import gov.nih.nci.ctcae.web.form.CreateFormCommand;
import gov.nih.nci.ctcae.web.form.EditFormController;
import gov.nih.nci.ctcae.web.study.CreateStudyController;
import gov.nih.nci.ctcae.web.study.StudyCommand;
import gov.nih.nci.ctcae.web.study.StudyController;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

/**
 * @author Vinay Kumar
 * @since Oct 24, 2008
 */
public class ControllersUtilsTest extends WebTestCase {

    private BasicFormController basicFormController;
    private EditFormController editFormController;
    private StudyController studyController;
    private CRFRepository crfRepository;
    private ProCtcTermRepository proCtcTermRepository;


    private TabConfigurer tabConfigurer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        basicFormController = new BasicFormController();
        crfRepository = registerMockFor(CRFRepository.class);
        proCtcTermRepository = registerMockFor(ProCtcTermRepository.class);
        crfRepository = registerMockFor(CRFRepository.class);

        tabConfigurer = new StaticTabConfigurer(proCtcQuestionRepository, proCtcTermRepository);

        studyController = new CreateStudyController();
        studyController.setTabConfigurer(tabConfigurer);
        studyController.setPrivilegeAuthorizationCheck(privilegeAuthorizationCheck);

        editFormController = new EditFormController();

        editFormController.setTabConfigurer(tabConfigurer);
        editFormController.setCrfRepository(crfRepository);
        editFormController.setPrivilegeAuthorizationCheck(privilegeAuthorizationCheck);
        editFormController.setStudyRepository(studyRepository);

        basicFormController.setTabConfigurer(tabConfigurer);
        basicFormController.setCrfRepository(crfRepository);
        basicFormController.setPrivilegeAuthorizationCheck(privilegeAuthorizationCheck);
        basicFormController.setStudyRepository(studyRepository);


    }


    public void testNoCommandInCreateForm() {
        replayMocks();
        assertNull("no command should present in session", ControllersUtils.getFormCommand(request));
        verifyMocks();

    }

    public void testNoCommandInCreateStudy() {
        replayMocks();
        assertNull("no command should present in session", ControllersUtils.getStudyCommand(request));
        verifyMocks();
    }

    public void testCommandInGetRequestOfCreateForm() throws Exception {
        replayMocks();
        basicFormController.handleRequest(request, response);
        verifyMocks();
        Object command = ControllersUtils.getFormCommand(request);
        assertNotNull("command must present in session", command);
        assertTrue(command instanceof CreateFormCommand);

    }

    public void testCommandInGetRequestOfEditForm() throws Exception {
        expect(proCtcTermRepository.find(isA(ProCtcTermQuery.class))).andReturn(proCtcTerms);
        replayMocks();
        editFormController.handleRequest(request, response);
        verifyMocks();
        Object command = ControllersUtils.getFormCommand(request);
        assertNotNull("command must present in session", command);
        assertTrue(command instanceof CreateFormCommand);

    }

    public void testCommandInGetRequestOfCreateStudy() throws Exception {
        replayMocks();
        studyController.handleRequest(request, response);
        verifyMocks();
        Object command = ControllersUtils.getStudyCommand(request);
        assertNotNull("command must present in session", command);
        assertTrue(command instanceof StudyCommand);
    }

    public void testRemoveParameterFromQueryString() {
        String queryString = "subview=subview&study=1&crf=1&symptom=2&filter=week&filterVal=1&studySite=&attributes=&group=cycle&att=Severity&grade=4&period=Week 1";
        queryString = ControllersUtils.removeParameterFromQueryString(queryString, "group");
        assertEquals("subview=subview&study=1&crf=1&symptom=2&filter=week&filterVal=1&studySite=&attributes=&att=Severity&grade=4&period=Week 1", queryString);


        queryString = "subview=subview&study=1&crf=1&symptom=2&filter=week&filterVal=1&studySite=&attributes=&group=cycle";
        queryString = ControllersUtils.removeParameterFromQueryString(queryString, "group");
        assertEquals("subview=subview&study=1&crf=1&symptom=2&filter=week&filterVal=1&studySite=&attributes=", queryString);

        queryString = null;
        queryString = ControllersUtils.removeParameterFromQueryString(queryString, "group");
        assertNull(queryString);
    }
}
