package gov.nih.nci.ctcae.commons.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author
 */
public class DateUtils extends edu.nwu.bioinformatics.commons.DateUtils {
    protected static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    protected static DateFormat dashedDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    protected static final Log logger = LogFactory.getLog(DateUtils.class);
    private static final String DEFAULT_TIMEZONE_ID = "America/New_York";

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
        return date == null ? null : dateFormat.format(date);

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

    public static int hoursBetweenDates(Date first, Date second) {
        long time = getCalendarForDate(first).getTimeInMillis();
        long time1 = getCalendarForDate(second).getTimeInMillis();
        long diff = time - time1;
        return new Long(diff / (1000 * 60 * 60)).intValue();
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
    
    /**
     * Gets the next date given any date. 
     *
     * @param theDate the the date
     * @return the next day
     */
    public static Date getNextDay(Date theDate){
		Calendar tmrw = Calendar.getInstance();
		tmrw.setTime(theDate);
		tmrw.add(Calendar.DATE, 1);
    	
    	return tmrw.getTime();
    }
    
    public static Date getDateInTimeZone(Date currentDate, String timeZoneId) {
    	TimeZone tz;

    	if(StringUtils.isEmpty(timeZoneId)) {
    		tz = TimeZone.getTimeZone(DEFAULT_TIMEZONE_ID);
    	} else {
    		tz = TimeZone.getTimeZone(timeZoneId);
    	}
	    Calendar mbCal = new GregorianCalendar(tz);
	    mbCal.setTimeInMillis(currentDate.getTime());
	
	    Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.YEAR, mbCal.get(Calendar.YEAR));
	    cal.set(Calendar.MONTH, mbCal.get(Calendar.MONTH));
	    cal.set(Calendar.DAY_OF_MONTH, mbCal.get(Calendar.DAY_OF_MONTH));
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0) ;
	
	    return cal.getTime();
    }

    // input timeString format 23:59
    public static String getFormattedTime(String timeString){
          String finalTimeString ="";
          String time[] = timeString.split(":");
          String hour = time[0].trim();
          String min = time[1].trim();
          if(Integer.parseInt(hour) <12){
              finalTimeString = hour + ":"+ min + " AM";
          }else{
              int hourInt = Integer.parseInt(hour) - 12;
              if(hourInt < 10)
                  hour = "0" + hourInt;
              else
                  hour = "" + hourInt;
              finalTimeString =  hour + ":" + min + " PM";
          }
        return finalTimeString;
    }
    
    /**
     * getEquivalentSystemTimeForTimeZone() method.
     * @param date
     * @param timeZoneId
     * @return
     * Given a date (yyyy-MM-dd hh:mm:ss) in 24 hour format and target timeZoneId,
     * this method returns equivalent time as per the system's timezone.
     * e.g if the date is "Nov 4th 2013 7:30:00" and timeZone is CST,
     * then this method would return  "Nov 4th 2013 8:30:00 EST" (assuming system's timezone as EST)
     */
    public static Date getEquivalentSystemTimeForTimeZone(Date date, String timeZoneId){
    	TimeZone targetTimeZone = TimeZone.getTimeZone(timeZoneId);
    	Calendar targetZoneCalendar = Calendar.getInstance(targetTimeZone);
    	targetZoneCalendar.setTimeInMillis(date.getTime());
		
    	Date dateInTargetZone = DateUtils.getDateInTimeZone(date, targetTimeZone.getID());
		//System.out.println("Current time in ms in target zone: " + currentDateInTargetZone.getTime());
		long offsetInMS = date.getTime() - dateInTargetZone.getTime();
		long equivalentEstTimeForDate = date.getTime() + offsetInMS;
		
		Calendar newCal = Calendar.getInstance();
		newCal.setTimeInMillis(equivalentEstTimeForDate);
		return  newCal.getTime();
    }
    
    public static int daysBetweenDatesWithRoundOff(Calendar c1, Calendar c2){
    	c1.set(Calendar.HOUR_OF_DAY, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);
        
        c2.set(Calendar.HOUR_OF_DAY, 0);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MILLISECOND, 0);
        
        long diffMillis = c1.getTimeInMillis() - c2.getTimeInMillis();
        double diff = (double)(diffMillis/ (double) (1000 * 60 * 60 * 24));
        double mantissa = diff % 1;
        // i.e 24.6 => 25 (as 0.6 > 0.5), 24.42 => 24 (as 0.42 < 0.5)
        if(mantissa > 0.5){
        	diff = Math.ceil(diff);
        }
        
        // i.e -24.6 => -25 (as -0.6 < -0.5), -24.42 => -24 (as -0.42 > -0.5)
        if(mantissa < -0.5){
        	diff = Math.floor(diff);
        }
        
        return new Double(diff).intValue();
    }
}
