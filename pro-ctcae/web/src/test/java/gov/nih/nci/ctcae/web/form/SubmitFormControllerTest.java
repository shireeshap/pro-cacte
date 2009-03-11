package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import org.easymock.EasyMock;
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
    private GenericRepository genericRepository;
    private SubmitFormCommand command;

    private StudyParticipantCrfSchedule studyParticipantCrfSchedule;
    private CRF crf;
    private Study study;
    private StudyParticipantAssignment studyParticipantAssignment;
    private StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion1, studyParticipantCrfAddedQuestion2, studyParticipantCrfAddedQuestion3;
    List<ProCtcQuestion> questions = new ArrayList<ProCtcQuestion>();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new SubmitFormController();
        genericRepository = registerMockFor(GenericRepository.class);
        validator = new WebControllerValidatorImpl();

        controller.setWebControllerValidator(validator);

        studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
        studyParticipantCrfSchedule.setId(1);


        crf = Fixture.createCrf();
        crf.setId(1);

        CRFPage crfPage = (CRFPage) crf.addProCtcTerm(proCtcTerm1);
        CrfPageItem item1 = crfPage.getCrfPageItems().get(0);
        item1.setId(1);
        CrfPageItem item2 = crfPage.getCrfPageItems().get(1);
        item2.setId(2);
        item2.setResponseRequired(true);

        crfPage = (CRFPage) crf.addProCtcTerm(proCtcTerm2);
        CrfPageItem item3 = crfPage.getCrfPageItems().get(0);
        item3.setId(3);
        CrfPageItem item4 = crfPage.getCrfPageItems().get(1);
        item4.setId(4);


        CrfPageItemDisplayRule crfPageItemDisplayRule = new CrfPageItemDisplayRule();
        ProCtcValidValue proCtcValidValue1 = new ProCtcValidValue();
        proCtcValidValue1.setId(-10);
        crfPageItemDisplayRule.setProCtcValidValue(proCtcValidValue1);
        item1.addCrfPageItemDisplayRules(crfPageItemDisplayRule);


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
        EasyMock.expectLastCall().anyTimes();
        replayMocks();
        ModelAndView modelAndView = controller.handleRequest(request, response);
        Map model = modelAndView.getModel();
        Object command = model.get("command");
        assertNotNull("must find command object", command);
        assertTrue(command instanceof SubmitFormCommand);
        assertEquals(controller.getFormView(), modelAndView.getViewName());

        request.getSession().setAttribute("gotopage", "" + 10);
        modelAndView = controller.handleRequest(request, response);
        model = modelAndView.getModel();
        command = model.get("command");
        assertNotNull("must find command object", command);
        assertTrue(command instanceof SubmitFormCommand);
        assertEquals(10, ((SubmitFormCommand) command).getCurrentPageIndex());
        assertEquals(null, request.getSession().getAttribute("gotopage"));

        verifyMocks();

    }

