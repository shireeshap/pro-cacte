package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.ProCtcAECalendar;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Mehul Gulati
 *         Date: Mar 17, 2009
 */
public class MonitorFormUtils {

    public static Date[] getStartEndDate(String dateRange) {
        Date today = new Date();
        Date startDate;
        Date endDate;
        Calendar c = ProCtcAECalendar.getCalendarForDate(today);
        Date[] dates = new Date[2];


        if ("thisWeek".equals(dateRange)) {
            c.set(Calendar.DAY_OF_WEEK, 1);
            startDate = c.getTime();
            dates[0] = startDate;
        }
        if ("lastWeek".equals(dateRange)) {
            c.set(Calendar.DAY_OF_WEEK, 1);
            c.add(Calendar.DATE, -7);
            startDate = c.getTime();
            dates[0] = startDate;
        }
        if ("thisMonth".equals(dateRange)) {
            c.set(Calendar.DAY_OF_MONTH, 1);
            startDate = c.getTime();
            dates[0] = startDate;
        }
        if ("lastMonth".equals(dateRange)) {
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.add(Calendar.DATE, -1);
            c.set(Calendar.DAY_OF_MONTH, 1);
            startDate = c.getTime();
        }
        c.add(Calendar.DATE, 6);
        endDate = c.getTime();
        dates[1] = endDate;
        return dates;
    }

}
