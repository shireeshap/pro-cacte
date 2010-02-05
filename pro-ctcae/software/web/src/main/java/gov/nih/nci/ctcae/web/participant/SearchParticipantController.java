package gov.nih.nci.ctcae.web.participant;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.beans.factory.annotation.Required;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

//
/**
 * The Class SearchParticipantController.
 *
 * @author Harsh Agarwal
 *         Date: Oct 23, 2008
 */
public class SearchParticipantController extends AbstractController {


    private ParticipantAjaxFacade participantAjaxFacade;

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("participant/searchParticipant");
        String useReqParam = request.getParameter("useReqParam");
        String firstName = "%";
        String lastName = "";
        String identifier = "";
        String study = "";
        Integer rowsPerPageInt = 15;
        String sort = request.getParameter("sort");
        String page = request.getParameter("page");
        String rowsPerPage = request.getParameter("rowsPerPage");
        String sortDir = request.getParameter("sortDir");
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
            if ("asc".equals(sortDir)) {
                sortDir = "desc";
            } else {
                sortDir = "asc";
            }
        }

        if (StringUtils.isBlank(useReqParam)) {
            //leaving blank for now. Will use session parameters if there is a requirement for that
        } else {
            firstName = request.getParameter("firstName");
            lastName = request.getParameter("lastName");
            identifier = request.getParameter("identifier");
            study = request.getParameter("study");
            modelAndView.addObject("firstName", firstName);
            modelAndView.addObject("lastName", lastName);
            modelAndView.addObject("identifier", identifier);
            modelAndView.addObject("study", study);
        }
        if (!StringUtils.isBlank(rowsPerPage)) {
            rowsPerPageInt = Integer.parseInt(rowsPerPage);
        }


        List<Participant> participants = participantAjaxFacade.searchParticipant(firstName, lastName, identifier, study);
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
        for (int index = 0; index < totalRecords; index++) {
            Participant participant = participants.get(index);
            String sites = "";
            String studies = "";
            for (StudyParticipantAssignment studyParticipantAssignment : participant.getStudyParticipantAssignments()) {
                sites += studyParticipantAssignment.getStudySite().getDisplayName() + "<br/>";
            }
            for (StudyParticipantAssignment studyParticipantAssignment : participant.getStudyParticipantAssignments()) {
                studies += studyParticipantAssignment.getStudySite().getStudy().getShortTitle() + "<br/>";
            }
            String cellValue = "<a class=\"fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all\" id=\"participantActions" + participant.getId() + "\"><span class=\"ui-icon ui-icon-triangle-1-s\"></span>Actions</a><script>showPopUpMenuParticipant(\"" + participant.getId() + "\");</script>";

            String[] row = new String[]{participant.getLastName(), participant.getFirstName(), sites, studies, cellValue};
            displayData.add(row);
        }
        Collections.sort(displayData, new ParticipantSearchResultsComparator(sort, sortDir));
        for (int index = 0; index < totalRecords; index++) {
            if (begin <= index && index <= end) {
                displayDataForPage.add(displayData.get(index));
                endDisplay++;
            }
        }
        modelAndView.addObject("searchResults", displayDataForPage);
        modelAndView.addObject("numberOfPages", numberOfPages);
        modelAndView.addObject("page", page);
        modelAndView.addObject("sort", sort);
        modelAndView.addObject("rowsPerPage", rowsPerPage);
        modelAndView.addObject("sortDir", sortDir);
        modelAndView.addObject("totalRecords", totalRecords);
        modelAndView.addObject("begin", begin + 1);
        modelAndView.addObject("end", endDisplay);

        return modelAndView;
    }

    @Required
    public void setParticipantAjaxFacade(ParticipantAjaxFacade participantAjaxFacade) {
        this.participantAjaxFacade = participantAjaxFacade;
    }
}
