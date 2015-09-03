package gov.nih.nci.ctcae.web.batch;

import java.text.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.quartz.CronTriggerBean;


public class SPCrfGradeCreationTrigger extends CronTriggerBean {
	protected final static Log logger = LogFactory.getLog(SPCrfGradeCreationTrigger.class.getName());
	
    public SPCrfGradeCreationTrigger() {
    	
    	try {
			setCronExpression("0 15 1 * * ? ");
		} catch (ParseException e) {
			logger.error("SPCrfGradeCreationTrigger: ParseException in cron expression. " + e.getMessage());
		}
    }
}
