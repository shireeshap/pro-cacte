package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CrfCreationMode;

import javax.servlet.http.HttpServletRequest;

// TODO: Auto-generated Javadoc
/**
 * The Class BasicFormController.
 * 
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class BasicFormController extends FormController {


    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.web.form.FormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        CreateFormCommand command = (CreateFormCommand) super.formBackingObject(request);
        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        return command;


    }


}
