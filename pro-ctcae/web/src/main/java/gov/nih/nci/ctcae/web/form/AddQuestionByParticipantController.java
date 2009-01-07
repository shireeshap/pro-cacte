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
import java.util.List;

/**
 * Author: Harsh Agarwal
 * Date: Dec 10, 2008
 * Time: 1:36:54 PM
 */
public class AddQuestionByParticipantController extends CtcAeSimpleFormController {

    private GenericRepository genericRepository;
    private String reviewView;

    public AddQuestionByParticipantController() {
        setFormView("form/addQuestionForParticipant");
        setSuccessView("form/confirmFormSubmission");
        setReviewView("form/reviewFormSubmission");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        SubmitFormCommand submitFormCommand = (SubmitFormCommand) command;
        StudyParticipantCrfSchedule studyParticipantCrfSchedule = submitFormCommand.getStudyParticipantCrfSchedule();
        StudyParticipantCrf studyParticipantCrf = studyParticipantCrfSchedule.getStudyParticipantCrf();
        if ("continue".equals(submitFormCommand.getDirection())) {
            String[] selectedQuestions = request.getParameterValues("questionsByParticipants");
            int i = 0;
            if (selectedQuestions != null) {
                for (String questionId : selectedQuestions) {
                    ProCtcQuestion question = finderRepository.findById(ProCtcQuestion.class, new Integer(questionId));
                    String answerId = request.getParameter("answer" + questionId);
                    ProCtcValidValue validValue = finderRepository.findById(ProCtcValidValue.class, new Integer(answerId));

                    StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion = new StudyParticipantCrfAddedQuestion();
                    studyParticipantCrfAddedQuestion.setProCtcQuestion(question);
                    studyParticipantCrfAddedQuestion.setStudyParticipantCrf(studyParticipantCrf);

                    StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion = new StudyParticipantCrfScheduleAddedQuestion();
                    studyParticipantCrfScheduleAddedQuestion.setProCtcValidValue(validValue);
                    studyParticipantCrfScheduleAddedQuestion.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);

                    genericRepository.save(studyParticipantCrfAddedQuestion);
                    genericRepository.save(studyParticipantCrfScheduleAddedQuestion);
                }
            }
        }

        return new ModelAndView(new RedirectView("submit?review=y&id=" + submitFormCommand.getStudyParticipantCrfSchedule().getId()));
    }


    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        SubmitFormCommand submitFormCommand = (SubmitFormCommand)
                request.getSession().getAttribute(SubmitFormController.class.getName() + ".FORM." + "command");

        ProCtcQuestionQuery query = new ProCtcQuestionQuery();
        List<ProCtcQuestion> questions = (List<ProCtcQuestion>) finderRepository.find(query);
        submitFormCommand.setProCtcQuestions(questions);

        return submitFormCommand;
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    public String getReviewView() {
        return reviewView;
    }

    public void setReviewView(String reviewView) {
        this.reviewView = reviewView;
    }
}