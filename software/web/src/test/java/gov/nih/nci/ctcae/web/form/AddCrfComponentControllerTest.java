package gov.nih.nci.ctcae.web.form;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.WebTestCase;
import org.springframework.web.servlet.ModelAndView;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

/**
 * @author Vinay Kumar
 * @since Dec 18, 2008
 */
public class AddCrfComponentControllerTest extends WebTestCase {

    private AddCrfComponentController controller;

    private ProCtcTermRepository proCtcTermRepository;
    private ProCtcTerm proCtcTerm;
    private ProCtcTerm proCtcTerm2;
    private ProCtcTerm proCtcTerm3;
    private List<ProCtcTerm> proCtcTermList;
    private CreateFormCommand command;
    private ProCtcQuestion firstQuestion;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new AddCrfComponentController();
        proCtcTermRepository = registerMockFor(ProCtcTermRepository.class);
        controller.setProCtcTermRepository(proCtcTermRepository);
        proCtcTerm = new ProCtcTerm();
        proCtcTerm2 = new ProCtcTerm();
        proCtcTerm3 = new ProCtcTerm();
        
        proCtcTermList = new ArrayList<ProCtcTerm>();
        proCtcTermList.add(proCtcTerm);
        proCtcTerm.setProCtcTermVocab(new ProCtcTermVocab());
        proCtcTerm2.setProCtcTermVocab(new ProCtcTermVocab());
        proCtcTerm3.setProCtcTermVocab(new ProCtcTermVocab());
        proCtcTermList.add(proCtcTerm2);
        proCtcTermList.add(proCtcTerm3);
        
        proCtcTerm.setTermEnglish("a", SupportedLanguageEnum.ENGLISH);
        proCtcTerm2.setTermEnglish("b", SupportedLanguageEnum.ENGLISH);
        proCtcTerm3.setTermEnglish("c", SupportedLanguageEnum.ENGLISH);
        
        
        CtcCategory ctcCategory = new CtcCategory();
        ctcCategory.setName("PRO-CTCAE");
        
        CategoryTermSet categoryTermSet =  new CategoryTermSet();
        categoryTermSet.setCategory(ctcCategory);
        categoryTermSet.setCtcTerm(new CtcTerm());
        
        proCtcTerm.setCtcTerm(new CtcTerm());
        proCtcTerm.getCtcTerm().addCategoryTermSet(categoryTermSet);

        command = new CreateFormCommand();

        firstQuestion = new ProCtcQuestion();
        firstQuestion.setId(11);
        firstQuestion.setQuestionText("sample question1", SupportedLanguageEnum.ENGLISH);

        proCtcTerm.addProCtcQuestion(firstQuestion);
    }

    public void testAddProCtcTermForBasicMode() throws Exception {

        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        request.getSession().setAttribute(FormController.class.getName() + ".FORM." + "command", command);

        request.addParameter("proCtcTermId", new String[]{"1"});
        request.addParameter("componentType", new String[]{AddCrfComponentController.PRO_CTC_TERM_COMPONENT});
        expect(proCtcTermRepository.findById(1)).andReturn(proCtcTerm);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        verifyMocks();

        assertEquals("must return view for new crf page", "form/ajax/oneCrfPageSection", modelAndView.getViewName());
        assertNotNull("must return crf page", modelAndView.getModel().get("crfPage"));

        CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);

        CRF crf = createFormCommand.getCrf();
        assertFalse("must add crf page", crf.getCrfPagesSortedByPageNumber().isEmpty());
        assertEquals("must add only one page", 1, crf.getCrfPagesSortedByPageNumber().size());


    }

    public void testAddProCtcTermAgainForBasicMode() throws Exception {
        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        request.getSession().setAttribute(FormController.class.getName() + ".FORM." + "command", command);

        request.addParameter("proCtcTermId", new String[]{"1"});
        request.addParameter("componentType", new String[]{AddCrfComponentController.PRO_CTC_TERM_COMPONENT});
        expect(proCtcTermRepository.findById(1)).andReturn(proCtcTerm);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        verifyMocks();
        resetMocks();

        expect(proCtcTermRepository.findById(1)).andReturn(proCtcTerm);
        replayMocks();
        modelAndView = controller.handleRequestInternal(request, response);
        verifyMocks();

        assertEquals("must return view for new pro ctc term", "form/ajax/oneProCtcTermSection", modelAndView.getViewName());
        assertNotNull("must return crf page items", modelAndView.getModel().get("crfPageItems"));

    }

    public void testSupportedMethod() {
        assertEqualArrays("only get is supported", new String[]{"GET"}, controller.getSupportedMethods());
    }
    
    
    public void testAddCtcTerm() throws Exception {
        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        request.getSession().setAttribute(FormController.class.getName() + ".FORM." + "command", command);

        request.addParameter("componentType", new String[]{AddCrfComponentController.CTC_CATEGORY_COMPONENT});
        request.addParameter("ctcCategoryId", "2");
        request.addParameter("categoryName", "Not a core symptom");
       
        expect(proCtcTermRepository.find(isA(ProCtcTermQuery.class))).andReturn(proCtcTermList);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        verifyMocks();
        
        assertEquals("must return view for new crf page", "form/ajax/reloadFormBuilderDiv", modelAndView.getViewName());
        assertNotNull("must return added Crf Pages", modelAndView.getModel().get("crfPages"));
        assertEquals("must not return crfPageItems", 0, ((List<CrfPageItem>) modelAndView.getModel().get("crfPageItems")).size());

        CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);

        CRF crf = createFormCommand.getCrf();
        assertFalse("must add crf page", crf.getCrfPagesSortedByPageNumber().isEmpty());
        assertEquals("must add 3 pages", 3, crf.getCrfPagesSortedByPageNumber().size());
    }

}