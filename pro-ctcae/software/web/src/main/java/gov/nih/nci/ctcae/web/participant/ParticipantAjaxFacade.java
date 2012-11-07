package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.QueryStrings;
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
    private static String OTHER = "every_case_other_than_organization";
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

    public List<Participant> searchParticipants(String[] searchStrings, Integer startIndex, Integer resultsCount, String sortField, String direction) {
        List<Participant> participants = getParticipantObjects(searchStrings, startIndex, resultsCount, sortField, direction);
        return participants;
    }

    public Long resultCount(String[] searchTexts, Integer startIndex, Integer resultsCount) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean leadStaff = false;
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole().equals(Role.LEAD_CRA) || userRole.getRole().equals(Role.PI)) {
                leadStaff = true;
                break;
            }
        }
        ParticipantQuery participantQuery;
        if (leadStaff) {
            participantQuery = new ParticipantQuery(true, Role.LEAD_CRA, true);
        } else {
            participantQuery = new ParticipantQuery(true, Role.SITE_CRA, true);
        }
        participantQuery.setFirstResult(startIndex);
        if(resultsCount != null){
          participantQuery.setMaximumResults(resultsCount);
        }

        if (searchTexts != null) {
            int index = 0;
            for (String searchText : searchTexts) {
                if (!StringUtils.isBlank(searchText)) {
                    participantQuery.filterByAll(searchText, "" + index);
                    index++;
                }
            }
        }
        return participantRepository.findWithCount(participantQuery);
    }

    public List<Participant> getParticipantList() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        boolean siteStaff = true;
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
        List<Participant> participants = (List<Participant>) participantRepository
                .find(participantQuery);
        return  participants;
    }

    public List<Participant> getParticipantObjects(String[] searchTexts, Integer startIndex, Integer resultsCount, String sortField, String direction) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        boolean leadStaff = false;
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole().equals(Role.LEAD_CRA) || userRole.getRole().equals(Role.PI)) {
            	leadStaff = true;
            	break;
            } 
        }
        ParticipantQuery participantQuery;
        if (leadStaff) {
            participantQuery = new ParticipantQuery(Role.LEAD_CRA, true, OTHER);
        } else if(sortField.equalsIgnoreCase("organizationName")){
        	participantQuery = new ParticipantQuery(Role.SITE_CRA, true,"organizationName");        	
        }
        else {
            participantQuery = new ParticipantQuery(Role.SITE_CRA, true, OTHER);
        }

        if (sortField.equals("studyParticipantIdentifier")) {
            participantQuery.setSortBy("p.studyParticipantAssignments.studyParticipantIdentifier");
        } else if(sortField.equalsIgnoreCase("organizationName")){
        	participantQuery.setSortBy("ss.organization.name");
        }else if(sortField.equalsIgnoreCase("studyShortTitle")){
        	participantQuery.setSortBy("study.shortTitle");
        }else {
            participantQuery.setSortBy("p." + sortField);
        }
        participantQuery.setSortDirection(direction);
        participantQuery.setFirstResult(startIndex);
        if(resultsCount != null){
          participantQuery.setMaximumResults(resultsCount);
        }
        if (searchTexts != null) {
            //participantQuery.setLeftJoin();
            int index = 0;
            for (String searchText : searchTexts) {
                if (!StringUtils.isBlank(searchText)) {
                    participantQuery.filterByAll(searchText, "" + index);
                    index++;
                }
            }
        }

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
            participantQuery = new ParticipantQuery(false);
        } else {
            participantQuery = new ParticipantQuery(true);
        }

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
            participantQuery = new ParticipantQuery(Role.LEAD_CRA, true, OTHER);
        } else {
            participantQuery = new ParticipantQuery(Role.SITE_CRA, true, OTHER);
        }
        participantQuery.filterByAll(text, "" + 0);
        if(studySiteId != null){
            participantQuery.filterByStudySite(studySiteId);
        }
        if(studyId != null){
        	participantQuery.filterByStudy(studyId);
        }
        List<Participant> participants = (List<Participant>) participantRepository.find(participantQuery);
        return ObjectTools.reduceAll(participants, "id", "firstName", "lastName", "assignedIdentifier", "displayName");

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