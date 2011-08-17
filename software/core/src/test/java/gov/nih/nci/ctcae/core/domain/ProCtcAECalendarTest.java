package gov.nih.nci.ctcae.core.domain;


import junit.framework.TestCase;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Suneel Allareddy
 * @since Jan 17, 2010
 */
public class ProCtcAECalendarTest extends TestCase {
    /**
     * **
     * Testing the  due dates for different dates
     *
     *
     */
    public void testGetDueDateForCalendarDate() {
        ProCtcAECalendar proCtcAECalendar = new ProCtcAECalendar();
        //test the hours
        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.AM_PM, 0);
        c1.set(2011, 0, 17, 9, 20, 0);

        Date dueDate = proCtcAECalendar.getDueDateForCalendarDate(c1, "Hours", 2);
        c1.add(Calendar.HOUR, 2);
        assertEquals(c1.getTime(), dueDate);

        Calendar c2 = Calendar.getInstance();
        c2.set(Calendar.AM_PM, 1);
        c2.set(2010, 11, 31, 9, 20, 0);
        dueDate = proCtcAECalendar.getDueDateForCalendarDate(c2, "Hours", 18);
        c2.add(Calendar.HOUR, 18);
        assertEquals(c2.get(Calendar.DATE), 1);
        assertEquals(c2.get(Calendar.MONTH), 0);
        assertEquals(c2.getTime(), dueDate);

        //test the days
        Calendar c3 = Calendar.getInstance();
        c3.set(Calendar.AM_PM, 0);
        c3.set(2010, 11, 30, 9, 20, 0);
        dueDate = proCtcAECalendar.getDueDateForCalendarDate(c3, "Days", 18);
        c3.add(Calendar.DATE, 18);
        assertEquals(c3.get(Calendar.DATE), 17);
        assertEquals(c3.get(Calendar.MONTH), 0);
        assertEquals(c3.get(Calendar.YEAR), 2011);
        assertEquals(c3.getTime(), dueDate);

        Calendar c4 = Calendar.getInstance();
        c4.set(Calendar.AM_PM, 0);
        c4.set(2011, 1, 25, 9, 20, 0);
        dueDate = proCtcAECalendar.getDueDateForCalendarDate(c4, "Days", 10);
        c4.add(Calendar.DATE, 10);
        assertEquals(c4.get(Calendar.DATE), 7);
        assertEquals(c4.get(Calendar.MONTH), 2);
        assertEquals(c4.get(Calendar.YEAR), 2011);
        assertEquals(c4.getTime(), dueDate);

        //test the weeks
        Calendar c5 = Calendar.getInstance();
        c5.set(Calendar.AM_PM, 0);
        c5.set(2011, 1, 25, 9, 20, 0);
        dueDate = proCtcAECalendar.getDueDateForCalendarDate(c5, "Weeks", 2);
        c5.add(Calendar.WEEK_OF_YEAR, 2);
        assertEquals(c5.get(Calendar.DATE), 11);
        assertEquals(c5.get(Calendar.MONTH), 2);
        assertEquals(c5.get(Calendar.YEAR), 2011);
        assertEquals(c5.getTime(), dueDate);


        Calendar c6 = Calendar.getInstance();
        c6.set(Calendar.AM_PM, 0);
        c6.set(2011, 11, 25, 9, 20, 0);
        dueDate = proCtcAECalendar.getDueDateForCalendarDate(c6, "Weeks", 3);
        c6.add(Calendar.WEEK_OF_YEAR, 3);
        assertEquals(c6.get(Calendar.DATE), 15);
        assertEquals(c6.get(Calendar.MONTH), 0);
        assertEquals(c6.get(Calendar.YEAR), 2012);
        assertEquals(c6.getTime(), dueDate);
    }


}