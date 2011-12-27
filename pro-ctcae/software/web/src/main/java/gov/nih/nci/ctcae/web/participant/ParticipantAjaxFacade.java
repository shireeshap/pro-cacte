package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserRole;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.web.tools.ObjectTools;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

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

    public List<Participant> searchParticipants(String[] searchStrings, Integer startIndex, Integer results, String sortField, String direction) {
        List<Participant> participants = getParticipantObjects(searchStrings, startIndex, results, sortField, direction);
        return participants;
    }

    public Long resultCount(String[] searchTexts) {
        ParticipantQuery participantQuery = new ParticipantQuery(true, true);
        if (searchTexts != null) {

            participantQuery.setLeftJoin();
            int index = 0;
            for (String searchText : searchTexts) {
                if (!StringUtils.isBlank(searchText)) {
                    participantQuery.filterByAll(searchText, ""+index);
                    index++;
                }
            }
        }
        return participantRepository.findWithCount(participantQuery);

    }

    public List<Participant> getParticipantObjects(String[] searchTexts, Integer startIndex, Integer results, String sortField, String direction) {
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
            participantQuery = new ParticipantQuery(true, false, false);
        } else {
            participantQuery = new ParticipantQuery(true, false, true);
        }
//
//        if (sortField.equals("studyShortTitle")) {
//            participantQuery.setSortBy("p.studyParticipantAssignments.studySite.study.studyShortTitle");
        if (sortField.equals("studyParticipantIdentifier")) {
            participantQuery.setSortBy("p.studyParticipantAssignments.studyParticipantIdentifier");
        } else {
            participantQuery.setSortBy("p." + sortField);
        }
        participantQuery.setSortDirection(direction);
        participantQuery.setFirstResult(startIndex);
        participantQuery.setMaximumResults(results);
        if (searchTexts != null) {
            participantQuery.setLeftJoin();
            int index = 0;
            for (String searchText : searchTexts) {
                if (!StringUtils.isBlank(searchText)) {
                    participantQuery.filterByAll(searchText, ""+index);
                    index++;
                }
            }
        }
//        if (!StringUtils.isBlank(searchText)) {
//            participantQuery.filterByAll(searchText);
//        }

        List<Participant> participants = (List<Participant>) participantRepository
                .find(participantQuery);
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

        if (StringUtils.isBlank(firstName) && StringUtils.isBlank(lastName) && StringUtils.isBlank(identifier) && StringUtils.isBlank(studyId) && StringUtils.isBlank(spIdentifier) && StringUtils.isBlank(siteId)) {
            participantQuery.setMaximumResults(75);
        }
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