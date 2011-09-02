package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserRole;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.web.tools.ObjectTools;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

import java.util.List;

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
     * @param firstName  the first name
     * @param lastName   the last name
     * @param identifier the identifier
     */
    public List<Participant> searchParticipant(String firstName,
                                               String lastName, String identifier, String studyId, String spIdentifier, String siteId) {
        List<Participant> participants = getObjects(firstName, lastName,
                identifier, studyId, spIdentifier, siteId);
        return participants;
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
                                         String identifier, String studyId, String spIdentifier, String siteId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        boolean siteStaff = false;
        boolean leadStaff = false;
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole().equals(Role.SITE_PI) || userRole.getRole().equals(Role.SITE_CRA) || userRole.getRole().equals(Role.NURSE) || userRole.getRole().equals(Role.TREATING_PHYSICIAN)) {
                siteStaff = true;
            } else {
                leadStaff = true;
            }
        }
        ParticipantQuery participantQuery;
        if (leadStaff) {
            participantQuery = new ParticipantQuery();
        } else {
            participantQuery = new ParticipantQuery(true);
        }
//         participantQuery = new ParticipantQuery();

        if (!StringUtils.isBlank(firstName)) {
            participantQuery.filterByParticipantFirstName(firstName);
        }
        if (!StringUtils.isBlank(lastName)) {
            participantQuery.filterByParticipantLastName(lastName);
        }
        if (!StringUtils.isBlank(identifier)) {
            participantQuery.filterByParticipantIdentifier(identifier);
        }
        if (!StringUtils.isBlank(studyId)) {
            participantQuery.filterByStudy(Integer.parseInt(studyId));
        }
        if (!StringUtils.isBlank(spIdentifier)) {
            participantQuery.filterByStudyParticipantIdentifier(spIdentifier);
        }
        if (!StringUtils.isBlank(siteId)) {
            participantQuery.filterBySite(Integer.parseInt(siteId));
        }
        List<Participant> participants = (List<Participant>) participantRepository
                .find(participantQuery);
        return participants;
    }

    public List<Participant> matchParticipantByStudySiteId(final String text, Integer studySiteId, Integer studyId) {
        if (studySiteId != null) {
            List<Participant> participants = participantRepository.findByStudySiteId(text, studySiteId);
            return ObjectTools.reduceAll(participants, "id", "firstName", "lastName", "assignedIdentifier", "displayName");
        } else {
            List<Participant> participants = participantRepository.findByStudyId(text, studyId);
            return ObjectTools.reduceAll(participants, "id", "firstName", "lastName", "assignedIdentifier", "displayName");

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