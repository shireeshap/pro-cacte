package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.service.AuthorizationServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author mehul
 * Date: 12/16/11
 */

public class FetchCrfController extends AbstractController {

    private CrfAjaxFacade crfAjaxFacade;
    public static final int rowsPerPageInt = 25;
    private AuthorizationServiceImpl authorizationServiceImpl;
    private String PRIVILEGE_VERSION_FORM = "PRIVILEGE_VERSION_FORM";
    private String PRIVILEGE_VIEW_FORM = "PRIVILEGE_VIEW_FORM";
    private String PRIVILEGE_COPY_FORM = "PRIVILEGE_COPY_FORM";
    private String PRIVILEGE_RELEASE_FORM = "PRIVILEGE_RELEASE_FORM";
    private String PRIVILEGE_DELETE_FORM = "PRIVILEGE_DELETE_FORM";
    private String PRIVILEGE_EDIT_FORM = "PRIVILEGE_EDIT_FORM";

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {

        ModelAndView modelAndView = new ModelAndView("form/manageForm");
        String startIndex = request.getParameter("startIndex");
        String results = request.getParameter("results");
        String sort = request.getParameter("sort");
        String dir = request.getParameter("dir");
        String searchString = "";
        String[] searchStrings = null;
        searchString = (String) request.getSession().getAttribute("crfSearchString");
        if(!StringUtils.isBlank(searchString)) {
            searchString.trim();
            searchStrings = searchString.split("\\s+");
        }
        
        Long totalRecords = crfAjaxFacade.resultCount(searchStrings);
        List<CRF> crfs = crfAjaxFacade.searchCrfs(searchStrings, Integer.parseInt(startIndex), Integer.parseInt(results), sort, dir, totalRecords);
        
        SearchCRFWrapper searchCRFWrapper = new SearchCRFWrapper();
        searchCRFWrapper.setTotalRecords(totalRecords);
        searchCRFWrapper.setRecordsReturned(crfs.size());
        searchCRFWrapper.setStartIndex(Integer.parseInt(startIndex));
        searchCRFWrapper.setPageSize(rowsPerPageInt);
        searchCRFWrapper.setDir(dir);
        searchCRFWrapper.setSearchCrfDTOs(new SearchCrfDTO[crfs.size()]);
        int index = 0;
        for (CRF crf: crfs) {

            SearchCrfDTO dto = new SearchCrfDTO();
            if (crf.getEffectiveStartDate()!=null) {
            dto.setEffectiveStartDate(DateUtils.format(crf.getEffectiveStartDate()));
            }
            dto.setStatus(crf.getStatus().getCode());
            dto.setStudyShortTitle(crf.getStudy().getShortTitle());
            dto.setTitle(crf.getTitle());
            dto.setVersion(crf.getCrfVersion());
            boolean showVersion = false;
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            
            if (crf.getParentCrf() != null) {
                 showVersion = true;
            }
            Map<String, Boolean> crfInstancePrivilegeMap = getPrivilegesForCurrentCrfInstance(user, user.getUserSpecificPrivilegeRoleMap(), crf.getStudy());
            
            String actions = "<a class='fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all' id='crfActions"
                    + crf.getId() + "'"
                    + " onmouseover=\"javascript:showPopUpMenu('"
                    + crf.getId()
                    + "','"
                    + crf.getStatus().getDisplayName()
                    + "','"
                    + showVersion
                    + "','"
                    + crf.getTitle()
                     + "','"
                    + crfInstancePrivilegeMap.get(PRIVILEGE_VERSION_FORM)
                     + "','"
                    + crfInstancePrivilegeMap.get(PRIVILEGE_VIEW_FORM)
                     + "','"
                    + crfInstancePrivilegeMap.get(PRIVILEGE_COPY_FORM)
                     + "','"
                    + crfInstancePrivilegeMap.get(PRIVILEGE_RELEASE_FORM)
                     + "','"
                    + crfInstancePrivilegeMap.get(PRIVILEGE_DELETE_FORM)
                     + "','"
                    + crfInstancePrivilegeMap.get(PRIVILEGE_EDIT_FORM)
                    +"');\">"
                    + "<span class=\"ui-icon ui-icon-triangle-1-s\"></span>Actions</a>";

            dto.setActions(actions);
            searchCRFWrapper.getSearchCrfDTOs()[index] = dto;
            index++;
        }

        JSONObject jsonObject = JSONObject.fromObject(searchCRFWrapper);
        Map<String, Object> modelMap =  new HashMap<String, Object>();
        modelMap.put("shippedRecordSet", jsonObject);
        modelAndView.addObject("totalRecords", totalRecords);
        modelAndView.addObject("crfs", crfs);

        return new ModelAndView("jsonView", modelMap);

    }
    
    private Map<String, Boolean> getPrivilegesForCurrentCrfInstance(User user, Map<String, List<Role>> rolePrivilegeMap, Study study){
    	 Map<String, Boolean> crfInstancePrivilegeMap = new HashMap<String, Boolean>();
    	 List<Role> roles = new ArrayList<Role>();
    	 boolean hasVersionFormPrivilege = false;
         boolean hasViewFormPrivilege = false;
         boolean hasCopyFormPrivilege = false;
         boolean hasReleaseFormPrivilege = false;
         boolean hasDeleteFormPrivilege = false;
         boolean hasEditFormPrivilege = false;
        
         roles = rolePrivilegeMap.get(PRIVILEGE_VERSION_FORM);
         hasVersionFormPrivilege = authorizationServiceImpl.hasRole(study, roles, user);
         crfInstancePrivilegeMap.put(PRIVILEGE_VERSION_FORM, hasVersionFormPrivilege);
         
         roles = rolePrivilegeMap.get(PRIVILEGE_VIEW_FORM);
         hasViewFormPrivilege = authorizationServiceImpl.hasRole(study, roles, user);
         crfInstancePrivilegeMap.put(PRIVILEGE_VIEW_FORM, hasViewFormPrivilege);
         
         roles = rolePrivilegeMap.get(PRIVILEGE_COPY_FORM);
         hasCopyFormPrivilege = authorizationServiceImpl.hasRole(study, roles, user);
         crfInstancePrivilegeMap.put(PRIVILEGE_COPY_FORM, hasCopyFormPrivilege);
         
         roles = rolePrivilegeMap.get(PRIVILEGE_RELEASE_FORM);
         hasReleaseFormPrivilege = authorizationServiceImpl.hasRole(study, roles, user);
         crfInstancePrivilegeMap.put(PRIVILEGE_RELEASE_FORM, hasReleaseFormPrivilege);
         
         roles = rolePrivilegeMap.get(PRIVILEGE_DELETE_FORM);
         hasDeleteFormPrivilege = authorizationServiceImpl.hasRole(study, roles, user);
         crfInstancePrivilegeMap.put(PRIVILEGE_DELETE_FORM, hasDeleteFormPrivilege);
         
         roles = rolePrivilegeMap.get(PRIVILEGE_EDIT_FORM);
         hasEditFormPrivilege = authorizationServiceImpl.hasRole(study, roles, user);
         crfInstancePrivilegeMap.put(PRIVILEGE_EDIT_FORM, hasEditFormPrivilege);
         
         return crfInstancePrivilegeMap;
    }

    public void setCrfAjaxFacade(CrfAjaxFacade crfAjaxFacade) {
        this.crfAjaxFacade = crfAjaxFacade;
    }
    
    public void setAuthorizationServiceImpl(AuthorizationServiceImpl authorizationServiceImpl) {
        this.authorizationServiceImpl = authorizationServiceImpl;
    }
}
