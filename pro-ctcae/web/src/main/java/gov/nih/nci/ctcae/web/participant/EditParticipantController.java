package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Participant;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Harsh Agarwal
 * @created Oct 21, 2008
 */
public class EditParticipantController extends ParticipantController {
	
	public EditParticipantController() {
		setFormView("participant/editParticipant");
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		String participantId = request.getParameter("participantId");
		Participant participant = participantRepository.findById(new Integer(participantId));
		ParticipantCommand participantCommand = new ParticipantCommand();
		participantCommand.setParticipant(participant);
		return participantCommand;
	}


}