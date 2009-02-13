package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.web.editor.EnumByNameEditor;
import gov.nih.nci.ctcae.web.editor.RepositoryBasedEditor;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//
/**
 * The Class CtcAeSimpleFormController.
 *
 * @author
 */
public class CtcAeSimpleFormController extends SimpleFormController {


    /**
     * The finder repository.
     */
    protected FinderRepository finderRepository;

    /**
     * The controller tools.
     */
    protected ControllerTools controllerTools;

    /**
     * The web controller validator.
     */
    private WebControllerValidator webControllerValidator;

    /**
     * Instantiates a new ctc ae simple form controller.
     */
    protected CtcAeSimpleFormController() {
        super();
        setBindOnNewForm(true);
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest, org.springframework.web.bind.ServletRequestDataBinder)
     */
    @Override
    protected void initBinder(final HttpServletRequest request, final ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
        binder.registerCustomEditor(Date.class, controllerTools.getDateEditor(true));
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

        RepositoryBasedEditor organizationEditor = new RepositoryBasedEditor(finderRepository, Organization.class);
        binder.registerCustomEditor(Organization.class, organizationEditor);

        RepositoryBasedEditor proCtcValidValueEditor = new RepositoryBasedEditor(finderRepository, ProCtcValidValue.class);
        binder.registerCustomEditor(ProCtcValidValue.class, proCtcValidValueEditor);

        binder.registerCustomEditor(Role.class, new EnumByNameEditor<Role>(Role.class));
        binder.registerCustomEditor(RoleStatus.class, new EnumByNameEditor<RoleStatus>(RoleStatus.class));

        RepositoryBasedEditor studySiteEditor = new RepositoryBasedEditor(finderRepository, StudySite.class);
        binder.registerCustomEditor(StudySite.class, studySiteEditor);

    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.BaseCommandController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException)
     */
    @Override
    protected void onBindAndValidate(HttpServletRequest request, Object o, BindException e) throws Exception {
        super.onBindAndValidate(request, o, e);
        webControllerValidator.validate(request, o, e);

    }

    /**
     * Sets the web controller validator.
     *
     * @param webControllerValidator the new web controller validator
     */
    @Required
    public void setWebControllerValidator(WebControllerValidator webControllerValidator) {
        this.webControllerValidator = webControllerValidator;
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)
     */
    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        return new HashMap();
    }


    /**
     * Sets the controller tools.
     *
     * @param controllerTools the new controller tools
     */
    @Required
    public void setControllerTools(ControllerTools controllerTools) {
        this.controllerTools = controllerTools;
    }


    /**
     * Sets the finder repository.
     *
     * @param finderRepository the new finder repository
     */
    @Required
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }
}
