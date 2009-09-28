package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.StaticTabConfigurer;
import gov.nih.nci.cabig.ctms.web.tabs.TabConfigurer;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.core.validation.annotation.NotEmptyValidator;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueTitleForCrfValidator;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @since Dec 8, 2008
 */
public class EditFormControllerTest extends WebTestCase {
    private EditFormController controller;
    private WebControllerValidator validator;
    private CRFRepository crfRepository;
    private ProCtcTermRepository proCtcTermRepository;
    private CRF crf;
    private TabConfigurer tabConfigurer;
    private UniqueTitleForCrfValidator uniqueTitleForCrfValidator;
    private NotEmptyValidator notEmptyValidator;
    private Study study;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new EditFormController();
        notEmptyValidator = registerMockFor(NotEmptyValidator.class);
        uniqueTitleForCrfValidator = registerMockFor(UniqueTitleForCrfValidator.class);

        proCtcTermRepository = registerMockFor(ProCtcTermRepository.class);
        crfRepository = registerMockFor(CRFRepository.class);
        tabConfigurer = new StaticTabConfigurer(proCtcTermRepository, notEmptyValidator, uniqueTitleForCrfValidator);
        validator = new WebControllerValidatorImpl();
        controller.setCrfRepository(crfRepository);
        controller.setWebControllerValidator(validator);
        controller.setTabConfigurer(tabConfigurer);
        controller.setPrivilegeAuthorizationCheck(privilegeAuthorizationCheck);
        crf = new CRF();
        crf.setTitle("title");
        crf.setId(1);
        study = Fixture.createStudy("short", "long", "assignedId");
        study.setId(1);
        crf.setStudy(study);
    }


    public void testShouldOnlyEditDraftForm() throws Exception {
        crf.setStatus(CrfStatus.DRAFT);
        request.setMethod("GET");
        request.addParameter("crfId", "1");
        expect(crfRepository.findById(Integer.valueOf(1))).andReturn(crf);
        expect(proCtcTermRepository.find(isA(ProCtcTermQuery.class))).andReturn(new ArrayList<ProCtcTerm>());
        replayMocks();
        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();
        Map model = modelAndView.getModel();
        Object command = model.get("command");
        assertNotNull("must find command object", command);

    }


    public void testProcessFinish() throws Exception {

//        request.setMethod("GET");
//        request.addParameter("crfId", "1");
//
//        expect(crfRepository.findById(Integer.valueOf(1))).andReturn(crf);
//        expect(proCtcTermRepository.find(isA(ProCtcTermQuery.class))).andReturn(new ArrayList<ProCtcTerm>());
//
//        replayMocks();
//        ModelAndView modelAndView1 = controller.handleRequest(request, response);
//        verifyMocks();
//        CreateFormCommand command = (CreateFormCommand) modelAndView1.getModel().get("command");
//        assertNotNull("must find command object", command);
//        assertNotNull("must find command object", command.getCrf());
//
//        resetMocks();
//        request.setMethod("POST");
//
//        request.addParameter("_finish", "_finish");
//        expect(crfRepository.save(crf)).andReturn(crf);
//        expect(notEmptyValidator.validate("title")).andReturn(true);
//        expect(uniqueTitleForCrfValidator.validate(crf, crf.getTitle())).andReturn(true);
//        expect(privilegeAuthorizationCheck.authorize(isA(String.class))).andReturn(true).anyTimes();
//        expect(privilegeAuthorizationCheck.authorize(isA(ConfigAttributeDefinition.class))).andReturn(true).anyTimes();
//
//        replayMocks();
//        ModelAndView modelAndView = controller.handleRequest(request, response);
//
//        verifyMocks();
//        Map model = modelAndView.getModel();
//        assertNull("must not find command object", model.get("command"));


    }
}