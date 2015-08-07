package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.StaticFlowFactory;
import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.core.security.passwordpolicy.PasswordPolicyServiceImpl;
import gov.nih.nci.ctcae.web.form.CtcAeSecuredTabbedFlowController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

//

/**
 * The Class ParticipantController.
 *
 * @author Harsh Agarwal
 * @created Oct 21, 2008
 */
public class ParticipantController extends CtcAeSecuredTabbedFlowController<ParticipantCommand> {

    /**
     * The participant repository.
     */
    protected ParticipantRepository participantRepository;
    protected UserRepository userRepository;
    protected PasswordPolicyServiceImpl passwordPolicyService;

    private Properties properties;

    public static final String IVRS_BLACKOUT_START_TIME = "ivrs.blackout.start";
    public static final String IVRS_BLACKOUT_END_TIME = "ivrs.blackout.end";
    public static final String PARTICIPANT_ID = "id";

    /**
     * Instantiates a new participant controller.
     */
    protected ParticipantController() {
        super();
        setCommandClass(ParticipantCommand.class);
        Flow<ParticipantCommand> flow = new Flow<ParticipantCommand>("Enter Participant");
        layoutTabs(flow);
        setFlowFactory(new StaticFlowFactory<ParticipantCommand>(flow));
        setAllowDirtyBack(false);
        setAllowDirtyForward(false);

    }

    protected void layoutTabs(final Flow<ParticipantCommand> flow) {
        flow.addTab(new ParticipantDetailsTab());
        flow.addTab(new ParticipantClinicalStaffTab());
        flow.addTab(new ScheduleCrfTab());
        flow.addTab(new ParticipantReviewTab());
    }

    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
    */

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        //TODO: this never gets executed
        ParticipantCommand participantCommand = (ParticipantCommand) command;
        participantCommand.assignStaff();

        participantRepository.saveOrUpdate(participantCommand.getParticipant());
        return showForm(request, errors, "participant/confirmParticipant");
    }

    //
    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
    */

    @Override
    protected Object formBackingObject(final HttpServletRequest request) throws ServletException {
        ParticipantCommand command = new ParticipantCommand();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        command.setPasswordPolicy(passwordPolicyService.getPasswordPolicy(Role.PARTICIPANT));
        command.setAdmin(user.isAdmin());
        command.setReadOnlyUserName(false);
        populateOrganizationsForUser(command);
        String mode = proCtcAEProperties.getProperty("mode.nonidentifying");
        command.setMode(mode);

        if (command.getSelectedOrganization() == null && command.getOrganizationId() != 0) {
            command.setSelectedOrganization((Organization) organizationRepository.findById(command.getOrganizationId()));
        }


        if (command.getSelectedStudyParticipantAssignment() != null) {
            command.setSelectedOrganization(command.getSelectedStudyParticipantAssignment().getStudySite().getOrganization());
        }

        return command;
    }
    
    @Override
    protected String getFormSessionAttributeName() {
    	ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    	String participantId = attr.getRequest().getParameter(PARTICIPANT_ID);
    	if(StringUtils.isNotEmpty(participantId)) {
    		return EditParticipantController.class.getName() + ".FORM.command" + "." + Integer.parseInt(participantId);
    	}
        return EditParticipantController.class.getName() + ".FORM.command";
    }

    @Override
    protected Map<String, Object> referenceData(HttpServletRequest request, Object oCommand, Errors errors, int page) throws Exception {
        Map<String, Object> modelAndView = super.referenceData(request, oCommand, errors, page);
        modelAndView.put("homeModeCount", 0);

        List<Integer> times = new ArrayList<Integer>();
        for (int j = 1; j <= 12; j++) {
            times.add(j);
        }
        List<Integer> minutes = new ArrayList<Integer>();
        for (int i = 0; i < 60; i += 15) {
            minutes.add(i);
        }

        ParticipantCommand command = (ParticipantCommand) oCommand;

        String[] timeZones = TimeZone.getAvailableIDs();
        boolean showTime = false;
        if (command.getParticipant().getStudyParticipantAssignments().size() > 0) {
            for (AppMode appMode : command.getParticipant().getStudyParticipantAssignments().get(0).getSelectedAppModes()) {
                if (appMode.equals(AppMode.IVRS)) {
                    showTime = true;
                }
            }
        }

        String blackoutStartTime = properties.getProperty(IVRS_BLACKOUT_START_TIME);
        String blackoutEndTime = properties.getProperty(IVRS_BLACKOUT_END_TIME);
        // Assigning default value for blackout period
        if (blackoutStartTime == null || blackoutStartTime.equals("") || blackoutEndTime == null || blackoutEndTime.equals("") || !blackoutStartTime.contains(":") || !blackoutEndTime.contains(":")) {
            blackoutStartTime = "21:00";
            blackoutEndTime = "04:59";
        }
        modelAndView.put("blackoutStartTime", blackoutStartTime);
        modelAndView.put("blackoutEndTime", blackoutEndTime);

        modelAndView.put("hours", times);
        modelAndView.put("timezones", timeZones);
        modelAndView.put("minutes", minutes);
        modelAndView.put("showTime", showTime);
        modelAndView.put("hasValidationErrors", errors.hasErrors());

        return modelAndView;

    }


    protected final void populateOrganizationsForUser(ParticipantCommand command) {
        if (!command.isAdmin()) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            ClinicalStaff clinicalStaff = userRepository.findClinicalStaffForUser(user);
            if (clinicalStaff != null) {
                for (OrganizationClinicalStaff organizationClinicalStaff : clinicalStaff.getOrganizationClinicalStaffs()) {
                    command.getClinicalStaffOrgs().add(organizationClinicalStaff.getOrganization());
                }
            }
        }
    }


    /**
     * Sets the participant repository.
     *
     * @param participantRepository the new participant repository
     */
    @Required
    public void setParticipantRepository(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @Override
    protected void save(ParticipantCommand command) {
        participantRepository.saveOrUpdate(command.getParticipant());
    }

    @Override
    protected boolean shouldSave(HttpServletRequest request, ParticipantCommand command, Tab tab) {
        return true;
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Required
    public void setPasswordPolicyService(PasswordPolicyServiceImpl passwordPolicyService) {
        this.passwordPolicyService = passwordPolicyService;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}