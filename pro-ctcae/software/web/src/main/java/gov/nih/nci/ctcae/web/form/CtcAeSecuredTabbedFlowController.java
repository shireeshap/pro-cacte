package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.AbstractTabbedFlowFormController;
import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FormArmScheduleRepository;
import gov.nih.nci.ctcae.core.repository.MeddraValidValueRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import gov.nih.nci.ctcae.core.repository.secured.*;
import gov.nih.nci.ctcae.core.security.PrivilegeAuthorizationCheck;
import gov.nih.nci.ctcae.web.ControllerTools;
import gov.nih.nci.ctcae.web.clinicalStaff.notifications.ClinicalStaffNotificationPublisher;
import gov.nih.nci.ctcae.web.editor.EnumByNameEditor;
import gov.nih.nci.ctcae.web.editor.RepositoryBasedEditor;
import gov.nih.nci.ctcae.web.participant.ParticipantControllerUtils;
import gov.nih.nci.ctcae.web.security.SecuredTab;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Properties;

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
    protected ClinicalStaffNotificationPublisher proctcaeEventPublisher;
    protected Properties proCtcAEProperties;
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
    protected MeddraValidValueRepository meddraValidValueRepository;

    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest, org.springframework.web.bind.ServletRequestDataBinder)
    */
    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);

        binder.registerCustomEditor(Date.class, ControllerTools.getDateEditor(false));
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
        RepositoryBasedEditor meddraValidValueEditor = new RepositoryBasedEditor(meddraValidValueRepository, MeddraValidValue.class);
        binder.registerCustomEditor(MeddraValidValue.class, meddraValidValueEditor);


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
    protected void postProcessPage(HttpServletRequest request, Object oCommand, Errors errors, int page) throws Exception{
        C command = (C) oCommand;
        try {
			super.postProcessPage(request, oCommand, errors, page);
	        if (!errors.hasErrors() && shouldSave(request, command, getTab(command, page))) {
        		save(command);
	            if (request.getParameter("_target" + page) != null) {
	                request.setAttribute("flashMessage", "save.confirmation");
	            }
	        }
        } catch(org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException e){
    		logger.error(e.getMessage());
    		errors.reject("errors.cannotEditRecordAtThisTime","Another user is currently accessing this record, please try again later.");
    	} catch (Exception e) {
			logger.error("Error while executing postProcessPage:" + e.getMessage());
			throw e;
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

    protected Flow<C> getSecuredFlow(Flow<C> flow) {

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

    @Override
    protected int getInitialPage(HttpServletRequest request, Object command) {
        if (!StringUtils.isBlank(request.getParameter("tab"))) {
            return Integer.parseInt(request.getParameter("tab"));
        }
        return super.getInitialPage(request, command);
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

    @Required
    public void setMeddraValidValueRepository(MeddraValidValueRepository meddraValidValueRepository) {
        this.meddraValidValueRepository = meddraValidValueRepository;
    }

    @Autowired(required=true)
    public void setProctcaeEventPublisher(ClinicalStaffNotificationPublisher proctcaeEventPublisher) {
        this.proctcaeEventPublisher = proctcaeEventPublisher;
        //Storing this in a static location because it no longer is valid once a new Create/EditParticipantController is created
        ParticipantControllerUtils.setClinicalStaffNotificationPublisher(proctcaeEventPublisher);
    }

    public void setProCtcAEProperties(Properties proCtcAEProperties) {
        this.proCtcAEProperties = proCtcAEProperties;
    }


    public ClinicalStaffNotificationPublisher getProctcaeEventPublisher() {
        return proctcaeEventPublisher;
    }
}

