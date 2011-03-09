package gov.nih.nci.ctcae.web.batch;

import org.springframework.scheduling.quartz.SimpleTriggerBean;

import java.util.Calendar;


public class NightlyTriggerBean extends SimpleTriggerBean {
    public NightlyTriggerBean() {
        setStartDelay(0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 04);
        calendar.set(Calendar.MINUTE, 15);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        setStartTime(calendar.getTime());
        setRepeatInterval(24 * 60 * 60 * 1000);
//        setRepeatInterval(600000);
//        setStartDelay(20000);
    }
}
