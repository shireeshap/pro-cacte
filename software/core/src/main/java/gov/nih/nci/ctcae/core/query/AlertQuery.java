package gov.nih.nci.ctcae.core.query;

import java.util.Date;

import gov.nih.nci.ctcae.core.domain.AlertStatus;
import gov.nih.nci.ctcae.core.domain.QueryStrings;

import org.apache.commons.lang.StringUtils;

public class AlertQuery extends AbstractQuery {
	private final static String ALERT_MESSAGE = "alertMessage";
	private final static String ALERT_STATUS = "alertStatus";
	private final static String TODAY = "today";

    public AlertQuery() {
        super(QueryStrings.ALERT_QUERY_BASIC);
    }

    public AlertQuery(QueryStrings query) {
        super(query);
    }
    
	public void filterByAlertMessage(String alertMessage) {
		andWhere(" alert.alertMessage =:" + ALERT_MESSAGE);
		setParameter(ALERT_MESSAGE, alertMessage);
	}
    
	public void filterByAlertStatus(AlertStatus alertStatus) {
		andWhere(" alert.alertStatus = :" +ALERT_STATUS);
		setParameter(ALERT_STATUS, alertStatus);
	}
	
	public void filterByDateWithin(Date date) {
		andWhere(" :" + TODAY + " between alert.startDate and alert.endDate ");
		setParameter(TODAY, date);
	}
	
    public void filterByAll(String text, String key) {
        String searchString = text != null && StringUtils.isNotBlank(text) ? "%" + StringUtils.trim(StringUtils.lowerCase(text)) + "%" : null;
        andWhere(String.format("(lower(alert.alertMessage) LIKE :%s )", ALERT_MESSAGE + key));
        setParameter(ALERT_MESSAGE + key, searchString);
    }
    
}