package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.repository.ParticipantRepository;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mehul Gulati
 * Date: Nov 17, 2008
 */
public class ParticipantInboxController extends CtcAeSimpleFormController {

    private ParticipantRepository participantRepository;

    public ParticipantInboxController() {
        setCommandClass(gov.nih.nci.ctcae.core.domain.Participant.class);
        setFormView("participant/participantInbox");
        setBindOnNewForm(true);
        setSessionForm(true);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String participantId = request.getParameter("participantId");
   //   Participant participantInboxCommand = new Participant();
        Participant participant = participantRepository.findById(new Integer(participantId));
        return participant;
    }


    public void setParticipantRepository(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }
}
