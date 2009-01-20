package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import org.easymock.EasyMock;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Harsh Agarwal
 * @crated Nov 25, 2008
 */
public class SubmitFormControllerTest extends WebTestCase {
    private SubmitFormController controller;
    private WebControllerValidator validator;
    private FinderRepository finderRepository;
    private GenericRepository genericRepository;
    private SubmitFormCommand command;

    private StudyParticipantCrfSchedule studyParticipantCrfSchedule;
    private CRF crf;
    private Study study;
    private ProCtcQuestion proCtcQuestion1, proCtcQuestion2, proCtcQuestion3, proCtcQuestion4, proCtcQuestion5, proCtcQuestion6, proCtcQuestion7, proCtcQuestion8;
    private StudyParticipantAssignment studyParticipantAssignment;
    private StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion1, studyParticipantCrfAddedQuestion2, studyParticipantCrfAddedQuestion3;
    List<ProCtcQuestion> questions = new ArrayList<ProCtcQuestion>();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new SubmitFormController();
        finderRepository = registerMockFor(FinderRepository.class);
        genericRepository = registerMockFor(GenericRepository.class);
        validator = new WebControllerValidatorImpl();

        controller.setFinderRepository(finderRepository);
        controller.setGenericRepository(genericRepository);
        controller.setWebControllerValidator(validator);

        studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
        studyParticipantCrfSchedule.setId(1);


        ProCtcTerm proCtcTerm1 = new ProCtcTerm();
        proCtcTerm1.setTerm("Fatigue");

        ProCtcTerm proCtcTerm2 = new ProCtcTerm();
        proCtcTerm2.setTerm("Pain");

        ProCtcTerm proCtcTerm3 = new ProCtcTerm();
        proCtcTerm3.setTerm("Cough");

        crf = Fixture.createCrf();
        crf.setId(1);
        CrfPageItem item1 = crf.addCrfPage(proCtcQuestion1).getCrfPageItems().get(0);
        item1.setId(1);
        CrfPageItem item2 = crf.addCrfPage(proCtcQuestion2).getCrfPageItems().get(0);
        item2.setId(2);
        item2.setResponseRequired(true);
        CrfPageItem item3 = crf.addCrfPage(proCtcQuestion3).getCrfPageItems().get(0);
        item3.setId(3);
        CrfPageItem item4 = crf.addCrfPage(proCtcQuestion4).getCrfPageItems().get(0);
        item4.setId(4);


        CrfPageItemDisplayRule crfPageItemDisplayRule = new CrfPageItemDisplayRule();
        ProCtcValidValue proCtcValidValue1 = new ProCtcValidValue();
        proCtcValidValue1.setId(-10);
        crfPageItemDisplayRule.setProCtcValidValue(proCtcValidValue1);
        item1.addCrfPageItemDisplayRules(crfPageItemDisplayRule);


        proCtcQuestion1 = new ProCtcQuestion();
        proCtcQuestion1.setProCtcTerm(proCtcTerm1);
        proCtcQuestion1.setProCtcQuestionType(ProCtcQuestionType.SEVERITY);

        proCtcQuestion2 = new ProCtcQuestion();
        proCtcQuestion2.setProCtcTerm(proCtcTerm1);
        proCtcQuestion2.setProCtcQuestionType(ProCtcQuestionType.INTERFERENCE);

        proCtcQuestion3 = new ProCtcQuestion();
        proCtcQuestion3.setProCtcTerm(proCtcTerm2);
        proCtcQuestion3.setProCtcQuestionType(ProCtcQuestionType.SEVERITY);

        proCtcQuestion4 = new ProCtcQuestion();
        proCtcQuestion4.setProCtcTerm(proCtcTerm2);
        proCtcQuestion4.setProCtcQuestionType(ProCtcQuestionType.INTERFERENCE);

        StudyParticipantCrfItem studyParticipantCrfItem1 = new StudyParticipantCrfItem();
        studyParticipantCrfItem1.setCrfPageItem(item1);
        ProCtcValidValue proCtcValidValue2 = new ProCtcValidValue();
        proCtcValidValue2.setValue("value0");
        studyParticipantCrfItem1.setProCtcValidValue(proCtcValidValue2);

        StudyParticipantCrfItem studyParticipantCrfItem2 = new StudyParticipantCrfItem();
        studyParticipantCrfItem2.setCrfPageItem(item2);

        StudyParticipantCrfItem studyParticipantCrfItem3 = new StudyParticipantCrfItem();
        studyParticipantCrfItem3.setCrfPageItem(item3);

