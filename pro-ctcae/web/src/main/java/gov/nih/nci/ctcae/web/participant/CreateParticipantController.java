package gov.nih.nci.ctcae.web.participant;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Harsh Agarwal
 * @created Oct 21, 2008
 */
public class CreateParticipantController extends ParticipantController {

    public CreateParticipantController() {
        setFormView("participant/createParticipant");
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception {
        ParticipantCommand participantCommand = new ParticipantCommand();
        return participantCommand;
    }

}