package gov.nih.nci.ctcae.web.form;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class AdvanceFormController extends FormController {

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        CreateFormCommand command = (CreateFormCommand) super.formBackingObject(request);
        command.setAdvance(Boolean.TRUE);
        return command;


    }
}