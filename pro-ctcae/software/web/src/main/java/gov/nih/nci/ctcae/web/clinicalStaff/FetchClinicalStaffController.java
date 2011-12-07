package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.secured.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.web.participant.ParticipantAjaxFacade;
import gov.nih.nci.ctcae.web.participant.SearchParticipantDTO;
import gov.nih.nci.ctcae.web.participant.SearchParticipantWrapper;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.util.*;
import gov.nih.nci.ctcae.commons.utils.DateUtils;

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

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("clinicalStaff/searchClinicalStaff");
        String useReqParam = request.getParameter("useReqParam");
        String startIndex = request.getParameter("startIndex");
        String results = request.getParameter("results");
        String sort = request.getParameter("sort");
        String dir = request.getParameter("dir");
        String searchString = "";

//        if (StringUtils.isBlank(useReqParam)) {
        searchString = (String) request.getSession().getAttribute("ParticipantSearchString");
//        } else {
        //    searchString = request.getParameter("searchString");
         //   request.getSession().setAttribute("ParticipantSearchString", searchString);
        //    request.getSession().removeAttribute("ParticipantSearchString");
//        }

        List<ClinicalStaff> clinicalStaffs = clinicalStaffAjaxFacade.searchClinicalStaff(searchString,Integer.parseInt(startIndex),Integer.parseInt(results),sort,dir);
        Long totalRecords = clinicalStaffAjaxFacade.resultCount(searchString);

        List<SearchClinicalStaffDTO> jsonListOfObjects = new ArrayList<SearchClinicalStaffDTO>();
        SearchClinicalStaffWrapper searchClinicalStaffWrapper = new SearchClinicalStaffWrapper();
        searchClinicalStaffWrapper.setTotalRecords(totalRecords);
        searchClinicalStaffWrapper.setRecordsReturned(25);
        searchClinicalStaffWrapper.setStartIndex(Integer.parseInt(startIndex));
        searchClinicalStaffWrapper.setPageSize(25);
        searchClinicalStaffWrapper.setDir("asc");
        searchClinicalStaffWrapper.setSearchClinicalStaffDTOs(new SearchClinicalStaffDTO[clinicalStaffs.size()]);
        int index = 0;
        for(ClinicalStaff clinicalStaff: clinicalStaffs){
            SearchClinicalStaffDTO dto = new SearchClinicalStaffDTO();
            dto.setFirstName(clinicalStaff.getFirstName());
            dto.setLastName(clinicalStaff.getLastName());
            dto.setStatus(clinicalStaff.getStatus().getDisplayName() + " from " + DateUtils.format(clinicalStaff.getEffectiveDate()));
            String studyNames = getStudyNames(clinicalStaff);
            dto.setStudy(studyNames);

            String actions = "<a class=\"fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all\" id=\"participantActions" + clinicalStaff.getId() + "\"><span class=\"ui-icon ui-icon-triangle-1-s\"></span>Actions</a><script>showPopUpMenuParticipant(\"" + clinicalStaff.getId() + "\",\"" + "--"+ "\");</script>";
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

    private String getStudyNames(ClinicalStaff clinicalStaff){
        String studyNames="";
        for(OrganizationClinicalStaff organizationClinicalStaff: clinicalStaff.getOrganizationClinicalStaffs()){
            for(StudyOrganizationClinicalStaff studyOrganizationClinicalStaff: organizationClinicalStaff.getStudyOrganizationClinicalStaff()){
                studyNames = studyNames + studyOrganizationClinicalStaff.getStudyOrganization().getStudy().getDisplayName() + "</br>";
            }
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
