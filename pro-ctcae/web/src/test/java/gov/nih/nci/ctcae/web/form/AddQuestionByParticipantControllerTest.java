package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfAddedQuestionRepository;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfRepository;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfScheduleAddedQuestionRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Harsh Agarwal
 * @crated Nov 25, 2008
 */
public class AddQuestionByParticipantControllerTest extends WebTestCase {
    private AddQuestionByParticipantController controller;
    private WebControllerValidator validator;
    private GenericRepository genericRepository;

    private StudyParticipantCrfSchedule studyParticipantCrfSchedule;
    private CRF crf;
    private Study study;
    private StudyParticipantAssignment studyParticipantAssignment;
    private StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion1, studyParticipantCrfAddedQuestion2, studyParticipantCrfAddedQuestion3;
    List<ProCtcQuestion> questions = new ArrayList<ProCtcQuestion>();
    private StudyParticipantCrfRepository studyParticipantCrfRepository;
    protected StudyParticipantCrfAddedQuestionRepository studyParticipantCrfAddedQuestionRepository;
    protected StudyParticipantCrfScheduleAddedQuestionRepository studyParticipantCrfScheduleAddedQuestionRepository;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new AddQuestionByParticipantController();
        genericRepository = registerMockFor(GenericRepository.class);
        validator = new WebControllerValidatorImpl();

        studyParticipantCrfRepository = registerMockFor(StudyParticipantCrfRepository.class);

        controller.setWebControllerValidator(validator);
        controller.setProCtcQuestionRepository(proCtcQuestionRepository);
        controller.setStudyParticipantCrfScheduleRepository(studyParticipantCrfScheduleRepository);
        controller.setStudyParticipantCrfRepository(studyParticipantCrfRepository);
        studyParticipantCrfScheduleAddedQuestionRepository = registerMockFor(StudyParticipantCrfScheduleAddedQuestionRepository.class);
        controller.setStudyParticipantCrfScheduleAddedQuestionRepository(studyParticipantCrfScheduleAddedQuestionRepository);

        studyParticipantCrfAddedQuestionRepository = registerMockFor(StudyParticipantCrfAddedQuestionRepository.class);
        controller.setStudyParticipantCrfAddedQuestionRepository(studyParticipantCrfAddedQuestionRepository);
        studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
        studyParticipantCrfSchedule.setId(1);


        crf = Fixture.createCrf();
        crf.setId(1);

        CRFPage crfPage = (CRFPage) crf.addProCtcTerm(proCtcTerm1);
        CrfPageItem item1 = crfPage.getCrfPageItems().get(0);
        item1.setId(1);
        CrfPageItem item2 = crfPage.getCrfPageItems().get(1);
        item2.setId(2);

        study = Fixture.createStudyWithStudySite("short", "long", "assigned", Fixture.createOrganization("test", "test"));
        studyParticipantAssignment = new StudyParticipantAssignment();
        studyParticipantAssignment.setStudySite(study.getStudySites().get(0));
        crf.setStudy(study);

        StudyParticipantCrf studyParticipantCrf = new StudyParticipantCrf();
        studyParticipantCrf.setCrf(crf);
        studyParticipantAssignment.addStudyParticipantCrf(studyParticipantCrf);
        studyParticipantCrfSchedule.setStudyParticipantCrf(studyParticipantCrf);

        StudyParticipantCrfItem studyParticipantCrfItem1 = new StudyParticipantCrfItem();
        studyParticipantCrfItem1.setCrfPageItem(item1);

