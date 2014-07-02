package gov.nih.nci.ctcae.web.batch;

import org.springframework.scheduling.quartz.SimpleTriggerBean;

import java.util.Calendar;


public class SPCrfGradeCreationTrigger extends SimpleTriggerBean {
    public SPCrfGradeCreationTrigger() {
        setStartDelay(0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 11);
        calendar.set(Calendar.MINUTE, 15);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        setStartTime(calendar.getTime());
        //for testing
        //setRepeatInterval(10 * 60 * 1000);
        //original value
        setRepeatInterval(24 * 60 * 60 * 1000);
    }
}
