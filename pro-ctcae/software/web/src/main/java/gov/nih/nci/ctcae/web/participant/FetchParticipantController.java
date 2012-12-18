package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.repository.secured.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author mehul
 *         Date: 11/29/11
 */

public class FetchParticipantController extends AbstractController {

    private ParticipantAjaxFacade participantAjaxFacade;
    StudyRepository studyRepository;
    OrganizationRepository organizationRepository;
    protected Properties proCtcAEProperties;
    
    List<Participant> completeParticipantsList = new ArrayList<Participant>();
    public static final int rowsPerPageInt = 25;

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("participant/searchParticipant");
        List<Participant> participantsForCurrentSearch = new ArrayList<Participant>();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String useReqParam = request.getParameter("useReqParam");
        String startIndex = request.getParameter("startIndex");
        String results = request.getParameter("results");
        String sortField = request.getParameter("sort");
        String direction = request.getParameter("dir");
        String searchString = "";
        String[] searchStrings = null;

        if (StringUtils.isBlank(useReqParam)) {
            searchString = (String) request.getSession().getAttribute("ParticipantSearchString");
        } else {
            searchString = request.getParameter("searchString");
            request.getSession().setAttribute("ParticipantSearchString", searchString);
        }
        modelAndView.addObject("searchString", searchString);
        if (!StringUtils.isEmpty(searchString)) {
             searchString.trim();
             searchStrings = searchString.split("\\s+");
        }
        Integer resultsCount = Integer.valueOf(results);
        Integer totalRecords = 0;
        
        totalRecords = participantAjaxFacade.resultCount(searchStrings, Integer.valueOf(startIndex), resultsCount).intValue();
         /*Fetch the records only if totalRecords are determined to be greater than zero */
         if(totalRecords > 0)
        	participantsForCurrentSearch = participantAjaxFacade.searchParticipants(searchStrings, Integer.valueOf(startIndex), resultsCount, sortField, direction);
         else 
        	participantsForCurrentSearch=null;
    	
        
        
//        if(resultsCount > completeParticipantsList.size()){
//        	resultsCount = completeParticipantsList.size();
//        }
//        participantsForCurrentSearch = completeParticipantsList.subList(Integer.valueOf(startIndex), resultsCount);
        //Long totalRecords = participantAjaxFacade.resultCount(searchStrings);

        Participant participant;
        SearchParticipantWrapper searchParticipantWrapper = new SearchParticipantWrapper();
        
        if(participantsForCurrentSearch != null){
	        searchParticipantWrapper.setTotalRecords(Long.valueOf(totalRecords));
	        searchParticipantWrapper.setRecordsReturned(participantsForCurrentSearch.size());
	        searchParticipantWrapper.setStartIndex(Integer.valueOf(startIndex));
	        searchParticipantWrapper.setPageSize(rowsPerPageInt);
	        searchParticipantWrapper.setDir(direction);
	        searchParticipantWrapper.setSearchParticipantDTOs(new SearchParticipantDTO[participantsForCurrentSearch.size()]);
	        SearchParticipantDTO searchParticipantDTO;
	        
	        List<Study> studiesOnWhichUserIsOdc;
	        StudyQuery studyQuery  = new StudyQuery();
	        studyQuery.filterStudiesByUserAndRole(user, Role.ODC);
	        studiesOnWhichUserIsOdc = (List<Study>) studyRepository.find(studyQuery);
	        Study study;
	        boolean odc;
	        for (int index = 0; index < participantsForCurrentSearch.size(); index++) {
	            participant = participantsForCurrentSearch.get(index);
	
	            searchParticipantDTO = new SearchParticipantDTO();
	            searchParticipantDTO.setFirstName(participant.getFirstName());
	            searchParticipantDTO.setLastName(participant.getLastName());
	            searchParticipantDTO.setOrganizationName(participant.getStudyParticipantAssignments().get(0).getStudySite().getOrganization().getName());
	            searchParticipantDTO.setStudyParticipantIdentifier(participant.getStudyParticipantAssignments().get(0).getStudyParticipantIdentifier());
	            searchParticipantDTO.setStudyShortTitle(participant.getStudyParticipantAssignments().get(0).getStudySite().getStudy().getShortTitle());
	
	            odc = false;
	            if (participant.getStudyParticipantAssignments().size() > 0) {
	                study = participant.getStudyParticipantAssignments().get(0).getStudySite().getStudy();
	                if(studiesOnWhichUserIsOdc.contains(study)){
	                	odc = true;
	                }
	            }
	
	            String actions = "<a class='fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all' id='participantActions"
	                    + participant.getId() + "'"
	                    + " onmouseover=\"javascript:showPopUpMenuParticipant('"
	                    + participant.getId()
	                    + "','"
	                    + odc
	                    + "');\">"
	                    + "<span class=\"ui-icon ui-icon-triangle-1-s\"></span>Actions</a>";
	
	
	            searchParticipantDTO.setActions(actions);
	            searchParticipantWrapper.getSearchParticipantDTOs()[index] = searchParticipantDTO;
	        }
        }
        String mode = proCtcAEProperties.getProperty("mode.nonidentifying");

        modelAndView.addObject("mode", mode);
        modelAndView.addObject("totalRecords", totalRecords);
        JSONObject jsonObject = JSONObject.fromObject(searchParticipantWrapper);
        Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put("shippedRecordSet", jsonObject);
        return new ModelAndView("jsonView", modelMap);
    }


    @Required
    public void setParticipantAjaxFacade
            (ParticipantAjaxFacade
                     participantAjaxFacade) {
        this.participantAjaxFacade = participantAjaxFacade;
    }

    @Required
    public void setStudyRepository
            (StudyRepository
                     studyRepository) {
        this.studyRepository = studyRepository;
    }

    @Required
    public void setOrganizationRepository
            (OrganizationRepository
                     organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Required
    public void setProCtcAEProperties
            (Properties
                     proCtcAEProperties) {
        this.proCtcAEProperties = proCtcAEProperties;
    }

}