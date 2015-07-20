package gov.nih.nci.ctcae.web.alert;

import gov.nih.nci.ctcae.core.domain.Alert;
import gov.nih.nci.ctcae.core.domain.AlertStatus;
import gov.nih.nci.ctcae.core.domain.QueryStrings;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.query.AlertQuery;
import gov.nih.nci.ctcae.core.repository.AlertRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

public class AlertAjaxFacade {

    protected final Log logger = LogFactory.getLog(getClass());
    AlertRepository alertRepository;
    private final static String ALERT_MESSAGE = "alertMessage";
	private final static String ALERT_STATUS = "alertStatus";
	private final static String START_DATE = "startDate";
	private final static String END_DATE = "endDate";
	private final static String ACTIVE = "ACTIVE";



    public List<Alert> searchAlerts(String[] searchString, Integer startIndex, Integer results, String sort, String dir, Long totalRecords) {
        List<Alert> alerts = getAlertObjects(searchString, startIndex, results, sort, dir, true, totalRecords);
        return alerts;
    }

    public List<Alert> getAlertObjects(String[] searchStrings, Integer startIndex, Integer results, String sort, String dir, boolean showInactive, Long totalRecords) {
        AlertQuery alertQuery = new AlertQuery(QueryStrings.ALERT_QUERY_SORTBY_FIELDS);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        String userName = user.getUsername();
        int sIndex = startIndex;
        
        List<Alert> alerts = new ArrayList<Alert>();
        Long searchCount = totalRecords;
        alertQuery.setFirstResult(startIndex);
        alertQuery.setMaximumResults(results);
        if(START_DATE.equals(sort)) {
        	alertQuery.setSortBy(" alert.startDate ");
        } else if(END_DATE.equals(sort)) {
        	alertQuery.setSortBy(" alert.endDate ");
        } else if(ALERT_STATUS.equals(sort)) {
        	alertQuery.setSortBy(" alert.alertStatus ");
        } else {
        	alertQuery.setSortBy(" alert.alertMessage ");
        }
        alertQuery.setSortDirection(dir);
        
        if (user.isAdmin()) {
            if (searchStrings != null) {
                int index = 0;
                for (String searchString : searchStrings) {
                    alertQuery.filterByAll(searchString, "" + index);
                    index++;
                }
            }
            alerts = (List<Alert>) alertRepository.find(alertQuery);
        }
        return alerts;
    }

    public Long resultCount(String[] searchTexts) {
        AlertQuery alertQuery = new AlertQuery(QueryStrings.ALERT_QUERY_COUNT);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (user.isAdmin()) {
            if (searchTexts != null) {
                int index = 0;
                for (String searchString : searchTexts) {
                    alertQuery.filterByAll(searchString, "" + index);
                    index++;
                }
            }
        }
        return alertRepository.findWithCount(alertQuery);
    }
    
    public List<Alert> fetchUpcommingAlerts() {
    	List<Alert> alerts = new ArrayList<Alert>();
    	
    	AlertQuery query = new AlertQuery();
    	query.filterByDateWithin(new Date());
    	query.filterByAlertStatus(AlertStatus.ACTIVE);
    	
    	alerts = (List<Alert>) alertRepository.find(query);
    	return alerts;
    }
    
    public AlertRepository getAlertRepository() {
		return alertRepository;
	}

	public void setAlertRepository(AlertRepository alertRepository) {
		this.alertRepository = alertRepository;
	}

    
}