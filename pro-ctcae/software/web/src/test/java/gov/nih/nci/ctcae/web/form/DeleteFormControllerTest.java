package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import java.util.ArrayList;

import com.semanticbits.rules.impl.RuleAuthoringServiceImpl;
import com.semanticbits.rules.impl.RulesEngineServiceImpl;
import com.semanticbits.rules.impl.RepositoryServiceImpl;
import com.semanticbits.rules.brxml.RuleSet;
import com.semanticbits.rules.brxml.Rule;

/**
 * @author Mehul Gulati
 *         Date: Dec 15, 2008
 */
public class DeleteFormControllerTest extends WebTestCase {
    private DeleteFormController controller;
    private WebControllerValidator validator;
    private CRFRepository crfRepository;
    private StudyRepository studyRepository;
    private CRF crf;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new DeleteFormController();
        crfRepository = registerMockFor(CRFRepository.class);
        studyRepository = registerMockFor(StudyRepository.class);
        validator = new WebControllerValidatorImpl();

        controller.setCrfRepository(crfRepository);
        controller.setStudyRepository(studyRepository);
        controller.setWebControllerValidator(validator);


        crf = new CRF();
        crf.setId(1);
        Study study = new Study();
        study.setId(1);
        crf.setStudy(study);
    }

    public void testGetRequest() throws Exception {
        request.setMethod("GET");
        request.addParameter("crfId", "1");
        expect(crfRepository.findById(1)).andReturn(crf);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();
        Map model = modelAndView.getModel();
        Object command = model.get("command");
        assertNotNull("must find command object", command);
        assertTrue(command instanceof CRF);
    }

    public void testPostRequest() throws Exception {
        RuleAuthoringServiceImpl ruleAuthoringService = registerMockFor(RuleAuthoringServiceImpl.class);
        RulesEngineServiceImpl rulesEngineService = registerMockFor(RulesEngineServiceImpl.class);
        RepositoryServiceImpl repositoryService = registerMockFor(RepositoryServiceImpl.class);

        RuleSet ruleSet = new RuleSet();
        Rule rule = new Rule();
        ArrayList<Rule> rules = new ArrayList<Rule>();
        rules.add(rule);
        ruleSet.setRule(rules);

        request.setMethod("POST");
        request.addParameter("crfId", "1");
        expect(crfRepository.findById(1)).andReturn(crf);
        expect(ruleAuthoringService.getRuleSet("gov.nih.nci.ctcae.rules.form.study_1.form_1", false)).andReturn(ruleSet);
        repositoryService.logout();
        rulesEngineService.deleteRuleSet(ruleSet.getName());
        repositoryService.logout();
        crfRepository.delete(isA(CRF.class));
        expect(studyRepository.findById(1)).andReturn(crf.getStudy());
        replayMocks();
        ModelAndView modelAndView = controller.handleRequest(request, response);

        verifyMocks();
        Map model = modelAndView.getModel();
        assertTrue("view must be instance of redirect view", modelAndView.getView() instanceof RedirectView);
        Object command = model.get("command");
        assertNull("must not find command object", command);


    }

}
