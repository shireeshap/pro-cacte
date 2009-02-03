package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.repository.ParticipantRepository;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class ParticipantInboxController.
 * 
 * @author Mehul Gulati
 * Date: Nov 17, 2008
 */
public class ParticipantInboxController extends CtcAeSimpleFormController {

    /** The participant repository. */
    private ParticipantRepository participantRepository;

    /**
     * Instantiates a new participant inbox controller.
     */
    public ParticipantInboxController() {
        setCommandClass(gov.nih.nci.ctcae.core.domain.Participant.class);
        setFormView("participant/participantInbox");
        setBindOnNewForm(true);
        setSessionForm(true);
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String participantId = request.getParameter("participantId");
   //   Participant participantInboxCommand = new Participant();
        Participant participant = participantRepository.findById(new Integer(participantId));
        return participant;
    }


    /**
     * Sets the participant repository.
     * 
     * @param participantRepository the new participant repository
     */
    public void setParticipantRepository(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }
}
