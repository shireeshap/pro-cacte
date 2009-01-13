package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.web.ControllersUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class CreateFormController extends FormController {

    @Override
    protected int getInitialPage(HttpServletRequest request) {
        if (!StringUtils.isBlank(request.getParameter("studyId"))) {
            return FORM_DETAILS_PAGE_NUMBER;
        }
        return super.getInitialPage(request);


    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        CreateFormCommand command = (CreateFormCommand) ControllersUtils.getFormCommand(request, this);
        if (command == null) {
            command = new CreateFormCommand();

        }
////        else {
////            request.setAttribute("flashMessage", "You were already updating one form. Do you want to  resume it or discard it.");
////        }
        //remove this line
        command = new CreateFormCommand();
        if (!StringUtils.isBlank(request.getParameter("studyId"))) {
            command.getCrf().setStudy(studyRepository.findById(Integer.parseInt(request.getParameter("studyId"))));
        }
        return command;


    }

}
