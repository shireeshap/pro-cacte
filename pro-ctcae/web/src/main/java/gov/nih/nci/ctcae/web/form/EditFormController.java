package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.web.ControllersUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class EditFormController extends FormController {


    @Override
    protected int getInitialPage(HttpServletRequest request) {
        if (!StringUtils.isBlank(request.getParameter("showFormDetails")) && !StringUtils.isBlank(request.getParameter("crfId"))) {
            if (request.getAttribute("flashMessage") == null) {
                request.setAttribute("flashMessage", "form.version.success");
            }
            return 2;
        }

        if (!StringUtils.isBlank(request.getParameter("copyForm")) && !StringUtils.isBlank(request.getParameter("crfId"))) {
            if (request.getAttribute("flashMessage") == null) {
                request.setAttribute("flashMessage", "form.copy.success");
            }
            return 0;
        }

        return super.getInitialPage(request);


    }


    @Override
    protected void layoutTabs(final Flow flow) {
        flow.addTab(new ReviewFormTab());
        flow.addTab(new SelectStudyForFormTab());
        flow.addTab(new FormDetailsTab());
    }


    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        CreateFormCommand command = (CreateFormCommand) ControllersUtils.getFormCommand(request, this);
        if (command == null) {
            command = new CreateFormCommand();
        }
        CRF crf = finderRepository.findAndInitializeCrf(ServletRequestUtils.getRequiredIntParameter(request, "crfId"));
        if (crf == null) {
            throw new CtcAeSystemException("No form found for given id" + ServletRequestUtils.getRequiredIntParameter(request, "crfId"));
        }
        command.setCrf(crf);
        if (CrfStatus.DRAFT.equals(crf.getStatus())) {
            return command;
        }

        throw new CtcAeSystemException("You can not only edit DRAFT forms. The status of this form is:" + crf.getStatus());


    }

    @Override
    protected ModelAndView processFinish(final HttpServletRequest request, final HttpServletResponse response, final Object command, final BindException errors) throws Exception {
        ModelAndView modelAndView = super.processFinish(request, response, command, errors);
        if (errors.hasErrors()) {
            return modelAndView;
        } else {

            CreateFormCommand createFormCommand = (CreateFormCommand) command;
            RedirectView view = new RedirectView("editForm?crfId=" + createFormCommand.getCrf().getId());
            ModelAndView redirectView = new ModelAndView(view);
            return redirectView;
        }


    }

    @Override
    @SuppressWarnings({"unchecked"})
    protected void postProcessPage(HttpServletRequest request, Object oCommand, Errors errors, int page) throws Exception {
        CreateFormCommand command = (CreateFormCommand) oCommand;
        super.postProcessPage(request, oCommand, errors, page);
        if (!errors.hasErrors() && shouldSave(request, command, getTab(command, page))) {
            save(command);
        }
    }

    protected boolean shouldSave(final HttpServletRequest request, final CreateFormCommand command, final Tab tab) {
        return true;
    }


}