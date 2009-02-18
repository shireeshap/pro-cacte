package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.AbstractTabbedFlowFormController;
import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.web.ControllerTools;
import gov.nih.nci.ctcae.web.editor.EnumByNameEditor;
import gov.nih.nci.ctcae.web.editor.RepositoryBasedEditor;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

//
/**
 * The Class CtcAeTabbedFlowController.
 *
 * @author Saurabh Agrawal
 * @crated Nov 5, 2008
 */
public abstract class CtcAeTabbedFlowController<C extends Object> extends AbstractTabbedFlowFormController<C> {

    /**
     * The study repository.
     */
    protected StudyRepository studyRepository;

    /**
     * The organization repository.
     */
    protected OrganizationRepository organizationRepository;

    /**
     * The finder repository.
     */
    protected FinderRepository finderRepository;

    /**
     * The pro ctc question repository.
     */
    protected ProCtcQuestionRepository proCtcQuestionRepository;

    /**
     * The controller tools.
     */
    protected ControllerTools controllerTools;

    /**
     * The web controller validator.
     */
    private WebControllerValidator webControllerValidator;

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest, org.springframework.web.bind.ServletRequestDataBinder)
     */
    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);

        binder.registerCustomEditor(Date.class, controllerTools.getDateEditor(true));
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

        binder.registerCustomEditor(Study.class, new RepositoryBasedEditor(finderRepository, Study.class));

        binder.registerCustomEditor(Organization.class, new RepositoryBasedEditor(finderRepository, Organization.class));

        binder.registerCustomEditor(StudyOrganization.class, new RepositoryBasedEditor(finderRepository, StudyOrganization.class));
        binder.registerCustomEditor(SiteClinicalStaff.class, new RepositoryBasedEditor(finderRepository, SiteClinicalStaff.class));

        binder.registerCustomEditor(Participant.class, new RepositoryBasedEditor(finderRepository, Participant.class));

        binder.registerCustomEditor(ClinicalStaff.class, new RepositoryBasedEditor(finderRepository, ClinicalStaff.class));

        binder.registerCustomEditor(CrfItemAllignment.class, new EnumByNameEditor<CrfItemAllignment>(CrfItemAllignment.class));

        binder.registerCustomEditor(RoleStatus.class, new EnumByNameEditor<RoleStatus>(RoleStatus.class));


    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, int)
     */
    @Override
    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
        super.onBindAndValidate(request, command, errors, page);
        if (validate()) {
            webControllerValidator.validate(request, command, errors);
        }
    }

    protected boolean shouldSave(HttpServletRequest request, C command) {
        return false;
    }


    @Override
    @SuppressWarnings({"unchecked"})
    protected void postProcessPage(HttpServletRequest request, Object oCommand, Errors errors, int page) throws Exception {
        C command = (C) oCommand;
        super.postProcessPage(request, oCommand, errors, page);
        if (!errors.hasErrors() && shouldSave(request, command, getTab(command, page))) {
            save(command);
        }
    }

    protected void save(C command) {


    }


    /**
     * Should save.
     *
     * @param request the request
     * @param command the command
     * @param tab     the tab
     * @return true, if successful
     */
    protected boolean shouldSave(final HttpServletRequest request, final C command, final Tab tab) {
        return false;
    }

    /**
     * Validate.
     *
     * @return true, if successful
     */
    protected boolean validate() {
        return true;
    }

    /**
     * Sets the organization repository.
     *
     * @param organizationRepository the new organization repository
     */
    @Required
    public void setOrganizationRepository(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
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
     * Sets the web controller validator.
     *
     * @param webControllerValidator the new web controller validator
     */
    @Required
    public void setWebControllerValidator(WebControllerValidator webControllerValidator) {
        this.webControllerValidator = webControllerValidator;
    }

    /**
     * Sets the study repository.
     *
     * @param studyRepository the new study repository
     */
    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    /**
     * Sets the pro ctc question repository.
     *
     * @param proCtcQuestionRepository the new pro ctc question repository
     */
    @Required
    public void setProCtcQuestionRepository(ProCtcQuestionRepository proCtcQuestionRepository) {
        this.proCtcQuestionRepository = proCtcQuestionRepository;
    }
}