        StudyParticipantCrfItem studyParticipantCrfItem4 = new StudyParticipantCrfItem();
        studyParticipantCrfItem4.setCrfPageItem(item4);


        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem1);
        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem2);
        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem3);
        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem4);

        proCtcQuestion5 = new ProCtcQuestion();
        proCtcQuestion6 = new ProCtcQuestion();
        proCtcQuestion7 = new ProCtcQuestion();
        proCtcQuestion8 = new ProCtcQuestion();

        proCtcQuestion1.setId(1);
        proCtcQuestion2.setId(2);
        proCtcQuestion3.setId(3);
        proCtcQuestion4.setId(4);

        proCtcQuestion5.setId(5);
        proCtcQuestion5.setProCtcTerm(proCtcTerm3);
        proCtcQuestion6.setId(6);
        proCtcQuestion6.setProCtcTerm(proCtcTerm3);
        proCtcQuestion7.setId(7);
        proCtcQuestion7.setProCtcTerm(proCtcTerm3);
        proCtcQuestion8.setId(8);
        proCtcQuestion8.setProCtcTerm(proCtcTerm3);

        questions.add(proCtcQuestion1);
        questions.add(proCtcQuestion2);
        questions.add(proCtcQuestion3);
        questions.add(proCtcQuestion4);
        questions.add(proCtcQuestion5);
        questions.add(proCtcQuestion6);
        questions.add(proCtcQuestion7);
        questions.add(proCtcQuestion8);


        study = Fixture.createStudyWithStudySite("short", "long", "assigned", Fixture.createOrganization("test", "test"));

        studyParticipantAssignment = new StudyParticipantAssignment();
        studyParticipantAssignment.setStudySite(study.getStudySites().get(0));

        crf.setStudy(study);
        StudyParticipantCrf studyParticipantCrf = new StudyParticipantCrf();

        studyParticipantCrfAddedQuestion1 = new StudyParticipantCrfAddedQuestion();
        studyParticipantCrfAddedQuestion1.setId(1);
        studyParticipantCrfAddedQuestion1.setProCtcQuestion(proCtcQuestion1);

        studyParticipantCrfAddedQuestion2 = new StudyParticipantCrfAddedQuestion();
        studyParticipantCrfAddedQuestion2.setId(2);
        studyParticipantCrfAddedQuestion2.setProCtcQuestion(proCtcQuestion5);

        studyParticipantCrfAddedQuestion3 = new StudyParticipantCrfAddedQuestion();
        studyParticipantCrfAddedQuestion3.setId(3);
        studyParticipantCrfAddedQuestion3.setProCtcQuestion(proCtcQuestion6);

        studyParticipantCrf.addStudyParticipantCrfAddedQuestion(studyParticipantCrfAddedQuestion1);
        studyParticipantCrf.addStudyParticipantCrfAddedQuestion(studyParticipantCrfAddedQuestion2);

        studyParticipantCrf.setCrf(crf);
        studyParticipantCrfSchedule.setStudyParticipantCrf(studyParticipantCrf);
        studyParticipantAssignment.addStudyParticipantCrf(studyParticipantCrf);

    }

    public void testConstructor() {
        assertEquals("form/submitForm", controller.getFormView());
        assertEquals("form/confirmFormSubmission", controller.getSuccessView());
        assertEquals("form/reviewFormSubmission", controller.getReviewView());
        assertEquals(SubmitFormCommand.class, controller.getCommandClass());
    }

    public void testGetRequest() throws Exception {
        request.setMethod("GET");
        request.addParameter("id", "1");
        EasyMock.expect(finderRepository.findById(StudyParticipantCrfSchedule.class, Integer.parseInt("1"))).andReturn(studyParticipantCrfSchedule);
        EasyMock.expectLastCall().anyTimes();
        replayMocks();
        ModelAndView modelAndView = controller.handleRequest(request, response);
        Map model = modelAndView.getModel();
        Object command = model.get("command");
        assertNotNull("must find command object", command);
        assertTrue(command instanceof SubmitFormCommand);
        assertEquals(controller.getFormView(), modelAndView.getViewName());

        request.getSession().setAttribute("review", "y");
        modelAndView = controller.handleRequest(request, response);
        model = modelAndView.getModel();
        command = model.get("command");
        assertNotNull("must find command object", command);
        assertTrue(command instanceof SubmitFormCommand);
        assertEquals("form/participantAddedQuestion", modelAndView.getViewName());
        assertEquals("n", request.getSession().getAttribute("review"));

        verifyMocks();

    }

    public void testPostRequest() throws Exception {

        EasyMock.expect(finderRepository.findById(StudyParticipantCrfSchedule.class, Integer.parseInt("1"))).andReturn(studyParticipantCrfSchedule);
        EasyMock.expectLastCall().anyTimes();
        EasyMock.expect(genericRepository.save(studyParticipantCrfSchedule)).andReturn(studyParticipantCrfSchedule);
        EasyMock.expectLastCall().anyTimes();
        replayMocks();


        request.setMethod("GET");
        request.addParameter("id", "1");
        ModelAndView modelAndView = controller.handleRequest(request, response);
        SubmitFormCommand command = (SubmitFormCommand) modelAndView.getModel().get("command");

        request.setMethod("POST");

        command.setDirection("continue");
        modelAndView = controller.handleRequest(request, response);
        assertEquals(controller.getFormView(), modelAndView.getViewName());
        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
        assertEquals(2, command.getCurrentPageIndex());

        command.setDirection("continue");
        modelAndView = controller.handleRequest(request, response);
        BeanPropertyBindingResult r = (BeanPropertyBindingResult) modelAndView.getModel().get("org.springframework.validation.BindingResult.command");
        assertEquals(1, r.getAllErrors().size());
        assertEquals(controller.getFormView(), modelAndView.getViewName());
        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
        assertEquals(2, command.getCurrentPageIndex());

        command.setDirection("back");
        modelAndView = controller.handleRequest(request, response);
        assertEquals(controller.getFormView(), modelAndView.getViewName());
        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
        assertEquals(1, command.getCurrentPageIndex());

        command.setCurrentPageIndex(command.getTotalPages());
        command.setDirection("continue");
        modelAndView = controller.handleRequest(request, response);
        assertEquals(controller.getReviewView(), modelAndView.getViewName());
        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());


        command.setDirection("save");
        modelAndView = controller.handleRequest(request, response);
        assertEquals(controller.getSuccessView(), modelAndView.getViewName());
        assertEquals(CrfStatus.COMPLETED, command.getStudyParticipantCrfSchedule().getStatus());
        assertEquals("You have successfully submitted the form.", command.getFlashMessage());


        verifyMocks();

    }
}