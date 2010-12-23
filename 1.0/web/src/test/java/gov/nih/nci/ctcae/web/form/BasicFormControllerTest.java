package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.StaticTabConfigurer;
import gov.nih.nci.cabig.ctms.web.tabs.TabConfigurer;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.validation.annotation.NotEmptyValidator;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueTitleForCrfValidator;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

/**
 * @author Vinay Kumar
 * @since Jan, 20, 2009
 */
public class BasicFormControllerTest extends WebTestCase {
    private BasicFormController controller;
    private WebControllerValidator validator;
    private CRFRepository crfRepository;
    private ProCtcTermRepository proCtcTermRepository;
    private CRF crf;
    private TabConfigurer tabConfigurer;
    private UniqueTitleForCrfValidator uniqueTitleForCrfValidator;
    private NotEmptyValidator notEmptyValidator;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new BasicFormController();
        notEmptyValidator = registerMockFor(NotEmptyValidator.class);
        uniqueTitleForCrfValidator = registerMockFor(UniqueTitleForCrfValidator.class);
        proCtcTermRepository = registerMockFor(ProCtcTermRepository.class);
        crfRepository = registerMockFor(CRFRepository.class);
        tabConfigurer = new StaticTabConfigurer(proCtcTermRepository);
        validator = new WebControllerValidatorImpl();
        controller.setCrfRepository(crfRepository);
        controller.setWebControllerValidator(validator);
        controller.setTabConfigurer(tabConfigurer);
        controller.setStudyRepository(studyRepository);
        controller.setPrivilegeAuthorizationCheck(privilegeAuthorizationCheck);
        crf = new CRF();

        crf.setTitle("title");
    }

    public void testFormBackingObject() throws Exception {
        replayMocks();
        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();
        Map model = modelAndView.getModel();
        Object command = model.get("command");
        assertNotNull("must find command object", command);
        assertTrue(command instanceof CreateFormCommand);
        assertFalse("must be basic form creation", ((CreateFormCommand) command).getCrf().getAdvance());

    }


    public void testFirstPage() throws Exception {
        SelectStudyForFormTab selectStudyForFormTab = new SelectStudyForFormTab();
        replayMocks();
        ModelAndView modelAndView = controller.handleRequest(request, response);
        Map model = modelAndView.getModel();
        verifyMocks();
        assertEquals("first page should be study details", modelAndView.getViewName(), selectStudyForFormTab.getViewName());
        Object command = model.get("command");
        assertNotNull("must find command object", command);
        assertTrue(command instanceof CreateFormCommand);

    }

    public void testFirstPageShouldBeFormDetailsPage() throws Exception {
        FormDetailsTab formDetailsTab = new FormDetailsTab();
        request.setMethod("GET");
        request.addParameter("studyId", "1");
        expect(studyRepository.findById(Integer.valueOf(1))).andReturn(new Study());
        Collection<ProCtcTerm> proCtcTerms = new ArrayList();
        expect(proCtcTermRepository.find(isA(ProCtcTermQuery.class))).andReturn(proCtcTerms);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();
        Map model = modelAndView.getModel();
        assertEquals("first page should be review form", modelAndView.getViewName(), formDetailsTab.getViewName());
        Object command = model.get("command");
        assertNotNull("must find command object", command);
        assertTrue(command instanceof CreateFormCommand);

    }


}