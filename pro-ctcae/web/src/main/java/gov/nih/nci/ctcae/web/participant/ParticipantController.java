package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.StaticFlowFactory;
import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.web.form.CtcAeSecuredTabbedFlowController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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


    private static final String PARTICIPANT_ID = "id";

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
        String id = request.getParameter(PARTICIPANT_ID);

        ParticipantCommand command = new ParticipantCommand();
        if (!StringUtils.isBlank(id)) {
            Participant participant = participantRepository.findById(Integer.valueOf(id));
            participant.getUser().setConfirmPassword(participant.getUser().getPassword());
            for (UserRole userRole : participant.getUser().getUserRoles()) {
                userRole.getId();
            }
            for (StudyParticipantAssignment studyParticipantAssignment : participant.getStudyParticipantAssignments()) {
                StudyOrganization studySite = studyParticipantAssignment.getStudySite();
                studySite.getStudy().getCrfs();
                studySite.getStudy().getStudySponsor();
            }

            command.setParticipant(participant);

            if (participant.getStudyParticipantAssignments().size() > 0) {
                Organization studyOrganization = participant.getStudyParticipantAssignments().get(0).getStudySite().getOrganization();
                String siteName = studyOrganization.getName();
                command.setOrganizationId(studyOrganization.getId());
                command.setSiteName(siteName);
            }
        }
        return command;
    }


    /**
     * Sets the participant repository.
     *
     * @param participantRepository the new participant repository
     */
    @Required
    public void setParticipantRepository(
            ParticipantRepository participantRepository) {
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
}