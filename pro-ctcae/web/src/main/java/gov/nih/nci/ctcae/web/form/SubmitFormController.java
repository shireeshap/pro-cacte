package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfScheduleAddedQuestion;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: Harsh
 * Date: Nov 12, 2008
 * Time: 1:36:54 PM
 */
public class SubmitFormController extends CtcAeSimpleFormController {
    GenericRepository genericRepository;
    private String reviewView;

    public SubmitFormController() {
        setFormView("form/submitForm");
        setSuccessView("form/confirmFormSubmission");
        setReviewView("form/reviewFormSubmission");
        setCommandClass(SubmitFormCommand.class);
        setSessionForm(true);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        SubmitFormCommand submitFormCommand = (SubmitFormCommand) command;
        if ("save".equals(submitFormCommand.getDirection())) {
            submitFormCommand.getStudyParticipantCrfSchedule().setStatus(CrfStatus.COMPLETED);
        } else {
            submitFormCommand.getStudyParticipantCrfSchedule().setStatus(CrfStatus.INPROGRESS);
        }
        genericRepository.save(submitFormCommand.getStudyParticipantCrfSchedule());
        submitFormCommand.setStudyParticipantCrfSchedule(finderRepository.findById(StudyParticipantCrfSchedule.class, submitFormCommand.getStudyParticipantCrfSchedule().getId()));
        return showForm(request, response, errors);
    }

    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        SubmitFormCommand submitFormCommand = (SubmitFormCommand) errors.getTarget();
        ModelAndView mv = null;

        if (CrfStatus.COMPLETED.equals(submitFormCommand.getStudyParticipantCrfSchedule().getStatus())) {
            if ("save".equals(submitFormCommand.getDirection())) {
                submitFormCommand.setFlashMessage("You have successfully submitted the form.");
            } else {
                submitFormCommand.setFlashMessage("You have already submitted the form.");
            }
            mv = showForm(request, errors, getSuccessView());
        } else {
            if (submitFormCommand.getCurrentPageIndex() > submitFormCommand.getTotalPages() || "y".equals(request.getParameter("review"))) {
                submitFormCommand.setDirection("");
                mv = showForm(request, errors, getReviewView());
            } else {
                if (submitFormCommand.getCurrentPageIndex() == submitFormCommand.getTotalPages() && submitFormCommand.isHasParticipantAddedQuestions()) {
                    for (StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion : submitFormCommand.getStudyParticipantCrfSchedule().getStudyParticipantCrfScheduleAddedQuestions()) {
                        studyParticipantCrfScheduleAddedQuestion.getProCtcValidValue();
                    }
                    mv = showForm(request, errors, "form/participantAddedQuestion");
                } else {
                    mv = showForm(request, errors, getFormView());
                }
            }
        }

        return mv;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        String crfScheduleId = request.getParameter("id");
        SubmitFormCommand submitFormCommand = new SubmitFormCommand();

        if (!StringUtils.isBlank(crfScheduleId)) {
            StudyParticipantCrfSchedule studyParticipantCrfSchedule = finderRepository.findById(StudyParticipantCrfSchedule.class, Integer.parseInt(crfScheduleId));
            submitFormCommand.setFinderRepository(finderRepository);
            submitFormCommand.setGenericRepository(genericRepository);
            submitFormCommand.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
            submitFormCommand.initialize();
        }
        return submitFormCommand;
    }

    /*	@Override
     protected void onBindAndValidate(HttpServletRequest request,
                                      Object command, BindException errors) throws Exception {
         super.onBindAndValidate(request, command, errors);

         SubmitFormCommand submitFormCommand = (SubmitFormCommand) command;

         if ("continue".equals(submitFormCommand.getDirection())) {
             List<StudyParticipantCrfItem> l = submitFormCommand.getPages().get(submitFormCommand.getCurrentPageIndex());
             for (StudyParticipantCrfItem studyParticipantCrfItem : l) {
                 if (new Boolean(true).equals(studyParticipantCrfItem.getCrfPageItem().getResponseRequired())) {
                     if (studyParticipantCrfItem.getProCtcValidValue() == null) {
                         errors.reject(
                             "answer", "Please select an answer.");
                         return;
                     }
                 }
             }
         }
     }
    */
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
