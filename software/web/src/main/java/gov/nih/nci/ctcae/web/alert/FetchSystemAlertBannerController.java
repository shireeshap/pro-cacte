package gov.nih.nci.ctcae.web.alert;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.Alert;
import gov.nih.nci.ctcae.core.domain.AlertStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class FetchSystemAlertBannerController extends AbstractController {
   private AlertAjaxFacade alertAjaxFacade;
   private Properties proCtcAEProperties;
   public static String ALERT_SEARCH_STRING = "searchString";

   
   	protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = new ModelAndView("alert/searchSystemAlert");
        String startIndex = request.getParameter("startIndex");
        Integer startIndexInt = Integer.parseInt(startIndex);

        String results = request.getParameter("results");
        Integer resultsInt = Integer.parseInt(results);

        String sort = request.getParameter("sort");
        String dir = request.getParameter("dir");
        String[] searchStrings= null;

        String searchString = (String) request.getSession().getAttribute(ALERT_SEARCH_STRING);
        if(!StringUtils.isBlank(searchString)){
            searchString.trim();
            searchStrings = searchString.split("\\s+");
        }
        Long totalRecords = alertAjaxFacade.resultCount(searchStrings);
        List<Alert> finalAlerts = alertAjaxFacade.searchAlerts(searchStrings,startIndexInt,resultsInt,sort,dir, totalRecords);
        SearchAlertWrapper searchAlertWrapper = new SearchAlertWrapper();
        searchAlertWrapper.setTotalRecords(totalRecords);
        searchAlertWrapper.setRecordsReturned(25);
        searchAlertWrapper.setStartIndex(Integer.parseInt(startIndex));
        searchAlertWrapper.setPageSize(25);
        searchAlertWrapper.setDir("asc");
        searchAlertWrapper.setSearchAlertDTOs(new SearchAlertDTO[finalAlerts.size()]);
        
        int index = 0;
        for(Alert alert: finalAlerts){
            SearchAlertDTO dto = new SearchAlertDTO();
            
            Date startDate = alert.getStartDate();
            dto.setStartDate(DateUtils.format(startDate));
            
            Date endDate = alert.getEndDate();
            dto.setEndDate(DateUtils.format(endDate));
            
            AlertStatus alertStatus = alert.getAlertStatus();
            dto.setAlertStatus((alertStatus !=null? alertStatus.getDisplayName() : ""));
            
            String alertMessage = alert.getAlertMessage();
            dto.setAlertMessage(alertMessage);
            
            String actions = "<a class='fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all' id='alertActions"
                    + alert.getId() + "'"
                    + " onmouseover=\"javascript:showPopUpMenuSystemAlert('"
                    + alert.getId()
                    + "','"
                    + alert.getAlertStatus()
                    + "');\">"
                    + "<span class=\"ui-icon ui-icon-triangle-1-s\"></span>Actions</a>";

            dto.setActions(actions);

            searchAlertWrapper.getSearchAlertDTOs()[index] = dto;
            index++;
        }
        JSONObject jsonObject = JSONObject.fromObject(searchAlertWrapper);
        Map<String, Object> modelMap = new HashMap<String, Object>();

        modelMap.put("shippedRecordSet", jsonObject);
        modelAndView.addObject("totalRecords", totalRecords);

        return new ModelAndView("jsonView", modelMap);
    }

    public Properties getProCtcAEProperties() {
        return proCtcAEProperties;
    }

    public void setProCtcAEProperties(Properties proCtcAEProperties) {
        this.proCtcAEProperties = proCtcAEProperties;
    }
    
    @Required
    public void setAlertAjaxFacade(AlertAjaxFacade alertAjaxFacade) {
    	this.alertAjaxFacade = alertAjaxFacade;
    }
}