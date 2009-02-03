package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Participant;

import javax.servlet.http.HttpServletRequest;


// TODO: Auto-generated Javadoc
/**
 * The Class EditParticipantController.
 * 
 * @author Harsh Agarwal
 * @created Oct 21, 2008
 */
public class EditParticipantController extends ParticipantController {

    /**
     * Instantiates a new edits the participant controller.
     */
    public EditParticipantController() {
        setFormView("participant/editParticipant");
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception {
        String participantId = request.getParameter("participantId");
        Participant participant = participantRepository.findById(new Integer(participantId));
        ParticipantCommand participantCommand = new ParticipantCommand();
        participantCommand.setParticipant(participant);
        if (participant.getStudyParticipantAssignments().size() > 0) {
            Organization studyOrganization = participant.getStudyParticipantAssignments().get(0).getStudySite().getOrganization();
            String siteName = studyOrganization.getName();
            participantCommand.setSiteId(studyOrganization.getId());
            participantCommand.setSiteName(siteName);
        }

        return participantCommand;
    }


}