//    public void testMandatorQuestions() throws Exception {
//        EasyMock.expect(finderRepository.findById(StudyParticipantCrfSchedule.class, Integer.parseInt("1"))).andReturn(studyParticipantCrfSchedule);
//        EasyMock.expectLastCall().anyTimes();
//        EasyMock.expect(genericRepository.save(studyParticipantCrfSchedule)).andReturn(studyParticipantCrfSchedule);
//        EasyMock.expectLastCall().anyTimes();
//        replayMocks();
//
//        request.setMethod("GET");
//        request.addParameter("id", "1");
//        ModelAndView modelAndView = controller.handleRequest(request, response);
//        SubmitFormCommand command = (SubmitFormCommand) modelAndView.getModel().get("command");
//
//        command.setCurrentPageIndex(2);
//
//        //test mandatory questions
//        request.setMethod("POST");
//        command.setDirection("continue");
//        modelAndView = controller.handleRequest(request, response);
//        BeanPropertyBindingResult r = (BeanPropertyBindingResult) modelAndView.getModel().get("org.springframework.validation.BindingResult.command");
//        assertEquals(1, r.getAllErrors().size());
//        assertEquals(controller.getFormView(), modelAndView.getViewName());
//        assertEquals(2, command.getCurrentPageIndex());
//
//        command.getStudyParticipantCrfSchedule().getStudyParticipantCrfItems().get(1).setProCtcValidValue(new ProCtcValidValue());
//        command.setDirection("continue");
//        modelAndView = controller.handleRequest(request, response);
//        r = (BeanPropertyBindingResult) modelAndView.getModel().get("org.springframework.validation.BindingResult.command");
//        assertEquals(0, r.getAllErrors().size());
//        assertEquals(controller.getFormView(), modelAndView.getViewName());
//        assertEquals(3, command.getCurrentPageIndex());
//    }
//
//    public void testPostRequest() throws Exception {
//
//        EasyMock.expect(finderRepository.findById(StudyParticipantCrfSchedule.class, Integer.parseInt("1"))).andReturn(studyParticipantCrfSchedule);
//        EasyMock.expectLastCall().anyTimes();
//        EasyMock.expect(genericRepository.save(studyParticipantCrfSchedule)).andReturn(studyParticipantCrfSchedule);
//        EasyMock.expectLastCall().anyTimes();
//        replayMocks();
//
////        studyParticipantCrf.addStudyParticipantCrfAddedQuestion(studyParticipantCrfAddedQuestion1);
////        studyParticipantCrf.addStudyParticipantCrfAddedQuestion(studyParticipantCrfAddedQuestion2);
//
//        request.setMethod("GET");
//        request.addParameter("id", "1");
//        ModelAndView modelAndView = controller.handleRequest(request, response);
//        SubmitFormCommand command = (SubmitFormCommand) modelAndView.getModel().get("command");
//
//        request.setMethod("POST");
//        command.setDirection("continue");
//        modelAndView = controller.handleRequest(request, response);
//        assertEquals(controller.getFormView(), modelAndView.getViewName());
//        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
//        assertEquals(2, command.getCurrentPageIndex());
//        assertEquals(4, command.getTotalPages());
//
//
//        command.setCurrentPageIndex(command.getTotalPages());
//        command.setDirection("continue");
//        modelAndView = controller.handleRequest(request, response);
//        assertEquals(modelAndView.toString(), new ModelAndView(new RedirectView("addquestion")).toString());

//        request.setMethod("POST");
//        command.setDirection("continue");
//        modelAndView = controller.handleRequest(request, response);
//
//        request.setMethod("POST");
//        command.setDirection("continue");
//        modelAndView = controller.handleRequest(request, response);
//
//        assertEquals(controller.getFormView(), modelAndView.getViewName());
//        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
//        assertEquals(5, command.getCurrentPageIndex());
//        assertEquals(4, command.getTotalPages());

