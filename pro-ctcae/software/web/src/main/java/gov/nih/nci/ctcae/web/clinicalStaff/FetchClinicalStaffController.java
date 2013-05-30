package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.User;
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
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Created by IntelliJ IDEA.
 * User: c3pr
 * Date: 12/5/11
 * Time: 11:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class FetchClinicalStaffController extends AbstractController {
   private ClinicalStaffAjaxFacade clinicalStaffAjaxFacade;
    private StudyRepository studyRepository;
    private Properties proCtcAEProperties;
    private static String CLINICAL_STAFF_SEARCH_STRING = "clinicalStaffSearchString";

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("clinicalStaff/searchClinicalStaff");
        String startIndex = request.getParameter("startIndex");
        Integer startIndexInt = Integer.parseInt(startIndex);

        String results = request.getParameter("results");
        Integer resultsInt = Integer.parseInt(results);

        String sort = request.getParameter("sort");
        String dir = request.getParameter("dir");
        String[] searchStrings= null;

        String searchString = (String) request.getSession().getAttribute(CLINICAL_STAFF_SEARCH_STRING);
        if(!StringUtils.isBlank(searchString)){
            searchString.trim();
            searchStrings = searchString.split("\\s+");
        }
        Long totalRecords = clinicalStaffAjaxFacade.resultCount(searchStrings);
        List<ClinicalStaff>  finalClinicalStaffs = clinicalStaffAjaxFacade.searchClinicalStaff(searchStrings,startIndexInt,resultsInt,sort,dir, totalRecords);
        SearchClinicalStaffWrapper searchClinicalStaffWrapper = new SearchClinicalStaffWrapper();
        searchClinicalStaffWrapper.setTotalRecords(totalRecords);
        searchClinicalStaffWrapper.setRecordsReturned(25);
        searchClinicalStaffWrapper.setStartIndex(Integer.parseInt(startIndex));
        searchClinicalStaffWrapper.setPageSize(25);
        searchClinicalStaffWrapper.setDir("asc");
        searchClinicalStaffWrapper.setSearchClinicalStaffDTOs(new SearchClinicalStaffDTO[finalClinicalStaffs.size()]);
        int index = 0;
        for(ClinicalStaff clinicalStaff: finalClinicalStaffs){
            SearchClinicalStaffDTO dto = new SearchClinicalStaffDTO();

            String firstName = clinicalStaff.getFirstName();
            dto.setFirstName(firstName);

            String lastName = clinicalStaff.getLastName();
            dto.setLastName(lastName);

            dto.setStatus(clinicalStaff.getStatus().getDisplayName() + " from " + DateUtils.format(clinicalStaff.getEffectiveDate()));

            String studyNames = getStudyNames(clinicalStaff);
            dto.setStudy(studyNames);

            String siteNames = getSiteNames(clinicalStaff);
            dto.setSite(siteNames);

            String identifier = clinicalStaff.getNciIdentifier();
            dto.setNciIdentifier(identifier);

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
            String actions = "<a class='fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all' id='clinicalStaffActions"
                    + clinicalStaff.getId() + "'"
                    + " onmouseover=\"javascript:showPopUpMenuClinicalStaff('"
                    + clinicalStaff.getId()
                    + "','"
                    + clinicalStaff.getStatus()
                    + "','"
                    + odc
                    + "');\">"
                    + "<span class=\"ui-icon ui-icon-triangle-1-s\"></span>Actions</a>";

            dto.setActions(actions);

            searchClinicalStaffWrapper.getSearchClinicalStaffDTOs()[index] = dto;
            index++;
        }
        JSONObject jsonObject = JSONObject.fromObject(searchClinicalStaffWrapper);
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

    private String getStudyNames(ClinicalStaff clinicalStaff){
        String studyNames = "";
        Set<Study> studiesSet = new HashSet<Study>();
        for (OrganizationClinicalStaff site : clinicalStaff.getOrganizationClinicalStaffs()) {
            for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : site.getStudyOrganizationClinicalStaff()) {
                studiesSet.add(studyOrganizationClinicalStaff.getStudyOrganization().getStudy());
            }
        }
        for (Study study : studiesSet) {
            studyNames += study.getDisplayName() + "<br>";
        }
        return studyNames;
    }

    @Required
    public void setClinicalStaffAjaxFacade(ClinicalStaffAjaxFacade clinicalStaffAjaxFacade) {
        this.clinicalStaffAjaxFacade = clinicalStaffAjaxFacade;
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
