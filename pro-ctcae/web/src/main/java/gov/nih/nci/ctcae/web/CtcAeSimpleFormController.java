package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.repository.CommonRepository;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.web.editor.RepositoryBasedEditor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 */
public class CtcAeSimpleFormController extends SimpleFormController {
    protected final Log log = LogFactory.getLog(getClass());
    protected CommonRepository commonRepository;

    protected ControllerTools controllerTools;

    protected CtcAeSimpleFormController() {
        setBindOnNewForm(true);
    }

    @Override
    protected void initBinder(final HttpServletRequest request, final ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
        binder.registerCustomEditor(Date.class, controllerTools.getDateEditor(true));
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

        RepositoryBasedEditor organizationEditor = new RepositoryBasedEditor(commonRepository, Organization.class);
        binder.registerCustomEditor(Organization.class, organizationEditor);


    }

    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        return new HashMap();
    }


    @Required
    public void setControllerTools(ControllerTools controllerTools) {
        this.controllerTools = controllerTools;
    }


    @Required
    public void setCommonRepository(CommonRepository commonRepository) {
        this.commonRepository = commonRepository;
    }
}
