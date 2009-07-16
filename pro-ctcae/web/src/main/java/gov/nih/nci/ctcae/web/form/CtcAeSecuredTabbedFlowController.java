package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.AbstractTabbedFlowFormController;
import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.*;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantAssignmentRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.secured.OrganizationClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.core.repository.secured.OrganizationRepository;
import gov.nih.nci.ctcae.core.security.PrivilegeAuthorizationCheck;
import gov.nih.nci.ctcae.web.ControllerTools;
import gov.nih.nci.ctcae.web.editor.EnumByNameEditor;
import gov.nih.nci.ctcae.web.editor.RepositoryBasedEditor;
import gov.nih.nci.ctcae.web.security.SecuredTab;
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
 * @since Nov 5, 2008
 */
@SuppressWarnings({"deprecation"})
public abstract class CtcAeSecuredTabbedFlowController<C> extends AbstractTabbedFlowFormController<C> {

    protected ClinicalStaffRepository clinicalStaffRepository;
    protected StudyParticipantAssignmentRepository studyParticipantAssignmentRepository;
    protected OrganizationClinicalStaffRepository organizationClinicalStaffRepository;
    protected StudyOrganizationRepository studyOrganizationRepository;
    protected ParticipantRepository participantRepository;
    private PrivilegeAuthorizationCheck privilegeAuthorizationCheck;
    private StudyOrganizationClinicalStaffRepository studyOrganizationClinicalStaffRepository;
    /**
     * The organization repository.
     */
    protected OrganizationRepository organizationRepository;

    /**
     * The finder repository.
     */
    protected StudyRepository studyRepository;

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

    private FormArmScheduleRepository formArmScheduleRepository;
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest, org.springframework.web.bind.ServletRequestDataBinder)
     */
    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);

        binder.registerCustomEditor(Date.class, ControllerTools.getDateEditor(true));
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(Study.class, new RepositoryBasedEditor(studyRepository, Study.class));
        binder.registerCustomEditor(Organization.class, new RepositoryBasedEditor(organizationRepository, Organization.class));
        binder.registerCustomEditor(OrganizationClinicalStaff.class, new RepositoryBasedEditor(organizationClinicalStaffRepository, OrganizationClinicalStaff.class));
        binder.registerCustomEditor(Participant.class, new RepositoryBasedEditor(participantRepository, Participant.class));
        binder.registerCustomEditor(ClinicalStaff.class, new RepositoryBasedEditor(clinicalStaffRepository, ClinicalStaff.class));
        binder.registerCustomEditor(CrfItemAllignment.class, new EnumByNameEditor<CrfItemAllignment>(CrfItemAllignment.class));
        binder.registerCustomEditor(RoleStatus.class, new EnumByNameEditor<RoleStatus>(RoleStatus.class));
        binder.registerCustomEditor(StudyParticipantAssignment.class, new RepositoryBasedEditor(studyParticipantAssignmentRepository, StudyParticipantAssignment.class));
        binder.registerCustomEditor(StudyOrganization.class, new RepositoryBasedEditor(studyOrganizationRepository, StudyOrganization.class));
        binder.registerCustomEditor(StudyOrganizationClinicalStaff.class, new RepositoryBasedEditor(studyOrganizationClinicalStaffRepository, StudyOrganizationClinicalStaff.class));
        binder.registerCustomEditor(FormArmSchedule.class, new RepositoryBasedEditor(formArmScheduleRepository, FormArmSchedule.class));
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


    @Override
    @SuppressWarnings({"unchecked"})
    protected void postProcessPage(HttpServletRequest request, Object oCommand, Errors errors, int page) throws Exception {
        C command = (C) oCommand;
        super.postProcessPage(request, oCommand, errors, page);
        if (!errors.hasErrors() && shouldSave(request, command, getTab(command, page))) {
            save(command);
            request.setAttribute("flashMessage", "save.confirmation");
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

    @Override
    public Flow<C> getFlow(C command) {
        Flow<C> cFlow = super.getFlow(command);


        return getSecuredFlow(cFlow);


    }

    private Flow<C> getSecuredFlow(Flow<C> flow) {

        Flow securedFlow = new Flow(flow.getName());

        for (Tab<C> tab : flow.getTabs()) {
            if (tab instanceof SecuredTab) {
                boolean authorize = privilegeAuthorizationCheck.authorize(((SecuredTab) tab).getRequiredPrivilege());
                if (authorize) {
                    securedFlow.addTab(tab);
                } else {
                    logger.debug(String.format("user can not access this secured tab %s", tab.getShortTitle()));
                }
            } else {
                logger.debug(String.format("tab %s is not secured  so returning the tab", tab.getShortTitle()));
                securedFlow.addTab(tab);
            }
        }

        return securedFlow;

    }

    @Override
    public Flow<C> getFlow() {
        Flow<C> cFlow = super.getFlow();
        return getSecuredFlow(cFlow);

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

    public void setClinicalStaffRepository(ClinicalStaffRepository clinicalStaffRepository) {
        this.clinicalStaffRepository = clinicalStaffRepository;
    }

    public void setStudyParticipantAssignmentRepository(StudyParticipantAssignmentRepository studyParticipantAssignmentRepository) {
        this.studyParticipantAssignmentRepository = studyParticipantAssignmentRepository;
    }

    public void setParticipantRepository(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
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

    @Required
    public void setOrganizationClinicalStaffRepository(OrganizationClinicalStaffRepository organizationClinicalStaffRepository) {
        this.organizationClinicalStaffRepository = organizationClinicalStaffRepository;
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

    @Required
    public void setPrivilegeAuthorizationCheck(PrivilegeAuthorizationCheck privilegeAuthorizationCheck) {
        this.privilegeAuthorizationCheck = privilegeAuthorizationCheck;
    }

    @Required
    public void setStudyOrganizationRepository(StudyOrganizationRepository studyOrganizationRepository) {
        this.studyOrganizationRepository = studyOrganizationRepository;
    }

    @Required
    public void setStudyOrganizationClinicalStaffRepository(StudyOrganizationClinicalStaffRepository studyOrganizationClinicalStaffRepository) {
        this.studyOrganizationClinicalStaffRepository = studyOrganizationClinicalStaffRepository;
    }

    @Required
    public void setFormArmScheduleRepository(FormArmScheduleRepository formArmScheduleRepository) {
        this.formArmScheduleRepository = formArmScheduleRepository;
    }
}
