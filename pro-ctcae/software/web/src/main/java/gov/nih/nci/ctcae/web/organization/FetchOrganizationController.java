package gov.nih.nci.ctcae.web.organization;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;


/**
 * @author Amey
 * FetchOrganizationController class.
 * Invoked via searchOrganization screen. 
 * Populates the result on dataTable in jsp page.
 */
public class FetchOrganizationController extends AbstractController {
   private OrganizationAjaxFacade organizationalAjaxFacade;
    private StudyRepository studyRepository;
    private Properties proCtcAEProperties;
	private static String ORGANIZATION_SEARCH_STRING = "seachString";
	private final static String ORGANIZATION_NAME_SORT = "organizationName";
	private final static String STUDY_SORT = "study";
	
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("organization/searchOrganization");
        String startIndex = request.getParameter("startIndex");
        Integer startIndexInt = Integer.parseInt(startIndex);

        String results = request.getParameter("results");
        Integer resultsInt = Integer.parseInt(results);
        String sort = request.getParameter("sort");
        String dir = request.getParameter("dir");
        String[] searchStrings= null;

        String searchString = (String) request.getSession().getAttribute(ORGANIZATION_SEARCH_STRING);
        if(!StringUtils.isBlank(searchString)){
            searchString.trim();
            searchStrings = searchString.split("\\s+");
        }
        
        Long totalRecords = organizationalAjaxFacade.resultCount(searchStrings);
        List<Organization>  searchedOrganizations = organizationalAjaxFacade.searchOrganizations(searchStrings,startIndexInt,resultsInt,sort,dir);

        SearchOrganizationWrapper searchOrganizationWrapper = new SearchOrganizationWrapper();
        searchOrganizationWrapper.setTotalRecords(totalRecords);
        searchOrganizationWrapper.setRecordsReturned(25);
        searchOrganizationWrapper.setStartIndex(Integer.parseInt(startIndex));
        searchOrganizationWrapper.setPageSize(25);
        searchOrganizationWrapper.setDir("asc");
        searchOrganizationWrapper.setSearchOrganizationDTOs(new SearchOrganizationDTO[searchedOrganizations.size()]);
        int index = 0;
        for(Organization organization: searchedOrganizations){
            SearchOrganizationDTO dto = new SearchOrganizationDTO();

            dto.setNciInstituteCode(organization.getNciInstituteCode());
            dto.setOrganizationName(organization.getName());

            dto.setStatus("Organization status place holder");

            String studyNames = getStudyNames(organization);
            dto.setStudy(studyNames);
            
            String statusPlaceHolder = "Active";

            String actions = "<a class='fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all' id='clinicalStaffActions"
                    + organization.getId() + "'"
                    + " onmouseover=\"javascript:showPopUpMenuClinicalStaff('"
                    + organization.getId()
                    + "','"
                    + statusPlaceHolder
                    + "');\">"
                    + "<span class=\"ui-icon ui-icon-triangle-1-s\"></span>Actions</a>";

            dto.setActions(actions);

            searchOrganizationWrapper.getSearchOrganizationDTOs()[index] = dto;
            index++;
        }
        JSONObject jsonObject = JSONObject.fromObject(searchOrganizationWrapper);
        Map<String, Object> modelMap = new HashMap<String, Object>();

        modelMap.put("shippedRecordSet", jsonObject);
        modelAndView.addObject("totalRecords", totalRecords);

        return new ModelAndView("jsonView", modelMap);
    }

    private String searchStringHighlight(String string,String[] stringArray){
       String highlightedString = string;
        if(highlightedString!= null && stringArray != null){
            for(String str: stringArray){
                highlightedString = highlightedString.replaceAll("(?i)"+str,"<b>"+str+"</b>");
            }
        }
       return highlightedString;
    }

    private String getSiteNames(ClinicalStaff clinicalStaff){
        String siteNames="";
        for(OrganizationClinicalStaff organizationClinicalStaff: clinicalStaff.getOrganizationClinicalStaffs()){
                siteNames = siteNames + organizationClinicalStaff.getOrganization().getDisplayName() + "</br>";
        }
        return siteNames;

    }

    private String getStudyNames(Organization organization){
        String studyNames = "";
        Set<Study> studiesSet = new HashSet<Study>();
        
        for(StudyOrganization studyOrganization : organization.getStudyOrganizations()) {
        	studiesSet.add(studyOrganization.getStudy());
        }
        
        for (Study study : studiesSet) {
            studyNames += study.getDisplayName() + "<br>";
        }
        return studyNames;
    }
    
    @Required
    public void setOrganizationAjaxFacade(OrganizationAjaxFacade organizationAjaxFacade) {
        this.organizationalAjaxFacade = organizationAjaxFacade;
    }

    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    public Properties getProCtcAEProperties() {
        return proCtcAEProperties;
    }

    public void setProCtcAEProperties(Properties proCtcAEProperties) {
        this.proCtcAEProperties = proCtcAEProperties;
    }
}
