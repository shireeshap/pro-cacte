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
        String firstName = "%";
        String lastName = "";
        String identifier = "";
        String studyId = "";
        String spIdentifier = "";
        String siteId = "";
        Integer rowsPerPageInt = 15;
        String sort = request.getParameter("sort");
        String page = request.getParameter("page");
        String rowsPerPage = request.getParameter("rowsPerPage");
        String sortDir = request.getParameter("sortDir");
        String searchString = "";
        if (StringUtils.isBlank(sort)) {
            sort = "lastName";
        }
        if (StringUtils.isBlank(page)) {
            page = "1";
        }
        if (StringUtils.isBlank(rowsPerPage)) {
            rowsPerPage = "15";
        }
        if (StringUtils.isBlank(sortDir)) {
            sortDir = "asc";
        } else {
            String doSort = request.getParameter("doSort");
            if ("true".equals(doSort)) {
                if ("asc".equals(sortDir)) {
                    sortDir = "desc";
                } else {
                    sortDir = "asc";
                }
            }
        }

        if (StringUtils.isBlank(useReqParam)) {
            searchString = (String) request.getSession().getAttribute("ParticipantSearchString");
        } else {
            searchString = request.getParameter("searchString");
            request.getSession().setAttribute("ParticipantSearchString", searchString);
        }

        modelAndView.addObject("searchString", searchString);
        if (!StringUtils.isBlank(studyId)) {
            modelAndView.addObject("study", studyRepository.findById(Integer.parseInt(studyId)));
        }
        if (!StringUtils.isBlank(siteId)) {
            modelAndView.addObject("studySite", organizationRepository.findById(Integer.parseInt(siteId)));
        }
        if (!StringUtils.isBlank(rowsPerPage)) {
            rowsPerPageInt = Integer.parseInt(rowsPerPage);
        }


//        List<Participant> participants = participantAjaxFacade.searchParticipant(firstName, lastName, identifier, studyId, spIdentifier, siteId);
        List<Participant> participants = participantAjaxFacade.searchParticipants(searchString);
        int totalRecords = participants.size();
        int numberOfPages = totalRecords / rowsPerPageInt;
        if (totalRecords % rowsPerPageInt > 0) {
            numberOfPages = numberOfPages + 1;
        }

        int pageInt = Integer.parseInt(page);
        if (pageInt > numberOfPages) {
            page = "1";
            pageInt = 1;
        }
        int begin = ((pageInt - 1) * rowsPerPageInt);
        int end = (pageInt * rowsPerPageInt) - 1;
        int endDisplay = begin;
        List<String[]> displayData = new ArrayList<String[]>();
        List<String[]> displayDataForPage = new ArrayList<String[]>();

        List<SearchParticipantDTO> jsonListOfObjects = new ArrayList<SearchParticipantDTO>();
        Participant participant;
        SearchParticipantWrapper searchParticipantWrapper = new SearchParticipantWrapper();
        searchParticipantWrapper.setTotalRecords(totalRecords);
        searchParticipantWrapper.setRecordsReturned(20);
        searchParticipantWrapper.setStartIndex(0);
        searchParticipantWrapper.setSearchParticipantDTOs(new SearchParticipantDTO[totalRecords]);
        SearchParticipantDTO searchParticipantDTO;
        for (int index = 0; index < totalRecords; index++) {
            participant = participants.get(index);

            searchParticipantDTO = new SearchParticipantDTO();
            searchParticipantDTO.setFirstName(participant.getFirstName());
            searchParticipantDTO.setLastName(participant.getLastName());
            searchParticipantDTO.setOrganizationName(participant.getStudyParticipantAssignments().get(0).getStudySite().getOrganization().getName());
            searchParticipantDTO.setStudyParticipantIdentifier(participant.getStudyParticipantAssignments().get(0).getStudyParticipantIdentifier());
            searchParticipantDTO.setStudyShortTitle(participant.getStudyParticipantAssignments().get(0).getStudySite().getStudy().getShortTitle());

//            String sites = "";
//            String studies = "";
//            for (StudyParticipantAssignment studyParticipantAssignment : participant.getStudyParticipantAssignments()) {
//                sites += studyParticipantAssignment.getStudySite().getDisplayName() + "<br/>";
//            }
//            for (StudyParticipantAssignment studyParticipantAssignment : participant.getStudyParticipantAssignments()) {
//                studies += studyParticipantAssignment.getStudySite().getStudy().getShortTitle() + "<br/>";
//            }
            boolean odc = false;
            if (participant.getStudyParticipantAssignments().size() > 0) {
                Study study = participant.getStudyParticipantAssignments().get(0).getStudySite().getStudy();
                User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                odc = user.isODCOnStudy(study);
            }

            String actions = "<a class=\"fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all\" id=\"participantActions" + participant.getId() + "\"><span class=\"ui-icon ui-icon-triangle-1-s\"></span>Actions</a><script>showPopUpMenuParticipant(\"" + participant.getId() + "\",\"" + odc + "\");</script>";
            searchParticipantDTO.setActions(actions);
            searchParticipantWrapper.getSearchParticipantDTOs()[index] = searchParticipantDTO;
//            String[] row = new String[]{participant.getLastName(), participant.getFirstName(), sites, studies, actions, participant.getStudyParticipantIdentifier()};
//            displayData.add(row);

//        Collections.sort(displayData, new ParticipantSearchResultsComparator(sort, sortDir));
//        for (int index = 0; index < totalRecords; index++) {
//            if (begin <= index && index <= end) {
//                displayDataForPage.add(displayData.get(index));
//                endDisplay++;
        }

        String mode = proCtcAEProperties.getProperty("mode.nonidentifying");

        modelAndView.addObject("mode", mode);
        modelAndView.addObject("searchResults", displayDataForPage);
        modelAndView.addObject("numberOfPages", numberOfPages);
        modelAndView.addObject("page", page);
        modelAndView.addObject("sort", sort);
        modelAndView.addObject("rowsPerPage", rowsPerPage);
        modelAndView.addObject("sortDir", sortDir);
        modelAndView.addObject("totalRecords", totalRecords);
        modelAndView.addObject("begin", begin + 1);
        modelAndView.addObject("end", endDisplay);


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