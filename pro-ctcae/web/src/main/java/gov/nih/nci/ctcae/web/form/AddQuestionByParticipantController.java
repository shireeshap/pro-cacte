package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;
import gov.nih.nci.ctcae.core.repository.*;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Hashtable;
import java.util.List;

//
/**
 * Author: Harsh Agarwal
 * Date: Dec 10, 2008
 * Time: 1:36:54 PM.
 */
public class AddQuestionByParticipantController extends CtcAeSimpleFormController {

    private ProCtcQuestionRepository proCtcQuestionRepository;
    private StudyParticipantCrfRepository studyParticipantCrfRepository;
    private StudyParticipantCrfAddedQuestionRepository studyParticipantCrfAddedQuestionRepository;
    private StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;

    private StudyParticipantCrfScheduleAddedQuestionRepository studyParticipantCrfScheduleAddedQuestionRepository;


    /**
     * The review view.
     */
    private String reviewView;

    /**
     * Instantiates a new adds the question by participant controller.
     */
    public AddQuestionByParticipantController() {
        super();
        setFormView("form/addQuestionForParticipant");
        setSuccessView("form/confirmFormSubmission");
        setReviewView("form/reviewFormSubmission");
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        StudyParticipantCrfSchedule studyParticipantCrfSchedule = ((SubmitFormCommand) command).getStudyParticipantCrfSchedule();
        studyParticipantCrfSchedule = studyParticipantCrfScheduleRepository.findById(studyParticipantCrfSchedule.getId());
        StudyParticipantCrf studyParticipantCrf = studyParticipantCrfRepository.findById(studyParticipantCrfSchedule.getStudyParticipantCrf().getId());
        int pageNumber = ((SubmitFormCommand) command).getTotalPages();
        request.getSession().setAttribute("questionstobedeletedlist", ((SubmitFormCommand) command).getQuestionsToBeDeleted());
        if ("continue".equals(((SubmitFormCommand) command).getDirection())) {
            request.getSession().setAttribute("gotopage", "" + (pageNumber + 1));
            request.getSession().setAttribute("skipaddquestion", "true");

            String[] selectedSymptoms = request.getParameterValues("symptomsByParticipants");
            Hashtable arrangedQuestions = ((SubmitFormCommand) command).getArrangedQuestions();

            if (selectedSymptoms != null) {
                for (String symptom : selectedSymptoms) {
                    List<ProCtcQuestion> questions = (List<ProCtcQuestion>) arrangedQuestions.get(symptom);
                    for (ProCtcQuestion question : questions) {

                        StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion = new StudyParticipantCrfAddedQuestion();
                        studyParticipantCrfAddedQuestion.setProCtcQuestion(question);
                        studyParticipantCrfAddedQuestion.setPageNumber(pageNumber);
                        studyParticipantCrf.addStudyParticipantCrfAddedQuestion(studyParticipantCrfAddedQuestion);
                        studyParticipantCrfAddedQuestionRepository.save(studyParticipantCrfAddedQuestion);

                        StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion = new StudyParticipantCrfScheduleAddedQuestion();
                        studyParticipantCrfScheduleAddedQuestion.setProCtcQuestion(question);
                        studyParticipantCrfScheduleAddedQuestion.setPageNumber(pageNumber);
                        studyParticipantCrfScheduleAddedQuestion.setStudyParticipantCrfAddedQuestion(studyParticipantCrfAddedQuestion);
                        studyParticipantCrfSchedule.addStudyParticipantCrfScheduleAddedQuestion(studyParticipantCrfScheduleAddedQuestion);
                        studyParticipantCrfScheduleAddedQuestionRepository.save(studyParticipantCrfScheduleAddedQuestion);

                    }
                    pageNumber++;
                }
            }
        } else {
            request.getSession().setAttribute("gotopage", "" + pageNumber);
        }
        return new ModelAndView(new RedirectView("submit?id=" + ((SubmitFormCommand) command).getStudyParticipantCrfSchedule().getId()));
    }


    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        SubmitFormCommand submitFormCommand = (SubmitFormCommand)
                request.getSession().getAttribute(SubmitFormController.class.getName() + ".FORM." + "command");

        ProCtcQuestionQuery query = new ProCtcQuestionQuery();
        List<ProCtcQuestion> questions = (List<ProCtcQuestion>) proCtcQuestionRepository.find(query);
        submitFormCommand.setProCtcQuestions(questions);

        return submitFormCommand;
    }


    /**
     * Gets the review view.
     *
     * @return the review view
     */
    public String getReviewView() {
        return reviewView;
    }

    /**
     * Sets the review view.
     *
     * @param reviewView the new review view
     */
    public void setReviewView(String reviewView) {
        this.reviewView = reviewView;
    }

    @Required
    public void setProCtcQuestionRepository(ProCtcQuestionRepository proCtcQuestionRepository) {
        this.proCtcQuestionRepository = proCtcQuestionRepository;
    }

    @Required

    public void setStudyParticipantCrfRepository(StudyParticipantCrfRepository studyParticipantCrfRepository) {
        this.studyParticipantCrfRepository = studyParticipantCrfRepository;
    }

    @Required

    public void setStudyParticipantCrfAddedQuestionRepository(StudyParticipantCrfAddedQuestionRepository studyParticipantCrfAddedQuestionRepository) {
        this.studyParticipantCrfAddedQuestionRepository = studyParticipantCrfAddedQuestionRepository;
    }

    @Required

    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }

    @Required

    public void setStudyParticipantCrfScheduleAddedQuestionRepository(StudyParticipantCrfScheduleAddedQuestionRepository studyParticipantCrfScheduleAddedQuestionRepository) {
        this.studyParticipantCrfScheduleAddedQuestionRepository = studyParticipantCrfScheduleAddedQuestionRepository;
    }
}