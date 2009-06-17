package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.security.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//
/**
 * The Class ParticipantInboxController.
 *
 * @author Mehul Gulati
 *         Date: Nov 17, 2008
 */
public class ParticipantInboxController extends CtcAeSimpleFormController {

    /**
     * The participant repository.
     */
    private ParticipantRepository participantRepository;

    /**
     * Instantiates a new participant inbox controller.
     */
    public ParticipantInboxController() {
        super();
        setCommandClass(gov.nih.nci.ctcae.core.domain.Participant.class);
        setBindOnNewForm(true);
        setSessionForm(true);
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ParticipantQuery query = new ParticipantQuery();
        query.filterByUsername(user.getUsername());
        List<Participant> participants = (List<Participant>) participantRepository.find(query);
        if (participants == null || participants.size() != 1) {
            throw new CtcAeSystemException("Can not find participant for username " + user.getUsername());
        }
        return participants.get(0);
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
