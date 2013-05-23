package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.AppMode;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.MeddraValidValue;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfScheduleAddedQuestion;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfScheduleNotification;
import gov.nih.nci.ctcae.core.domain.ValidValue;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.MeddraRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

//

/**
 * User: Harsh
 * Date: Nov 12, 2008
 * Time: 1:36:54 PM.
 */
public class SubmitFormController extends SimpleFormController {
    private GenericRepository genericRepository;
    private StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;
    private ProCtcTermRepository proCtcTermRepository;
    private MeddraRepository meddraRepository;

    public SubmitFormController() {
        super();
        setFormView("form/submitForm");
        setSuccessView("form/confirmFormSubmission");
        setCommandClass(SubmitFormCommand.class);
        setSessionForm(true);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        SubmitFormCommand sCommand = (SubmitFormCommand) command;
        
		/* Save() call also updates the further page number to be rendered and stores it in the command object (increments or decrements
		 * the page no. based on flow-button hit on the UI). 
		*/
        boolean submit = save(sCommand);
        request.getSession().setAttribute(getFormSessionAttributeName(), sCommand);
        if (submit) {
            return showConfirmationPage(sCommand);
        } else {
        	request.getSession().setAttribute("id", sCommand.getSchedule().getId());
        	return new ModelAndView(new RedirectView("submit"));
        }
    }
    

    public boolean save(SubmitFormCommand sCommand) throws Exception {
        boolean submit = false;

        if ("save".equals(sCommand.getDirection())) {
        	sCommand.deleteQuestions();
        	sCommand.getSchedule().setStatus(CrfStatus.COMPLETED);
        	sCommand.getSchedule().setFormSubmissionMode(AppMode.HOMEWEB);
            //adding the notifications scheduled for the form submission
        	sCommand.getSchedule().setCompletionDate(new Date());
            if (sCommand.getSchedule().getStudyParticipantCrfScheduleNotification() == null) {
                StudyParticipantCrfScheduleNotification studyParticipantCrfScheduleNotification = new StudyParticipantCrfScheduleNotification();
                studyParticipantCrfScheduleNotification.setStudyParticipantCrfSchedule(sCommand.getSchedule());
                sCommand.getSchedule().setStudyParticipantCrfScheduleNotification(studyParticipantCrfScheduleNotification);
            }

            sCommand.setFlashMessage("You have successfully submitted the form.");
            submit = true;
        } else {
        	sCommand.getSchedule().setStatus(CrfStatus.INPROGRESS);
            List<DisplayQuestion> questions = sCommand.getCurrentPageQuestions();
            if (questions != null) {
                for (DisplayQuestion question : questions) {
                    if (question != null) {
                        StudyParticipantCrfItem spCrfItem = question.getStudyParticipantCrfItem();
                        if (spCrfItem != null) {
                            spCrfItem.setReponseDate(new Date());
                            spCrfItem.setResponseMode(AppMode.HOMEWEB);
                            spCrfItem.setUpdatedBy(sCommand.getUserName());
                        }
                        StudyParticipantCrfScheduleAddedQuestion spcsAddedQuestion = question.getStudyParticipantCrfScheduleAddedQuestion();
                        if (spcsAddedQuestion != null) {
                            spcsAddedQuestion.setReponseDate(new Date());
                            spcsAddedQuestion.setResponseMode(AppMode.HOMEWEB);
                            spcsAddedQuestion.setUpdatedBy(sCommand.getUserName());
                        }
                    }
                }
            }
        }
        sCommand.setSchedule(studyParticipantCrfScheduleRepository.save(sCommand.getSchedule()));
        return submit;
    }
    

