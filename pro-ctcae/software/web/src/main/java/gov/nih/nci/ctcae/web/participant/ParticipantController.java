package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.StaticFlowFactory;
import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.web.form.CtcAeSecuredTabbedFlowController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

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
    protected Properties proCtcAEProperties;

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
        flow.addTab(new ParticipantReviewTab());
    }

    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
    */

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        ParticipantCommand participantCommand = (ParticipantCommand) command;
        participantCommand.assignStaff();
        participantCommand.setParticipant(participantRepository.save(participantCommand.getParticipant()));
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
        command.setAdmin(user.isAdmin());
        command.setReadOnlyUserName(false);
        populateOrganizationsForUser(command);
        String mode = proCtcAEProperties.getProperty("mode.identifier");
        command.setMode(mode);
        return command;
    }

    protected final void populateOrganizationsForUser(ParticipantCommand command) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ClinicalStaff clinicalStaff = userRepository.findClinicalStaffForUser(user);
        for (OrganizationClinicalStaff organizationClinicalStaff : clinicalStaff.getOrganizationClinicalStaffs()) {
            command.getClinicalStaffOrgs().add(organizationClinicalStaff.getOrganization());
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
        command.setParticipant(participantRepository.save(command.getParticipant()));

    }

    @Override
    protected boolean shouldSave(HttpServletRequest request, ParticipantCommand command, Tab tab) {
        return true;
    }

    @Required
    public void setProCtcAEProperties(Properties proCtcAEProperties) {
        this.proCtcAEProperties = proCtcAEProperties;
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}