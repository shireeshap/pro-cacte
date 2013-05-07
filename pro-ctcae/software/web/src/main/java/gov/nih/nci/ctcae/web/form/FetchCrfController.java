package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.CRF;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author mehul
 * Date: 12/16/11
 */

public class FetchCrfController extends AbstractController {

    private CrfAjaxFacade crfAjaxFacade;
    public static final int rowsPerPageInt = 25;

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
            //User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //boolean odc = user.isODCOnStudy(crf.getStudy());
            if (crf.getParentCrf() != null) {
                 showVersion = true;
            }

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

    public void setCrfAjaxFacade(CrfAjaxFacade crfAjaxFacade) {
        this.crfAjaxFacade = crfAjaxFacade;
    }
}
