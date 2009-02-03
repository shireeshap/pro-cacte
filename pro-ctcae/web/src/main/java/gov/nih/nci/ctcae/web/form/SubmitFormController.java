package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfScheduleAddedQuestion;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * User: Harsh
 * Date: Nov 12, 2008
 * Time: 1:36:54 PM.
 */
public class SubmitFormController extends CtcAeSimpleFormController {
    
    /** The generic repository. */
    GenericRepository genericRepository;
    
    /** The review view. */
    private String reviewView;

    /**
     * Instantiates a new submit form controller.
     */
    public SubmitFormController() {
        setFormView("form/submitForm");
        setSuccessView("form/confirmFormSubmission");
        setReviewView("form/reviewFormSubmission");
        setCommandClass(SubmitFormCommand.class);
        setSessionForm(true);
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        SubmitFormCommand submitFormCommand = (SubmitFormCommand) command;
        if ("save".equals(submitFormCommand.getDirection())) {
            submitFormCommand.getStudyParticipantCrfSchedule().setStatus(CrfStatus.COMPLETED);
        } else {
            submitFormCommand.getStudyParticipantCrfSchedule().setStatus(CrfStatus.INPROGRESS);
        }
        submitFormCommand.deleteQuestions(request.getParameter("deletedQuestions"));
        genericRepository.save(submitFormCommand.getStudyParticipantCrfSchedule());
        submitFormCommand.setStudyParticipantCrfSchedule(finderRepository.findById(StudyParticipantCrfSchedule.class, submitFormCommand.getStudyParticipantCrfSchedule().getId()));
        return showForm(request, response, errors);
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#showForm(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.validation.BindException)
     */
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
            return mv;
        }

        if (!StringUtils.isBlank((String) request.getSession().getAttribute("gotopage"))) {
            submitFormCommand.setCurrentPageIndex(Integer.parseInt((String) request.getSession().getAttribute("gotopage")));
            request.getSession().removeAttribute("gotopage");
        }

        if (submitFormCommand.getCurrentPageIndex() == submitFormCommand.getTotalPages() + 1) {
            mv = showForm(request, errors, "form/addquestion");
            return mv;
        }

        if (submitFormCommand.getCurrentPageIndex() > submitFormCommand.getTotalPages() + 1) {
            mv = showForm(request, errors, getReviewView());
            return mv;
        }

        if (submitFormCommand.getCurrentPageIndex() >= submitFormCommand.getParticipantAddedQuestionIndex()) {
            for (StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion : submitFormCommand.getStudyParticipantCrfSchedule().getStudyParticipantCrfScheduleAddedQuestions()) {
                studyParticipantCrfScheduleAddedQuestion.getProCtcValidValue();
            }
            mv = showForm(request, errors, "form/participantAddedQuestion");
        } else {
            mv = showForm(request, errors, getFormView());
        }

        return mv;
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
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

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.web.CtcAeSimpleFormController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException)
     */
    @Override
    protected void onBindAndValidate(HttpServletRequest request,
                                     Object command, BindException errors) throws Exception {
        super.onBindAndValidate(request, command, errors);

        SubmitFormCommand submitFormCommand = (SubmitFormCommand) command;
        StudyParticipantCrfSchedule studyParticipantCrfSchedule = finderRepository.findById(StudyParticipantCrfSchedule.class, submitFormCommand.getStudyParticipantCrfSchedule().getId());
        if ("continue".equals(submitFormCommand.getDirection())) {
            for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
                if (studyParticipantCrfItem.getCrfPageItem().getCrfPage().getPageNumber() == submitFormCommand.getCurrentPageIndex() - 2) {
                    if (new Boolean(true).equals(studyParticipantCrfItem.getCrfPageItem().getResponseRequired())) {
                        if (studyParticipantCrfItem.getProCtcValidValue() == null) {
                            errors.reject(
                                    "answer", "Please select an answer for question " + studyParticipantCrfItem.getCrfPageItem().getDisplayOrder() + ".");
                            submitFormCommand.setCurrentPageIndex(submitFormCommand.getCurrentPageIndex() - 1);
                            return;
                        }
                    }
                }
            }
        }
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
