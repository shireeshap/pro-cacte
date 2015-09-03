package gov.nih.nci.ctcae.core.domain;


import gov.nih.nci.ctcae.commons.utils.DateUtils;
import junit.framework.TestCase;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Suneel Allareddy
 * @since Jan 17, 2010
 */
public class ProCtcAECalendarTest extends TestCase {
	
	ProCtcAECalendar proCtcAECalendar;
	CRFCycle cycle;
	Calendar c;
	CRFCycleDefinition crfCycleDefinition;
	
	public ProCtcAECalendarTest(){
		proCtcAECalendar = new ProCtcAECalendar();
        c = Calendar.getInstance();
        c.setTime(DateUtils.addDaysToDate(new Date(), 5));
        proCtcAECalendar.setCalendar(c);
        crfCycleDefinition = new CRFCycleDefinition();
        crfCycleDefinition.setCycleLength(14);
        crfCycleDefinition.setCycleLengthUnit("Days");
        crfCycleDefinition.setRepeatTimes("2");
        crfCycleDefinition.setOrder(0);
        crfCycleDefinition.setDueDateUnit("Days");
        crfCycleDefinition.setDueDateValue("3");  
        cycle = new CRFCycle();
        cycle.setOrder(0);
        cycle.setCycleDays(",1,8,13");
        cycle.setCrfDefinition(crfCycleDefinition);
	}
	
	
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

        Date dueDate = proCtcAECalendar.getDueDateForCalendarDate(c1, "Hours", 3);
        c1.add(Calendar.HOUR, 2);
        assertEquals(c1.getTime(), dueDate);

        Calendar c2 = Calendar.getInstance();
        c2.set(Calendar.AM_PM, 1);
        c2.set(2010, 11, 31, 9, 20, 0);
        dueDate = proCtcAECalendar.getDueDateForCalendarDate(c2, "Hours", 19);
        c2.add(Calendar.HOUR, 18);
        assertEquals(c2.get(Calendar.DATE), 1);
        assertEquals(c2.get(Calendar.MONTH), 0);
        assertEquals(c2.getTime(), dueDate);

        //test the days
        Calendar c3 = Calendar.getInstance();
        c3.set(Calendar.AM_PM, 0);
        c3.set(2010, 11, 30, 9, 20, 0);
        dueDate = proCtcAECalendar.getDueDateForCalendarDate(c3, "Days", 19);
        c3.add(Calendar.DATE, 18);
        assertEquals(c3.get(Calendar.DATE), 17);
        assertEquals(c3.get(Calendar.MONTH), 0);
        assertEquals(c3.get(Calendar.YEAR), 2011);
        assertEquals(c3.getTime(), dueDate);

        Calendar c4 = Calendar.getInstance();
        c4.set(Calendar.AM_PM, 0);
        c4.set(2011, 1, 25, 9, 20, 0);
        dueDate = proCtcAECalendar.getDueDateForCalendarDate(c4, "Days", 11);
        c4.add(Calendar.DATE, 10);
        assertEquals(c4.get(Calendar.DATE), 7);
        assertEquals(c4.get(Calendar.MONTH), 2);
        assertEquals(c4.get(Calendar.YEAR), 2011);
        assertEquals(c4.getTime(), dueDate);

        //test the weeks
        Calendar c5 = Calendar.getInstance();
        c5.set(Calendar.AM_PM, 0);
        c5.set(2011, 1, 25, 9, 20, 0);
        dueDate = proCtcAECalendar.getDueDateForCalendarDate(c5, "Weeks", 3);
        c5.add(Calendar.WEEK_OF_YEAR, 2);
        assertEquals(c5.get(Calendar.DATE), 11);
        assertEquals(c5.get(Calendar.MONTH), 2);
        assertEquals(c5.get(Calendar.YEAR), 2011);
        assertEquals(c5.getTime(), dueDate);


        Calendar c6 = Calendar.getInstance();
        c6.set(Calendar.AM_PM, 0);
        c6.set(2011, 11, 25, 9, 20, 0);
        dueDate = proCtcAECalendar.getDueDateForCalendarDate(c6, "Weeks", 4);
        c6.add(Calendar.WEEK_OF_YEAR, 3);
        assertEquals(c6.get(Calendar.DATE), 15);
        assertEquals(c6.get(Calendar.MONTH), 0);
        assertEquals(c6.get(Calendar.YEAR), 2012);
        assertEquals(c6.getTime(), dueDate);
    }
    
    public void testIsDateAfterOrWithinMonth(){
    	Date today = new Date();
    	
    	assertFalse(proCtcAECalendar.isDateAfterMonth(today));
    	if(c.get(Calendar.MONTH) == today.getMonth()){
    		assertTrue(proCtcAECalendar.isDateWithinMonth(today));
    	}else{
    		assertFalse(proCtcAECalendar.isDateWithinMonth(today));
    	}
    	
    	
    }
    
    public void testSetCycleParameters(){
    	Date today = new Date();
    	int cycleNumber =1;
    	proCtcAECalendar.setCycleParameters(cycle, today, cycleNumber);
    	
    	assertEquals(proCtcAECalendar.getDueDateUnit(), "Days");
    	assertEquals(proCtcAECalendar.getDueDateAmount(), 3);
    	assertEquals(proCtcAECalendar.getStartDate(),today);
    	assertEquals(proCtcAECalendar.getCycleNumber(), cycleNumber);
    }
    
    public void testSetGeneralScheduleParameters(){
    	Date today = new Date();
    	CRFCalendar calendar = new CRFCalendar();
        calendar.setRepeatEveryValue("2");
        calendar.setRepeatEveryUnit("Days");
        calendar.setDueDateValue("24");
        calendar.setDueDateUnit("Hours");
        calendar.setRepeatUntilValue("2");
        calendar.setRepeatUntilUnit("Number");
         
        proCtcAECalendar.setGeneralScheduleParameters(calendar, today);
        
        assertEquals(proCtcAECalendar.getRepeatUntilUnit(), "Number");
        assertEquals(proCtcAECalendar.getRepeatUntilValue(), "2");
        assertEquals(proCtcAECalendar.getDueDateUnit(), "Hours");
        assertEquals(proCtcAECalendar.getDueDateAmount(), 24);
    }

}