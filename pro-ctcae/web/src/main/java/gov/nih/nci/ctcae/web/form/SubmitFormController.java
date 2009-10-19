package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.rules.NotificationsEvaluationService;
import gov.nih.nci.ctcae.core.rules.ProCtcAERulesService;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
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

    private String reviewView;
    private GenericRepository genericRepository;
    private ProCtcAERulesService proCtcAERulesService;

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
        String skip = "&skip=";
        if (submitFormCommand.getDirection().equals("back")) {
            skip = skip + "y";
        }
        if ("save".equals(submitFormCommand.getDirection())) {
            submitFormCommand.deleteQuestions();
            submitFormCommand.getStudyParticipantCrfSchedule().setStatus(CrfStatus.COMPLETED);
            submitFormCommand.setFlashMessage("You have successfully submitted the form.");
        } else {
            submitFormCommand.addQuestionToDeleteList(request.getParameter("deletedQuestions"));
            submitFormCommand.getStudyParticipantCrfSchedule().setStatus(CrfStatus.INPROGRESS);
        }
        StudyParticipantCrfSchedule savedStudyParticipantCrfSchedule = genericRepository.save(submitFormCommand.getStudyParticipantCrfSchedule());

        if ("save".equals(submitFormCommand.getDirection())) {
            initialize(savedStudyParticipantCrfSchedule, submitFormCommand);
            NotificationsEvaluationService notificationsEvaluationService = new NotificationsEvaluationService();
            notificationsEvaluationService.setGenericRepository(genericRepository);
            notificationsEvaluationService.setProCtcAERulesService(proCtcAERulesService);
            notificationsEvaluationService.executeRules(savedStudyParticipantCrfSchedule, savedStudyParticipantCrfSchedule.getStudyParticipantCrf().getCrf(), savedStudyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getStudySite());
            submitFormCommand.markAllPastDueSchedulesAsCancelled();
        }

        request.getSession().setAttribute(SubmitFormController.class.getName() + ".FORM." + "command", submitFormCommand);
        return new ModelAndView(new RedirectView("submit?id=" + savedStudyParticipantCrfSchedule.getId() + "&p=" + submitFormCommand.getCurrentPageIndex() + skip));
    }

    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.SimpleFormController#showForm(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.validation.BindException)
    */
    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        SubmitFormCommand submitFormCommand = (SubmitFormCommand) errors.getTarget();
        ModelAndView mv = null;
        String pageIndex = request.getParameter("p");

        if (StringUtils.isBlank(pageIndex)) {
            pageIndex = "1";
        }
        submitFormCommand.setCurrentPageIndex(Integer.parseInt(pageIndex));
        if (CrfStatus.COMPLETED.equals(submitFormCommand.getStudyParticipantCrfSchedule().getStatus())) {
            return showForm(request, errors, getSuccessView());
        }
        if (submitFormCommand.getCurrentPageIndex() == submitFormCommand.getAddQuestionPageIndex() && (!"y".equals(request.getParameter("skip")))) {
            mv = showForm(request, errors, getReviewView());
            mv.setView(new RedirectView("addquestion"));
            return mv;
        }
        if (submitFormCommand.getCurrentPageIndex() >= submitFormCommand.getTotalPages() + 1) {
            return showForm(request, errors, getReviewView());
        }
        if (submitFormCommand.getCurrentPageIndex() >= submitFormCommand.getParticipantAddedQuestionIndex()) {
            for (StudyParticipantCrfScheduleAddedQuestion cQ : submitFormCommand.getStudyParticipantCrfSchedule().getStudyParticipantCrfScheduleAddedQuestions()) {
                if (cQ.getPageNumber() + 1 == submitFormCommand.getCurrentPageIndex()) {
                    if (cQ.getProCtcQuestion() != null) {
                        mv = showForm(request, errors, "form/participantAddedQuestion");
                    } else {
                        cQ.getMeddraQuestion().getValidValues();
                        mv = showForm(request, errors, "form/participantAddedMeddraQuestion");
                    }
                    break;
                }
            }
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
        SubmitFormCommand submitFormCommand = (SubmitFormCommand)
                request.getSession().getAttribute(SubmitFormController.class.getName() + ".FORM." + "command");
        String crfScheduleId = request.getParameter("id");
        if (submitFormCommand == null || !crfScheduleId.equals(submitFormCommand.getStudyParticipantCrfSchedule().getId().toString())) {
            submitFormCommand = new SubmitFormCommand();
            if (!StringUtils.isBlank(crfScheduleId)) {
                StudyParticipantCrfSchedule studyParticipantCrfSchedule = genericRepository.findById(StudyParticipantCrfSchedule.class, Integer.parseInt(crfScheduleId));
                submitFormCommand.setGenericRepository(genericRepository);
                submitFormCommand.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
                submitFormCommand.initialize();
                submitFormCommand.setStudyParticipantCrfSchedule(genericRepository.save(submitFormCommand.getStudyParticipantCrfSchedule()));
            }
        }
        initialize(submitFormCommand.getStudyParticipantCrfSchedule(), submitFormCommand);
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
        StudyParticipantCrfSchedule studyParticipantCrfSchedule = genericRepository.findById(StudyParticipantCrfSchedule.class, submitFormCommand.getStudyParticipantCrfSchedule().getId());
        if ("continue".equals(submitFormCommand.getDirection())) {
            for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
                if (studyParticipantCrfItem.getCrfPageItem().getCrfPage().getPageNumber() == submitFormCommand.getCurrentPageIndex() - 2) {
                    if (studyParticipantCrfItem.getCrfPageItem().getResponseRequired()) {
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

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    private void initialize(StudyParticipantCrfSchedule studyParticipantCrfSchedule, SubmitFormCommand submitFormCommand) {
        studyParticipantCrfSchedule = genericRepository.findById(StudyParticipantCrfSchedule.class, studyParticipantCrfSchedule.getId());
        for (StudyParticipantClinicalStaff studyParticipantClinicalStaff : studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getStudyParticipantClinicalStaffs()) {
            studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff();
        }
        for (CRFPage crfPage : studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getCrfPagesSortedByPageNumber()) {
            for (CrfPageItem crfPageItem : crfPage.getCrfPageItems()) {
                crfPageItem.getProCtcQuestion();
            }
        }
        for (StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion : studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions()) {
            ProCtcQuestion proCtcQuestion = studyParticipantCrfScheduleAddedQuestion.getProCtcQuestion();
            if (proCtcQuestion != null) {
                proCtcQuestion.getProCtcQuestionDisplayRules();
            }
        }
        submitFormCommand.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);

    }

    @Required
    public void setProCtcAERulesService(ProCtcAERulesService proCtcAERulesService) {
        this.proCtcAERulesService = proCtcAERulesService;
    }
}