    private ModelAndView showConfirmationPage(SubmitFormCommand sCommand) {
        ModelAndView modelAndView = new ModelAndView(getSuccessView());
        modelAndView.addObject("scheduleid", sCommand.getSchedule().getId());
        return modelAndView;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        SubmitFormCommand command = (SubmitFormCommand) request.getSession().getAttribute(getFormSessionAttributeName());
        String crfScheduleId = request.getParameter("id");
        if(crfScheduleId == null){
        	crfScheduleId = request.getSession().getAttribute("id").toString();  //request.getParameter("id");
        }
        if (command != null && crfScheduleId.equals(command.getSchedule().getId().toString())) {
            command.lazyInitializeSchedule();
            return command;
        }
        command = new SubmitFormCommand(crfScheduleId, genericRepository, studyParticipantCrfScheduleRepository, proCtcTermRepository, meddraRepository);
        return command;
    }

    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        SubmitFormCommand submitFormCommand = (SubmitFormCommand) errors.getTarget();
        if (errors.hasErrors()) {
            submitFormCommand.setDirection("");
            return super.showForm(request, response, errors);
        }

        if (CrfStatus.COMPLETED.equals(submitFormCommand.getSchedule().getStatus())) {
            return showConfirmationPage(submitFormCommand);
        }
        
        ModelAndView mv;
        int currentPageIndex = 0;
        //Fetch next or previous page number of the page to be rendered using commandObject.
        currentPageIndex = submitFormCommand.getNewPageIndex();
        if (currentPageIndex == submitFormCommand.getAddMoreQuestionPageIndex()) {
            mv = showForm(request, errors, "");
            mv.setView(new RedirectView("addMorequestion"));
            return mv;
        }

        if (currentPageIndex == submitFormCommand.getAddMoreQuestionPageIndex()){ // && submitFormCommand.getIsEq5dCrf()) {
        	return showForm(request, errors, getReviewView());
        }
        
        if (currentPageIndex >= submitFormCommand.getReviewPageIndex()) {
            return showForm(request, errors, getReviewView());
        }

        return showForm(request, errors, getFormView());
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }


    @Required
    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }

    @Required
    public void setMeddraRepository(MeddraRepository meddraRepository) {
        this.meddraRepository = meddraRepository;
    }

    private String getReviewView() {
        return "form/reviewFormSubmission";
    }


    @Override
    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
        SubmitFormCommand submitFormCommand = (SubmitFormCommand) command;
        if ("continue".equals(submitFormCommand.getDirection())) {
            int index = 0;
            for (DisplayQuestion displayQuestion : submitFormCommand.getSubmittedPageQuestions()) {
                try {
                    if (displayQuestion.isMandatory()) {
                        String selectedValidValueId = request.getParameter("currentPageQuestions[" + index + "].selectedValidValueId");
                        if (StringUtils.isBlank(selectedValidValueId)) {
                            if (index > 0) {
                                DisplayQuestion mainQuestion = submitFormCommand.getSubmittedPageQuestions().get(0);
                                String selectedValidValueIdForMainQuestion = request.getParameter("currentPageQuestions[0].selectedValidValueId");
                                if (StringUtils.isBlank(selectedValidValueIdForMainQuestion)) {
                                    continue;
                                }
                                ValidValue value = null;
                                if (mainQuestion.isProCtcQuestion()) {
                                    value = genericRepository.findById(ProCtcValidValue.class, new Integer(selectedValidValueIdForMainQuestion));
                                }
                                if (mainQuestion.isMeddraQuestion()) {
                                    value = genericRepository.findById(MeddraValidValue.class, new Integer(selectedValidValueIdForMainQuestion));
                                }
                                if (value == null || value.getDisplayOrder() == 0 || value.getValue().equalsIgnoreCase(" Not applicable")) {
                                    continue;
                                }
                            }
                            errors.reject("crf.MANDATORY_VALUE_MISSING", new Object[] {index + 1} , "Please provide a response for question" + (index + 1));
                        }
                    }
                } catch (Exception
                        e) {
                    e.printStackTrace();
                }
                index++;
            }
        }
    }

	public void setStudyParticipantCrfScheduleRepository(
			StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
		this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
	}
}

