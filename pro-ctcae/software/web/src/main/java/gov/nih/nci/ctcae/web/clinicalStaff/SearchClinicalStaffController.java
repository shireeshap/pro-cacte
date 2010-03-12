package gov.nih.nci.ctcae.web.clinicalStaff;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.SecurityContextHolder;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.web.participant.ParticipantSearchResultsComparator;

import java.util.*;

//

/**
 * The Class SearchParticipantController.
 *
 * @author Harsh Agarwal
 *         Date: Oct 23, 2008
 */
public class SearchClinicalStaffController extends AbstractController {


    private ClinicalStaffAjaxFacade clinicalStaffAjaxFacade;
    StudyRepository studyRepository;

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("clinicalStaff/searchClinicalStaff");
        String useReqParam = request.getParameter("useReqParam");
        String firstName = "%";
        String lastName = "";
        String identifier = "";
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
            firstName = (String) request.getSession().getAttribute("clinicaStaffSearchFirstName");
            lastName = (String) request.getSession().getAttribute("clinicaStaffSearchLastName");
            identifier = (String) request.getSession().getAttribute("clinicaStaffSearchIdentifier");
        } else {
            firstName = request.getParameter("firstName");
            lastName = request.getParameter("lastName");
            identifier = request.getParameter("identifier");
            request.getSession().setAttribute("clinicaStaffSearchFirstName", firstName);
            request.getSession().setAttribute("clinicaStaffSearchLastName", lastName);
            request.getSession().setAttribute("clinicaStaffSearchIdentifier", identifier);
        }
        modelAndView.addObject("firstName", firstName);
        modelAndView.addObject("lastName", lastName);
        modelAndView.addObject("identifier", identifier);
        if (!StringUtils.isBlank(rowsPerPage)) {
            rowsPerPageInt = Integer.parseInt(rowsPerPage);
        }


        List<ClinicalStaff> clinicalStaffs = clinicalStaffAjaxFacade.searchClinicalStaff(firstName, lastName, identifier);
        int totalRecords = clinicalStaffs.size();
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
            ClinicalStaff clinicalStaff = clinicalStaffs.get(index);
            String sites = "";
            String studies = "";
            for (OrganizationClinicalStaff organizationClinicalStaff : clinicalStaff.getOrganizationClinicalStaffs()) {
                sites += organizationClinicalStaff.getOrganization().getDisplayName() + "<br/>";
            }
            Set<Study> studiesSet = new HashSet<Study>();
            for (OrganizationClinicalStaff site : clinicalStaff.getOrganizationClinicalStaffs()) {
                for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : site.getStudyOrganizationClinicalStaff()) {
                    studiesSet.add(studyOrganizationClinicalStaff.getStudyOrganization().getStudy());
                }
            }
            for (Study study : studiesSet) {
                studies += study.getDisplayName() + "<br>";
            }
            String status = "Status not assigned";
            if (clinicalStaff.getStatus() != null) {
                status = "Effectively <span class=\"" + clinicalStaff.getStatus().getDisplayName() + "\">" + clinicalStaff.getStatus().getDisplayName() + "</span> from " + clinicalStaff.getFormattedDate();
            }

            boolean odc = false;
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            for (OrganizationClinicalStaff site : clinicalStaff.getOrganizationClinicalStaffs()) {
                for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : site.getStudyOrganizationClinicalStaff()) {
                    Study study = studyOrganizationClinicalStaff.getStudyOrganization().getStudy();
                    if (user.isODCOnStudy(study)) {
                        odc = true;
                        break;
                    }
                }
                if (odc) {
                    break;
                }
            }

            String actions = "<a class=\"fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all\" id=\"clinicalStaffActions" + clinicalStaff.getId() + "\"><span class=\"ui-icon ui-icon-triangle-1-s\"></span>Actions</a><script>showPopUpMenuClinicalStaff(\"" + clinicalStaff.getId() + "\",\"" + clinicalStaff.getStatus() + "\",\"" + odc + "\");</script>";
            String[] row = new String[]{clinicalStaff.getLastName(), clinicalStaff.getFirstName(), sites, studies, status, actions};
            displayData.add(row);
        }
        Collections.sort(displayData, new ClinicalStaffSearchResultsComparator(sort, sortDir));
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
    public void setClinicalStaffAjaxFacade(ClinicalStaffAjaxFacade clinicalStaffAjaxFacade) {
        this.clinicalStaffAjaxFacade = clinicalStaffAjaxFacade;
    }

    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }
}
