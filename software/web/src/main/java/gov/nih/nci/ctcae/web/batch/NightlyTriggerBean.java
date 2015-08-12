package gov.nih.nci.ctcae.web.batch;

import java.text.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.quartz.CronTriggerBean;


public class NightlyTriggerBean extends CronTriggerBean {
	protected static final Log logger = LogFactory.getLog(NightlyTriggerBean.class.getName());
	
    public NightlyTriggerBean() {
    	try {
			setCronExpression("0 30 0 * * ? ");
		} catch (ParseException e) {
			logger.error("NightlyTriggerBean: ParseException in cron expression. " + e.getMessage());
		}
    }
}
