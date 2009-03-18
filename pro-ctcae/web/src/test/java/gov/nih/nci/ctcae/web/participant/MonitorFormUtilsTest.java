package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.web.WebTestCase;

import java.util.Date;

/**
 * @author Mehul Gulati
 * Date: Mar 17, 2009
 */
public class MonitorFormUtilsTest extends WebTestCase {

    public void testGetStartDate(){

        String dateRange = "lastMonth";
        Date startDate = new Date();
        Date endDate = new Date();

     Date[] date = MonitorFormUtils.getStartEndDate(dateRange);
     System.out.println(date[0]+","+date[1]);
}
}
