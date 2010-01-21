package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.rules.ProCtcAERulesService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.validation.BindException;

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
    private ProCtcAERulesService proCtcAERulesService;

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
        request.getSession().setAttribute(getFormSessionAttributeName(), command);
        if (submit) {
            return new ModelAndView(getSuccessView());
        } else {
            return new ModelAndView(new RedirectView("submit?id=" + sCommand.getSchedule().getId() + "&p=" + sCommand.getCurrentPageIndex()));
        }

    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        SubmitFormCommand command = (SubmitFormCommand) request.getSession().getAttribute(getFormSessionAttributeName());
        String crfScheduleId = request.getParameter("id");
        if (command != null && crfScheduleId.equals(command.getSchedule().getId().toString())) {
            command.lazyInitializeSchedule();
            return command;
        }
        command = new SubmitFormCommand(crfScheduleId, genericRepository, proCtcAERulesService);
        return command;
    }

    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        SubmitFormCommand submitFormCommand = (SubmitFormCommand) errors.getTarget();
        ModelAndView mv;
        submitFormCommand.setCurrentPageIndex(request.getParameter("p"));
        int currentPageIndex = submitFormCommand.getCurrentPageIndex();
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
    public void setProCtcAERulesService(ProCtcAERulesService proCtcAERulesService) {
        this.proCtcAERulesService = proCtcAERulesService;
    }

    private String getReviewView() {
        return "form/reviewFormSubmission";
    }
}

