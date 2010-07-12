package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

//
/**
 * The Class SearchParticipantController.
 *
 * @author Harsh Agarwal
 *         Date: Oct 23, 2008
 */
public class SearchParticipantController extends AbstractController {


    private ParticipantAjaxFacade participantAjaxFacade;
    StudyRepository studyRepository;
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
            firstName = (String) request.getSession().getAttribute("ParticipantSearchFirstName");
            lastName = (String) request.getSession().getAttribute("ParticipantSearchLastName");
            identifier = (String) request.getSession().getAttribute("ParticipantSearchIdentifier");
            studyId = (String) request.getSession().getAttribute("ParticipantSearchStudyId");
            spIdentifier = (String) request.getSession().getAttribute("ParticipantSearchSpIdentifier");
        } else {
            firstName = request.getParameter("firstName");
            lastName = request.getParameter("lastName");
            identifier = request.getParameter("identifier");
            studyId = request.getParameter("study");
            spIdentifier = request.getParameter("spIdentifier");
            request.getSession().setAttribute("ParticipantSearchFirstName", firstName);
            request.getSession().setAttribute("ParticipantSearchLastName", lastName);
            request.getSession().setAttribute("ParticipantSearchIdentifier", identifier);
            request.getSession().setAttribute("ParticipantSearchStudyId", studyId);
            request.getSession().setAttribute("ParticipantSearchSpIdentifier", spIdentifier);
        }
        modelAndView.addObject("firstName", firstName);
        modelAndView.addObject("lastName", lastName);
        modelAndView.addObject("identifier", identifier);
        modelAndView.addObject("spIdentifier", spIdentifier);
        if (!StringUtils.isBlank(studyId)) {
            modelAndView.addObject("study", studyRepository.findById(Integer.parseInt(studyId)));
        }
        if (!StringUtils.isBlank(rowsPerPage)) {
            rowsPerPageInt = Integer.parseInt(rowsPerPage);
        }


        List<Participant> participants = participantAjaxFacade.searchParticipant(firstName, lastName, identifier, studyId, spIdentifier);
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
            boolean isODC = false;
            Participant participant = participants.get(index);
            String sites = "";
            String studies = "";
            for (StudyParticipantAssignment studyParticipantAssignment : participant.getStudyParticipantAssignments()) {
                sites += studyParticipantAssignment.getStudySite().getDisplayName() + "<br/>";
            }
            for (StudyParticipantAssignment studyParticipantAssignment : participant.getStudyParticipantAssignments()) {
                studies += studyParticipantAssignment.getStudySite().getStudy().getShortTitle() + "<br/>";
            }
            boolean odc = false;
            if (participant.getStudyParticipantAssignments().size() > 0) {
                Study study = participant.getStudyParticipantAssignments().get(0).getStudySite().getStudy();
                User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                odc = user.isODCOnStudy(study);
            }

            String actions = "<a class=\"fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all\" id=\"participantActions" + participant.getId() + "\"><span class=\"ui-icon ui-icon-triangle-1-s\"></span>Actions</a><script>showPopUpMenuParticipant(\"" + participant.getId() + "\",\"" + odc + "\");</script>";

            String[] row = new String[]{participant.getLastName(), participant.getFirstName(), sites, studies, actions, participant.getStudyParticipantIdentifier()};
            displayData.add(row);
        }
        Collections.sort(displayData, new ParticipantSearchResultsComparator(sort, sortDir));
        for (int index = 0; index < totalRecords; index++) {
            if (begin <= index && index <= end) {
                displayDataForPage.add(displayData.get(index));
                endDisplay++;
            }
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

        return modelAndView;
    }

    @Required
    public void setParticipantAjaxFacade(ParticipantAjaxFacade participantAjaxFacade) {
        this.participantAjaxFacade = participantAjaxFacade;
    }

    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    @Required
    public void setProCtcAEProperties(Properties proCtcAEProperties) {
        this.proCtcAEProperties = proCtcAEProperties;
    }
}
