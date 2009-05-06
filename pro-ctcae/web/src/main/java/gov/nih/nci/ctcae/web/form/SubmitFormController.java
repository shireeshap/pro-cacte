package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfAddedQuestionRepository;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfRepository;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfScheduleAddedQuestionRepository;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import gov.nih.nci.ctcae.web.rules.NotificationsEvaluationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

//
/**
 * User: Harsh
 * Date: Nov 12, 2008
 * Time: 1:36:54 PM.
 */
public class SubmitFormController extends CtcAeSimpleFormController {

    private StudyParticipantCrfRepository studyParticipantCrfRepository;
    private StudyParticipantCrfAddedQuestionRepository studyParticipantCrfAddedQuestionRepository;
    /**
     * The review view.
     */
    private String reviewView;
    private StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;

    private StudyParticipantCrfScheduleAddedQuestionRepository studyParticipantCrfScheduleAddedQuestionRepository;


    /**
     * Instantiates a new submit form controller.
     */
    public SubmitFormController() {
        super();
        setFormView("form/submitForm");
        setSuccessView("form/confirmFormSubmission");
        this.reviewView = "form/reviewFormSubmission";
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
            submitFormCommand.deleteQuestions();
            submitFormCommand.getStudyParticipantCrfSchedule().setStatus(CrfStatus.COMPLETED);
            StudyParticipantCrfSchedule s = initialize(submitFormCommand.getStudyParticipantCrfSchedule());
            NotificationsEvaluationService.executeRules(s, s.getStudyParticipantCrf().getCrf(), s.getStudyParticipantCrf().getStudyParticipantAssignment().getStudySite());
        } else {
            submitFormCommand.getStudyParticipantCrfSchedule().setStatus(CrfStatus.INPROGRESS);
        }
        submitFormCommand.addQuestionToDeleteList(request.getParameter("deletedQuestions"));
        studyParticipantCrfScheduleRepository.save(submitFormCommand.getStudyParticipantCrfSchedule());
        submitFormCommand.setStudyParticipantCrfSchedule(studyParticipantCrfScheduleRepository.findById(submitFormCommand.getStudyParticipantCrfSchedule().getId()));
        return showForm(request, response, errors);
    }

    private StudyParticipantCrfSchedule initialize(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        StudyParticipantCrfSchedule s = studyParticipantCrfScheduleRepository.findById(studyParticipantCrfSchedule.getId());
        for (StudyParticipantClinicalStaff studyParticipantClinicalStaff : s.getStudyParticipantCrf().getStudyParticipantAssignment().getStudyParticipantClinicalStaffs()) {
            studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff();
        }
        for (CRFPage crfPage : s.getStudyParticipantCrf().getCrf().getCrfPagesSortedByPageNumber()) {
            for (CrfPageItem crfPageItem : crfPage.getCrfPageItems()) {
                crfPageItem.getProCtcQuestion();
            }
        }
        return s;
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
            if (StringUtils.isBlank((String) request.getSession().getAttribute("skipaddquestion"))) {
                mv = showForm(request, errors, getReviewView());
                mv.setView(new RedirectView("addquestion"));
                return mv;
            } else {
                submitFormCommand.setCurrentPageIndex(submitFormCommand.getCurrentPageIndex() + 1);
                request.getSession().removeAttribute("skipaddquestion");
            }
        }

        if (submitFormCommand.getCurrentPageIndex() > submitFormCommand.getTotalPages() + 1)

        {
            mv = showForm(request, errors, getReviewView());
            return mv;
        }

        if (submitFormCommand.getCurrentPageIndex() >= submitFormCommand.getParticipantAddedQuestionIndex())

        {
            for (StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion : submitFormCommand.getStudyParticipantCrfSchedule().getStudyParticipantCrfScheduleAddedQuestions()) {
                studyParticipantCrfScheduleAddedQuestion.getProCtcValidValue();
            }
            mv = showForm(request, errors, "form/participantAddedQuestion");
        } else

        {
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
        Set<String> questionsToBeDeleted = (Set<String>) request.getSession().getAttribute("questionstobedeletedlist");
        if (questionsToBeDeleted != null) {
            submitFormCommand.setQuestionsToBeDeleted(questionsToBeDeleted);
        }

        if (!StringUtils.isBlank(crfScheduleId)) {
            StudyParticipantCrfSchedule studyParticipantCrfSchedule = studyParticipantCrfScheduleRepository.findById(Integer.parseInt(crfScheduleId));
            submitFormCommand.setStudyParticipantCrfScheduleAddedQuestionRepository(studyParticipantCrfScheduleAddedQuestionRepository);
            submitFormCommand.setStudyParticipantCrfScheduleRepository(studyParticipantCrfScheduleRepository);
            submitFormCommand.setStudyParticipantCrfRepository(studyParticipantCrfRepository);
            submitFormCommand.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
            submitFormCommand.setStudyParticipantCrfAddedQuestionRepository(studyParticipantCrfAddedQuestionRepository);
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
        StudyParticipantCrfSchedule studyParticipantCrfSchedule = studyParticipantCrfScheduleRepository.findById(submitFormCommand.getStudyParticipantCrfSchedule().getId());
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
     * Gets the review view.
     *
     * @return the review view
     */
    public String getReviewView() {
        return reviewView;
    }

    @Required

    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }

    @Required
    public void setStudyParticipantCrfRepository(StudyParticipantCrfRepository studyParticipantCrfRepository) {
        this.studyParticipantCrfRepository = studyParticipantCrfRepository;
    }

    @Required

    public void setStudyParticipantCrfScheduleAddedQuestionRepository(StudyParticipantCrfScheduleAddedQuestionRepository studyParticipantCrfScheduleAddedQuestionRepository) {
        this.studyParticipantCrfScheduleAddedQuestionRepository = studyParticipantCrfScheduleAddedQuestionRepository;
    }

    @Required
    public void setStudyParticipantCrfAddedQuestionRepository(StudyParticipantCrfAddedQuestionRepository studyParticipantCrfAddedQuestionRepository) {
        this.studyParticipantCrfAddedQuestionRepository = studyParticipantCrfAddedQuestionRepository;
    }
}

