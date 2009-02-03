package gov.nih.nci.ctcae.web.participant;

import javax.servlet.http.HttpServletRequest;


// TODO: Auto-generated Javadoc
/**
 * The Class CreateParticipantController.
 * 
 * @author Harsh Agarwal
 * @created Oct 21, 2008
 */
public class CreateParticipantController extends ParticipantController {

    /**
     * Instantiates a new creates the participant controller.
     */
    public CreateParticipantController() {
        setFormView("participant/createParticipant");
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception {
        ParticipantCommand participantCommand = new ParticipantCommand();
        return participantCommand;
    }

}