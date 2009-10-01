package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.web.tools.ObjectTools;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

//
/**
 * The Class ParticipantAjaxFacade.
 *
 * @author Harsh Agarwal
 * @created Date: Oct 23, 2008
 */
public class ParticipantAjaxFacade {

    /**
     * The participant repository.
     */
    private ParticipantRepository participantRepository;

    /**
     * Search participant.
     *
     * @param parameterMap the parameter map
     * @param firstName    the first name
     * @param lastName     the last name
     * @param identifier   the identifier
     * @param request      the request
     * @return the string
     */
    public String searchParticipant(Map parameterMap, String firstName,
                                    String lastName, String identifier, HttpServletRequest request) {

        List<Participant> participants = getObjects(firstName, lastName,
                identifier);
        ParticipantTableModel participantTableModel = new ParticipantTableModel();
        String table = participantTableModel.buildParticipantTable(
                parameterMap, participants, request);
        return table;

    }

    /**
     * Gets the objects.
     *
     * @param firstName  the first name
     * @param lastName   the last name
     * @param identifier the identifier
     * @return the objects
     */
    private List<Participant> getObjects(String firstName, String lastName,
                                         String identifier) {
        ParticipantQuery participantQuery = new ParticipantQuery();

        if (firstName != null && !"".equals(firstName)) {
            participantQuery.filterByParticipantFirstName(firstName);
        }
        if (lastName != null && !"".equals(lastName)) {
            participantQuery.filterByParticipantLastName(lastName);
        }
        if (identifier != null && !"".equals(identifier)) {
            participantQuery.filterByParticipantIdentifier(identifier);
        }
        List<Participant> participants = (List<Participant>) participantRepository
                .find(participantQuery);
        return participants;
    }

    public List<Participant> matchParticipantByStudySiteId(final String text, Integer studySiteId, Integer studyId) {
        if (studySiteId != null){
        List<Participant> participants = participantRepository.findByStudySiteId(text, studySiteId);
        return ObjectTools.reduceAll(participants, "id", "firstName", "lastName");
        } else{
            List<Participant> participants = participantRepository.findByStudyId(text, studyId);
            return ObjectTools.reduceAll(participants, "id", "firstName", "lastName");

        }
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

}