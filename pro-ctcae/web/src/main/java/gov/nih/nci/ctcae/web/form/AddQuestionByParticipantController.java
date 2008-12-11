package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.core.domain.CrfItem;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.beans.factory.annotation.Required;

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

    public AddQuestionByParticipantController() {
        setFormView("form/addQuestionForParticipant");
        setSuccessView("form/reviewFormSubmission");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        SubmitFormCommand submitFormCommand = (SubmitFormCommand) command;
        if ("continue".equals(submitFormCommand.getDirection())) {
            String[] selectedQuestions = request.getParameterValues("questionsByParticipants");
            int i = 0;
            if (selectedQuestions != null) {
                for (String questionId : selectedQuestions) {
                    ProCtcQuestion question = finderRepository.findById(ProCtcQuestion.class, new Integer(questionId));
                    String answerId = request.getParameter("answer" + questionId);
                    ProCtcValidValue validValue = finderRepository.findById(ProCtcValidValue.class, new Integer(answerId));

                    StudyParticipantCrfItem studyParticipantCrfItem = new StudyParticipantCrfItem();

                    CrfItem crfItem = new CrfItem();
                    crfItem.setProCtcQuestion(question);
                    crfItem.setDisplayOrder(100 + i++);

                    crfItem = genericRepository.save(crfItem);

                    studyParticipantCrfItem.setCrfItem(crfItem);
                    studyParticipantCrfItem.setStudyParticipantCrfSchedule(submitFormCommand.getStudyParticipantCrfSchedule());
                    studyParticipantCrfItem.setProCtcValidValue(validValue);
                    genericRepository.save(studyParticipantCrfItem);
                }
            }
        }

        return new ModelAndView(new RedirectView("submit?id=" + submitFormCommand.getStudyParticipantCrfSchedule().getId()));
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
}