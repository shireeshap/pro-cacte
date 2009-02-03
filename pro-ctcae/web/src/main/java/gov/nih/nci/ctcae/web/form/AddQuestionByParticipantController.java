package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
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

    /**
     * The generic repository.
     */
    private GenericRepository genericRepository;

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
        studyParticipantCrfSchedule = finderRepository.findById(StudyParticipantCrfSchedule.class, studyParticipantCrfSchedule.getId());
        StudyParticipantCrf studyParticipantCrf = finderRepository.findById(StudyParticipantCrf.class, studyParticipantCrfSchedule.getStudyParticipantCrf().getId());
        int pageNumber = ((SubmitFormCommand) command).getTotalPages();
        request.getSession().setAttribute("gotopage", "" + (pageNumber + 1));
        if ("continue".equals(((SubmitFormCommand) command).getDirection())) {
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
                        genericRepository.save(studyParticipantCrfAddedQuestion);

                        StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion = new StudyParticipantCrfScheduleAddedQuestion();
                        studyParticipantCrfSchedule.addStudyParticipantCrfScheduleAddedQuestion(studyParticipantCrfScheduleAddedQuestion);
                        genericRepository.save(studyParticipantCrfScheduleAddedQuestion);

                    }
                    pageNumber++;
                }
            }
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
        List<ProCtcQuestion> questions = (List<ProCtcQuestion>) finderRepository.find(query);
        submitFormCommand.setProCtcQuestions(questions);

        return submitFormCommand;
    }

    /**
     * Sets the generic repository.
     *
     * @param genericRepository the new generic repository
     */
    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
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
}