//        command.setDirection("continue");
//        modelAndView = controller.handleRequest(request, response);
//        BeanPropertyBindingResult r = (BeanPropertyBindingResult) modelAndView.getModel().get("org.springframework.validation.BindingResult.command");
//        assertEquals(1, r.getAllErrors().size());
//        assertEquals(controller.getFormView(), modelAndView.getViewName());
//        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
//        assertEquals(2, command.getCurrentPageIndex());
//
//        command.setDirection("back");
//        modelAndView = controller.handleRequest(request, response);
//        assertEquals(controller.getFormView(), modelAndView.getViewName());
//        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
//        assertEquals(1, command.getCurrentPageIndex());
//
//        command.setCurrentPageIndex(command.getTotalPages());
//        command.setDirection("continue");
//        modelAndView = controller.handleRequest(request, response);
//        assertEquals("form/addquestion", modelAndView.getViewName());
//        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
//
//        command.setDirection("continue");
//        modelAndView = controller.handleRequest(request, response);
//        assertEquals(controller.getReviewView(), modelAndView.getViewName());
//        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
//
//        command.setDirection("back");
//        modelAndView = controller.handleRequest(request, response);
//        assertEquals("form/addquestion", modelAndView.getViewName());
//        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
//
//        command.setDirection("back");
//        modelAndView = controller.handleRequest(request, response);
//        assertEquals("form/participantAddedQuestion", modelAndView.getViewName());
//        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
//
//        command.setDirection("back");
//        modelAndView = controller.handleRequest(request, response);
//        assertEquals("form/participantAddedQuestion", modelAndView.getViewName());
//        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
//
//        command.setDirection("back");
//        modelAndView = controller.handleRequest(request, response);
//        assertEquals(controller.getFormView(), modelAndView.getViewName());
//        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
//
//        command.setDirection("save");
//        modelAndView = controller.handleRequest(request, response);
//        assertEquals(controller.getSuccessView(), modelAndView.getViewName());
//        assertEquals(CrfStatus.COMPLETED, command.getStudyParticipantCrfSchedule().getStatus());
//        assertEquals("You have successfully submitted the form.", command.getFlashMessage());
//
//
//        verifyMocks();
//    }
//      public void testPostRequest() throws Exception {
//
//        EasyMock.expect(finderRepository.findById(StudyParticipantCrfSchedule.class, Integer.parseInt("1"))).andReturn(studyParticipantCrfSchedule);
//        EasyMock.expectLastCall().anyTimes();
//        EasyMock.expect(genericRepository.save(studyParticipantCrfSchedule)).andReturn(studyParticipantCrfSchedule);
//        EasyMock.expectLastCall().anyTimes();
//        replayMocks();
//
//
//        request.setMethod("GET");
//        request.addParameter("id", "1");
//        ModelAndView modelAndView = controller.handleRequest(request, response);
//        SubmitFormCommand command = (SubmitFormCommand) modelAndView.getModel().get("command");
//
//        request.setMethod("POST");
//
//        command.setDirection("continue");
//        modelAndView = controller.handleRequest(request, response);
//        assertEquals(controller.getFormView(), modelAndView.getViewName());
//        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
//        assertEquals(2, command.getCurrentPageIndex());
//
//        command.setDirection("continue");
//        modelAndView = controller.handleRequest(request, response);
//        BeanPropertyBindingResult r = (BeanPropertyBindingResult) modelAndView.getModel().get("org.springframework.validation.BindingResult.command");
//        assertEquals(1, r.getAllErrors().size());
//        assertEquals(controller.getFormView(), modelAndView.getViewName());
//        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
//        assertEquals(2, command.getCurrentPageIndex());
//
//        command.setDirection("back");
//        modelAndView = controller.handleRequest(request, response);
//        assertEquals(controller.getFormView(), modelAndView.getViewName());
//        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
//        assertEquals(1, command.getCurrentPageIndex());
//
//        command.setCurrentPageIndex(command.getTotalPages());
//        command.setDirection("continue");
//        modelAndView = controller.handleRequest(request, response);
//        assertEquals("form/addquestion", modelAndView.getViewName());
//        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
//
//        command.setDirection("continue");
//        modelAndView = controller.handleRequest(request, response);
//        assertEquals(controller.getReviewView(), modelAndView.getViewName());
//        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
//
//        command.setDirection("back");
//        modelAndView = controller.handleRequest(request, response);
//        assertEquals("form/addquestion", modelAndView.getViewName());
//        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
//
//        command.setDirection("back");
//        modelAndView = controller.handleRequest(request, response);
//        assertEquals("form/participantAddedQuestion", modelAndView.getViewName());
//        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
//
//        command.setDirection("back");
//        modelAndView = controller.handleRequest(request, response);
//        assertEquals("form/participantAddedQuestion", modelAndView.getViewName());
//        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
//
//        command.setDirection("back");
//        modelAndView = controller.handleRequest(request, response);
//        assertEquals(controller.getFormView(), modelAndView.getViewName());
//        assertEquals(CrfStatus.INPROGRESS, command.getStudyParticipantCrfSchedule().getStatus());
//
//        command.setDirection("save");
//        modelAndView = controller.handleRequest(request, response);
//        assertEquals(controller.getSuccessView(), modelAndView.getViewName());
//        assertEquals(CrfStatus.COMPLETED, command.getStudyParticipantCrfSchedule().getStatus());
//        assertEquals("You have successfully submitted the form.", command.getFlashMessage());
//
//
//        verifyMocks();
//
//    }
}