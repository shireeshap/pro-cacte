package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class EditFormController extends FormController {

    @Override
    protected Map referenceData(HttpServletRequest request, Object oCommand, Errors errors, int page) throws Exception {

        if (!StringUtils.isBlank(request.getParameter("showFormDetails")) && !StringUtils.isBlank(request.getParameter("crfId"))) {
            if (request.getAttribute("flashMessage") == null) {
                request.setAttribute("flashMessage", "form.version.success");
            }
        }

        if (!StringUtils.isBlank(request.getParameter("copyForm")) && !StringUtils.isBlank(request.getParameter("crfId"))) {
            if (request.getAttribute("flashMessage") == null) {
                request.setAttribute("flashMessage", "form.copy.success");
            }
        }
        return super.referenceData(request, oCommand, errors, page);


    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        CreateFormCommand command = (CreateFormCommand) super.formBackingObject(request);
        CRF crf = command.getCrf();
        if (CrfStatus.DRAFT.equals(crf.getStatus())) {
            return command;
        }

        throw new CtcAeSystemException("You can not only edit DRAFT forms. The status of this form is:" + crf.getStatus());


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