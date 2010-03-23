package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.web.ListValues;
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

//
/**
 * The Class SearchParticipantController.
 *
 * @author Harsh Agarwal
 *         Date: Oct 23, 2008
 */
public class SearchStudyController extends AbstractController {


    private StudyAjaxFacade studyAjaxFacade;
    StudyRepository studyRepository;

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("study/searchStudy");
        String useReqParam = request.getParameter("useReqParam");
        String searchType = "shortTitle";
        String searchText = "%";
        Integer rowsPerPageInt = 15;
        String sort = request.getParameter("sort");
        String page = request.getParameter("page");
        String rowsPerPage = request.getParameter("rowsPerPage");
        String sortDir = request.getParameter("sortDir");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (StringUtils.isBlank(sort)) {
            sort = "shortTitle";
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
            searchType = (String) request.getSession().getAttribute("StudySearchSearchType");
            searchText = (String) request.getSession().getAttribute("StudySearchSearchText");

        } else {
            searchType = request.getParameter("searchType");
            searchText = request.getParameter("searchText");
            request.getSession().setAttribute("StudySearchSearchType", searchType);
            request.getSession().setAttribute("StudySearchSearchText", searchText);
        }
        modelAndView.addObject("searchType", searchType);
        modelAndView.addObject("searchText", searchText);
        if (!StringUtils.isBlank(rowsPerPage)) {
            rowsPerPageInt = Integer.parseInt(rowsPerPage);
        }


        List<Study> studies = studyAjaxFacade.searchStudies(searchType, searchText);
        int totalRecords = studies.size();
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
            Study study = studies.get(index);
            boolean odcOnStudy = user.isODCOnStudy(study);
            String actions = "<a class=\"fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all\" id=\"studyActions" + study.getId() + "\"><span class=\"ui-icon ui-icon-triangle-1-s\"></span>Actions</a><script>showPopUpMenuStudy('" + study.getId() + "','" + odcOnStudy + "');</script>";
            String[] row = new String[]{study.getAssignedIdentifier(), study.getShortTitle(), study.getFundingSponsor().getOrganization().getDisplayName(), study.getDataCoordinatingCenter().getOrganization().getDisplayName(), actions};
            displayData.add(row);
        }
        Collections.sort(displayData, new StudySearchResultsComparator(sort, sortDir));
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
        ListValues listValues = new ListValues();
        modelAndView.addObject("searchCriteria", listValues.getStudySearchType());


        return modelAndView;
    }

    @Required
    public void setStudyAjaxFacade(StudyAjaxFacade studyAjaxFacade) {
        this.studyAjaxFacade = studyAjaxFacade;
    }

    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }
}
