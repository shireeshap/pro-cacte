package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.secured.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

//

/**
 * The Class SearchParticipantController.
 *
 * @author Harsh Agarwal
 *         Date: Oct 23, 2008
 */
public class FetchStudyController extends AbstractController {


    private StudyAjaxFacade studyAjaxFacade;
    StudyRepository studyRepository;
    OrganizationRepository organizationRepository;
    
    public static final int rowsPerPageInt = 25;

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

    	ModelAndView modelAndView = new ModelAndView("study/searchStudy");
        String searchText = "%";
        String sort = StringUtils.isBlank(request.getParameter("sort")) ? "shortTitle" : request.getParameter("sort");
        String startIndex = request.getParameter("startIndex") == null?"0":request.getParameter("startIndex");
        String results = request.getParameter("results") == null?"25":request.getParameter("results");
        String dir = request.getParameter("dir");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String[] searchStrings= null;
        searchText = (String) request.getSession().getAttribute("searchText");
        modelAndView.addObject("searchText", searchText);
        if(!StringUtils.isBlank(searchText)){
            searchStrings = searchText.trim().split("\\s+");
        }

        Long totalRecords = studyAjaxFacade.resultCount(searchStrings, sort);
        List<Study> studies = studyAjaxFacade.searchStudies(searchStrings, Integer.parseInt(startIndex), Integer.parseInt(results), sort, dir, totalRecords.intValue());

        Study study;
        SearchStudyWrapper searchStudyWrapper = new SearchStudyWrapper();
        if(studies!=null) {
            searchStudyWrapper.setTotalRecords(totalRecords.intValue());
            searchStudyWrapper.setRecordsReturned(studies.size());
            searchStudyWrapper.setStartIndex(Integer.parseInt(startIndex));
            searchStudyWrapper.setPageSize(rowsPerPageInt);
            searchStudyWrapper.setDir(dir);
            searchStudyWrapper.setSort(sort);


            List<SearchStudyDTO> uniqueStudies = new ArrayList<>();
            for (int index = 0; index < studies.size(); index++) {
                study = studies.get(index);

                SearchStudyDTO studyCommand = new SearchStudyDTO();
                studyCommand.setShortTitle(study.getShortTitle());
                studyCommand.setAssignedIdentifier(study.getAssignedIdentifier());
                studyCommand.setFundingSponsorDisplayName(study.getFundingSponsor().getOrganization().getDisplayName());
                studyCommand.setCoordinatingCenterDisplayName(study.getDataCoordinatingCenter().getOrganization().getDisplayName());


                boolean odcOnStudy = false;
                if (user.isCCA() || user.isAdmin()) {
                    odcOnStudy = false;
                } else {
                    if (user.isODCOnStudy(study)) {
                        odcOnStudy = true;
                    }
                }
                String actions = "<a class='fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all' id='studyActions"
                        + study.getId() + "'"
                        + " onmouseover=\"javascript:showPopUpMenuStudy('"
                        + study.getId()
                        + "','"
                        + odcOnStudy
                        + "');\">"
                        + "<span class=\"ui-icon ui-icon-triangle-1-s\"></span>Actions</a>";

                studyCommand.setActions(actions);

                    uniqueStudies.add(studyCommand);
            }

            searchStudyWrapper.setSearchStudyDTO(new SearchStudyDTO[uniqueStudies.size()]);
            for (int index = 0; index < uniqueStudies.size(); index++) {
                searchStudyWrapper.getSearchStudyDTO()[index] = uniqueStudies.get(index);
            }

        }
        JSONObject jsonObject = JSONObject.fromObject(searchStudyWrapper);  
        Map<String,Object> modelMap = new HashMap<String,Object>();
        modelMap.put("shippedRecordSet", jsonObject);
        return new ModelAndView("jsonView", modelMap);
    }

    @Required
    public void setStudyAjaxFacade(StudyAjaxFacade studyAjaxFacade) {
        this.studyAjaxFacade = studyAjaxFacade;
    }

    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    @Required
    public void setOrganizationRepository(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }
}