        StudyParticipantCrfItem studyParticipantCrfItem2 = new StudyParticipantCrfItem();
        studyParticipantCrfItem2.setCrfPageItem(item2);

        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem1);
        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem2);
        studyParticipantCrfSchedule.getStudyParticipantCrf().setId(1);

        questions.add(proCtcQuestion1);
        questions.add(proCtcQuestion2);
        questions.add(proCtcQuestion3);
        questions.add(proCtcQuestion4);
        questions.add(proCtcQuestion5);
        questions.add(proCtcQuestion6);
        questions.add(proCtcQuestion7);
        questions.add(proCtcQuestion8);

        expect(proCtcQuestionRepository.find(isA(ProCtcQuestionQuery.class))).andReturn(questions).anyTimes();

    }

    public void testConstructor() {
        replayMocks();

        assertEquals("form/addQuestionForParticipant", controller.getFormView());
        assertEquals("form/confirmFormSubmission", controller.getSuccessView());
        verifyMocks();
    }

    public void testGetRequest() throws Exception {
        request.setMethod("GET");
        request.getSession().setAttribute(SubmitFormController.class.getName() + ".FORM." + "command", new SubmitFormCommand());

        //expect((List<ProCtcQuestion>) finderRepository.find(isA(Query.class))).andReturn(questions);
        replayMocks();

        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();

        Map model = modelAndView.getModel();
        Object command = model.get("command");
        assertNotNull("must find command object", command);
        assertTrue(command instanceof SubmitFormCommand);
        assertEquals(controller.getFormView(), modelAndView.getViewName());
        assertEquals(questions, ((SubmitFormCommand) command).getProCtcQuestions());
    }

    public void testPostRequest() throws Exception {
        expect(studyParticipantCrfScheduleRepository.save(isA(StudyParticipantCrfSchedule.class))).andReturn(studyParticipantCrfSchedule).anyTimes();
        replayMocks();

        SubmitFormCommand command = new SubmitFormCommand();
        command.setStudyParticipantCrfScheduleRepository(studyParticipantCrfScheduleRepository);
        command.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
        command.setStudyParticipantCrfRepository(studyParticipantCrfRepository);
        command.initialize();
        request.setMethod("GET");
        request.getSession().setAttribute(SubmitFormController.class.getName() + ".FORM." + "command", command);
        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();
        resetMocks();
        command = (SubmitFormCommand) modelAndView.getModel().get("command");

        expect(genericRepository.save(isA(StudyParticipantCrfAddedQuestion.class))).andReturn(null).anyTimes();
        expect(genericRepository.save(isA(StudyParticipantCrfScheduleAddedQuestion.class))).andReturn(null).anyTimes();
        expect(studyParticipantCrfScheduleRepository.findById(1)).andReturn(studyParticipantCrfSchedule).anyTimes();
        expect(studyParticipantCrfRepository.findById(1)).andReturn(new StudyParticipantCrf()).anyTimes();

        expect(studyParticipantCrfAddedQuestionRepository.save(isA(StudyParticipantCrfAddedQuestion.class))).andReturn(studyParticipantCrfAddedQuestion1).anyTimes();
        expect(studyParticipantCrfScheduleAddedQuestionRepository.save(isA(StudyParticipantCrfScheduleAddedQuestion.class))).andReturn(new StudyParticipantCrfScheduleAddedQuestion()).anyTimes();
        expect(proCtcQuestionRepository.find(isA(ProCtcQuestionQuery.class))).andReturn(questions);

        replayMocks();

        request.setMethod("POST");
        command.setDirection("continue");
        request.setParameter("symptomsByParticipants", new String[]{"Pain"});

        assertEquals(0, command.getStudyParticipantCrfSchedule().getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions().size());
        assertEquals(0, command.getStudyParticipantCrfSchedule().getStudyParticipantCrfScheduleAddedQuestions().size());

        modelAndView = controller.handleRequest(request, response);
        verifyMocks();
//        assertEquals(2, command.getStudyParticipantCrfSchedule().getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions().size());
//        assertEquals(2, command.getStudyParticipantCrfSchedule().getStudyParticipantCrfScheduleAddedQuestions().size());
//        assertEquals("Pain", command.getStudyParticipantCrfSchedule().getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions().get(0).getProCtcQuestion().getProCtcTerm().getTerm());
//        assertEquals(command.getTotalPages(), command.getStudyParticipantCrfSchedule().getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions().get(0).getPageNumber().intValue());
//        assertEquals("Pain", command.getStudyParticipantCrfSchedule().getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions().get(1).getProCtcQuestion().getProCtcTerm().getTerm());
//        assertEquals(command.getTotalPages(), command.getStudyParticipantCrfSchedule().getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions().get(1).getPageNumber().intValue());
        assertNotNull(modelAndView);

    }
}