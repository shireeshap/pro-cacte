package gov.nih.nci.ctcae.commons.utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import edu.nwu.bioinformatics.commons.testing.CoreTestCase;

/**
 * @author
 * @created Sep 26, 2008
 */
public class DateUtilsTest extends CoreTestCase {

    Date futureDate = DateUtils.createDate(2012, Calendar.JULY, 13);
    Date pastDate = DateUtils.createDate(2008, Calendar.JANUARY, 21);

    public void testFormatDashedDate() throws ParseException {
    	System.out.println("testFormatDashedDate starting..");
        assertEquals("2012-07-13", DateUtils.formatDashedFormart(futureDate));
        assertEquals("2008-01-21", DateUtils.formatDashedFormart(pastDate));
        System.out.println("testFormatDashedDate complete..");
    }

    public void testFormatDate() throws ParseException {
    	System.out.println("testFormatDate starting..");
        assertEquals("07/13/2012", DateUtils.format(futureDate));
        assertEquals("01/21/2008", DateUtils.format(pastDate));
        System.out.println("testFormatDate complete..");
    }


    public void testParseDate() throws ParseException {
    	System.out.println("testParseDate starting..");
        assertEquals(Integer.valueOf(0), Integer.valueOf(DateUtils.compareDate(DateUtils.parseDate("07/13/2012"), futureDate)));
        assertEquals(Integer.valueOf(0), Integer.valueOf(DateUtils.compareDate(DateUtils.parseDate("01/21/2008"), pastDate)));
        System.out.println("testParseDate complete..");
    }


    public void testParseDashedDate() throws ParseException {
    	System.out.println("testParseDashedDate starting..");
        assertEquals(Integer.valueOf(0), Integer.valueOf(DateUtils.compareDate(DateUtils.parseDashedDate("2012-07-13"), futureDate)));
        assertEquals(Integer.valueOf(0), Integer.valueOf(DateUtils.compareDate(DateUtils.parseDashedDate("2008-01-21"), pastDate)));
        System.out.println("testParseDashedDate complete..");
    }

    public void testCompareDate() throws ParseException {
    	System.out.println("testCompareDate starting..");
        assertEquals(Integer.valueOf(1), Integer.valueOf(DateUtils.compareDate(DateUtils.parseDashedDate("2012-07-14"), futureDate)));
        assertEquals(Integer.valueOf(1), Integer.valueOf(DateUtils.compareDate(DateUtils.parseDashedDate("2012-08-13"), futureDate)));
        assertEquals(Integer.valueOf(1), Integer.valueOf(DateUtils.compareDate(DateUtils.parseDashedDate("2013-07-13"), futureDate)));
        assertEquals(Integer.valueOf(-1), Integer.valueOf(DateUtils.compareDate(DateUtils.parseDashedDate("2008-01-20"), pastDate)));
        System.out.println("testCompareDate complete..");
    }

    public void testAddDaysToDate() throws ParseException {
    	System.out.println("testAddDaysToDate starting..");
        Date d = DateUtils.parseDate("10/10/2009");
        d = DateUtils.addDaysToDate(d, 5);
        assert (DateUtils.format(d).indexOf("/15/") != -1);
       	System.out.println("testAddDaysToDate complete..");
    }

    public void testGetCurrentDate() throws ParseException {
       	System.out.println("testGetCurrentDate starting..");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.AM_PM, 0);
        Date s = DateUtils.getCurrentDate();
        assertEquals(c.getTime(), s);
       	System.out.println("testGetCurrentDate complete.."); 
    }

    public void testDaysBetweenDates() {
       	System.out.println("testDaysBetweenDates starting..");
        Calendar c = GregorianCalendar.getInstance();
        Date d1 = c.getTime();

        c.add(Calendar.DATE, 8);
        Date d2 = c.getTime();

        assertEquals(-8, DateUtils.daysBetweenDates(d1, d2));
        assertEquals(8, DateUtils.daysBetweenDates(d2, d1));
    	System.out.println("testDaysBetweenDates complete..");
    }

    public void testWeeksBetweenDates() {
    	System.out.println("testWeeksBetweenDates starting..");
        Calendar c = GregorianCalendar.getInstance();
        Date d1 = c.getTime();

        c.add(Calendar.DATE, 2);
        Date d2 = c.getTime();
        assertEquals(1, DateUtils.weeksBetweenDates(d2, d1));

        c.add(Calendar.DATE, 11);
        d2 = c.getTime();
        assertEquals(2, DateUtils.weeksBetweenDates(d2, d1));

        c.add(Calendar.DATE, 1);
        d2 = c.getTime();
        assertEquals(2, DateUtils.weeksBetweenDates(d2, d1));

        c.add(Calendar.DATE, 2);
        d2 = c.getTime();
        assertEquals(3, DateUtils.weeksBetweenDates(d2, d1));

        c.add(Calendar.DATE, 5);
        d2 = c.getTime();
        assertEquals(3, DateUtils.weeksBetweenDates(d2, d1));
    	System.out.println("testWeeksBetweenDates complete..");
    }
    
    public void testMonthsBetweenDates() {
    	System.out.println("testMonthsBetweenDates starting..");
        Calendar c = GregorianCalendar.getInstance();
        Date d1 = c.getTime();

        c.add(Calendar.DATE, 2);
        Date d2 = c.getTime();
        assertEquals(1, DateUtils.monthsBetweenDates(d2, d1));

        c.add(Calendar.DATE, 30);
        d2 = c.getTime();
        assertEquals(2, DateUtils.monthsBetweenDates(d2, d1));

        c.add(Calendar.DATE, 1);
        d2 = c.getTime();
        assertEquals(2, DateUtils.monthsBetweenDates(d2, d1));

        c.add(Calendar.DATE, 27);
        d2 = c.getTime();
        assertEquals(2, DateUtils.monthsBetweenDates(d2, d1));
        System.out.println("testMonthsBetweenDates complete..");
    }
}
