package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.QueryStrings;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserRole;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.web.tools.ObjectTools;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;


/**
 * The Class ParticipantAjaxFacade.
 *
 * @author Harsh Agarwal
 * @created Date: Oct 23, 2008
 */
public class ParticipantAjaxFacade {

    private static String OTHER = "sortByFiledOtherThanOrganizationName";
    private static final String ORGANIZATION_NAME = "sortByOrganizationName";

    private ParticipantRepository participantRepository;
    private Log logger = LogFactory.getLog(ParticipantAjaxFacade.class);


    public List<Participant> searchParticipants(String[] searchStrings, Integer startIndex, Integer resultsCount, String sortField, String direction) {
        List<Participant> participants = getParticipantObjects(searchStrings, startIndex, resultsCount, sortField, direction);
        return participants;
    }

    public Long resultCount(String[] searchTexts, Integer startIndex, Integer resultsCount) {
        
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        /* If user privileges are such that he has an empty list of AccessibleObjectIds associated with him, then return 0 as resultCount
    	 * else get the actual resutCount from the database. 
    	*/
        boolean leadStaff = false;
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole().equals(Role.LEAD_CRA) || userRole.getRole().equals(Role.PI)) {
                leadStaff = true;
                break;
            }
        }
        ParticipantQuery participantQuery;
        if (leadStaff) {
            participantQuery = new ParticipantQuery(QueryStrings.PARTICIPANT_QUERY_COUNT, Role.LEAD_CRA, true, OTHER);
        } else {

            participantQuery = new ParticipantQuery(QueryStrings.PARTICIPANT_QUERY_COUNT, Role.SITE_CRA, true, OTHER);
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
                if (index > 1) {
                    participantQuery.filterByMultiwordWildcard(searchTexts, ""+index);
                }
            }
        }
        return participantRepository.findWithCount(participantQuery);
    }

    public List<Participant> getParticipantList() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        boolean leadStaff = false;
        boolean siteStaff = false;
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole().equals(Role.SITE_PI) || userRole.getRole().equals(Role.SITE_CRA) || userRole.getRole().equals(Role.NURSE) || userRole.getRole().equals(Role.TREATING_PHYSICIAN)) {
                siteStaff = true;
            } else {
                leadStaff = true;
            }
        }
        ParticipantQuery participantQuery;
        if (leadStaff) {
            participantQuery = new ParticipantQuery(QueryStrings.PARTICIPANT_QUERY_WITH_JOINS, false);
        } else {
            participantQuery = new ParticipantQuery(QueryStrings.PARTICIPANT_QUERY_WITH_JOINS, true);
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
            participantQuery = new ParticipantQuery(QueryStrings.PARTICIPANT_QUERY_WITH_JOINS, Role.LEAD_CRA, true, OTHER);
        } else if(sortField.equalsIgnoreCase("organizationName")){
        	participantQuery = new ParticipantQuery(QueryStrings.PARTICIPANT_QUERY_WITH_JOINS, Role.SITE_CRA, true,ORGANIZATION_NAME);        	
        }
        else {
            participantQuery = new ParticipantQuery(QueryStrings.PARTICIPANT_QUERY_WITH_JOINS, Role.SITE_CRA, true, OTHER);
        }

        if (sortField.equals("studyParticipantIdentifier")) {
            participantQuery.setSortBy("p.studyParticipantAssignments.studyParticipantIdentifier");
        } else if(sortField.equalsIgnoreCase("organizationName")){
        	participantQuery.setSortBy("ss.organization.name");
        } else if(sortField.equalsIgnoreCase("studyShortTitle")){
        	participantQuery.setSortBy("study.shortTitle");
        } else {
            participantQuery.setSortBy("p." + sortField);
        }
        participantQuery.setSortDirection(direction);
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
            if (index > 1) {
                participantQuery.filterByMultiwordWildcard(searchTexts, ""+index);
            }
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
            participantQuery = new ParticipantQuery(QueryStrings.PARTICIPANT_QUERY_WITH_JOINS, Role.LEAD_CRA, true, OTHER);
        } else {
            participantQuery = new ParticipantQuery(QueryStrings.PARTICIPANT_QUERY_WITH_JOINS, Role.SITE_CRA, true, OTHER);
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