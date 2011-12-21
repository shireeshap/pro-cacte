package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.secured.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author mehul
 *         Date: 11/29/11
 */

public class FetchParticipantController extends AbstractController {

    private ParticipantAjaxFacade participantAjaxFacade;
    StudyRepository studyRepository;
    OrganizationRepository organizationRepository;
    protected Properties proCtcAEProperties;

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("participant/searchParticipant");
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

        List<Participant> participants = participantAjaxFacade.searchParticipants(searchStrings, Integer.valueOf(startIndex), Integer.valueOf(results), sortField, direction);
        Long totalRecords = participantAjaxFacade.resultCount(searchStrings);
//        List<Participant> sortedParticipants = participantAjaxFacade.getSortedParticipants();

        Participant participant;
        SearchParticipantWrapper searchParticipantWrapper = new SearchParticipantWrapper();
        searchParticipantWrapper.setTotalRecords(totalRecords);
        searchParticipantWrapper.setRecordsReturned(25);
        searchParticipantWrapper.setStartIndex(Integer.valueOf(startIndex));
        searchParticipantWrapper.setPageSize(25);
        searchParticipantWrapper.setDir("asc");
        searchParticipantWrapper.setSearchParticipantDTOs(new SearchParticipantDTO[participants.size()]);
        SearchParticipantDTO searchParticipantDTO;
        for (int index = 0; index < participants.size(); index++) {
            participant = participants.get(index);

            searchParticipantDTO = new SearchParticipantDTO();
            searchParticipantDTO.setFirstName(participant.getFirstName());
            searchParticipantDTO.setLastName(participant.getLastName());
            searchParticipantDTO.setOrganizationName(participant.getStudyParticipantAssignments().get(0).getStudySite().getOrganization().getName());
            searchParticipantDTO.setStudyParticipantIdentifier(participant.getStudyParticipantAssignments().get(0).getStudyParticipantIdentifier());
            searchParticipantDTO.setStudyShortTitle(participant.getStudyParticipantAssignments().get(0).getStudySite().getStudy().getShortTitle());

            boolean odc = false;
            if (participant.getStudyParticipantAssignments().size() > 0) {
                Study study = participant.getStudyParticipantAssignments().get(0).getStudySite().getStudy();
                User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                odc = user.isODCOnStudy(study);
            }

            String actions = "<a class=\"fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all\" id=\"participantActions" + participant.getId() + "\"><span class=\"ui-icon ui-icon-triangle-1-s\"></span>Actions</a><script>showPopUpMenuParticipant(\"" + participant.getId() + "\",\"" + odc + "\");</script>";
            searchParticipantDTO.setActions(actions);
            searchParticipantWrapper.getSearchParticipantDTOs()[index] = searchParticipantDTO;
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