package gov.nih.nci.ctcae.commons.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * @author
 */
public class DateUtils extends edu.nwu.bioinformatics.commons.DateUtils {
    protected static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    protected static DateFormat dashedDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    protected static final Log logger = LogFactory.getLog(DateUtils.class);

    public static Date parseDate(String dateString) throws ParseException {
        return dateFormat.parse(dateString.trim());
    }

    public static int compareDate(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);

        int x = c1.get(Calendar.YEAR);
        int y = c2.get(Calendar.YEAR);
        if (x != y) return x - y;

        x = c1.get(Calendar.MONTH);
        y = c2.get(Calendar.MONTH);
        if (x != y) return x - y;

        x = c1.get(Calendar.DATE);
        y = c2.get(Calendar.DATE);
        return x - y;
    }


    public static Date parseDashedDate(String dateString) throws ParseException {

        return dashedDateFormat.parse(dateString);

    }

    public static Date getCurrentDate() {

        String s = dateFormat.format(new Date());
        try {
            return dateFormat.parse(s);
        } catch (ParseException e) {
            logger.error(e);
            throw new RuntimeException(e);

        }

    }

    public static String format(Date date) {
        return dateFormat.format(date);

    }

    /**
     * ex yyyy-mm-dd
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String formatDashedFormart(Date date) {

        return dashedDateFormat.format(date);

    }

    public static Date addDaysToDate(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date.getTime());
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    public static int daysBetweenDates(Date first, Date second) {

        long time = getCalendarForDate(first).getTimeInMillis();
        long time1 = getCalendarForDate(second).getTimeInMillis();
        long diff = time - time1;
        return new Long(diff / (1000 * 60 * 60 * 24)).intValue();
    }

    public static int weeksBetweenDates(Date first, Date second) {
        return (daysBetweenDates(first, second) / 7) + 1;
    }

    public static int monthsBetweenDates(Date first, Date second) {
        return (daysBetweenDates(first, second) / 30) + 1;
    }

    public static Calendar getCalendarForDate(Date date) {
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

        int month = Integer.parseInt(sdfMonth.format(date));
        int day = Integer.parseInt(sdfDay.format(date));
        int year = Integer.parseInt(sdfYear.format(date));

        Calendar c1 = Calendar.getInstance();
        c1.set(year, month - 1, day);
        c1.set(Calendar.AM_PM, 0);
        c1.set(Calendar.HOUR, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);
        return c1;
    }

}
