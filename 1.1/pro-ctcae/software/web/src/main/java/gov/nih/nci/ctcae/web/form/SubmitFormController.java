package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.MeddraValidValue;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.domain.ValidValue;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.MeddraRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//

/**
 * User: Harsh
 * Date: Nov 12, 2008
 * Time: 1:36:54 PM.
 */
public class SubmitFormController extends SimpleFormController {
    private GenericRepository genericRepository;
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
        boolean submit = sCommand.save();
        request.getSession().setAttribute(getFormSessionAttributeName(), sCommand);
        if (submit) {
            return showConfirmationPage(sCommand);
        } else {
            return new ModelAndView(new RedirectView("submit?id=" + sCommand.getSchedule().getId() + "&p=" + sCommand.getNewPageIndex()));
        }

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
        if (command != null && crfScheduleId.equals(command.getSchedule().getId().toString())) {
           // command.lazyInitializeSchedule();
            return command;
        }
        command = new SubmitFormCommand(crfScheduleId, genericRepository, proCtcTermRepository, meddraRepository);
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
        submitFormCommand.setCurrentPageIndex(request.getParameter("p"));
        int currentPageIndex = submitFormCommand.getNewPageIndex();
        if (currentPageIndex == submitFormCommand.getAddQuestionPageIndex()) {
            mv = showForm(request, errors, "");
            mv.setView(new RedirectView("addquestion?p=" + currentPageIndex));
            return mv;
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
                                if (value == null || value.getDisplayOrder() == 0) {
                                    continue;
                                }
                            }
                            errors.reject("MANDATORY_VALUE_MISSING", "Please provide a response for question " + (index + 1));
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
}

