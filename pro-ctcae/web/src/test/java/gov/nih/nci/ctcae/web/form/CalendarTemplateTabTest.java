package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.web.WebTestCase;
import org.apache.commons.lang.StringUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Vinay Kumar
 * @crated Nov 6, 2008
 */
public class CalendarTemplateTabTest extends WebTestCase {


    @Override
    protected void setUp() throws Exception {
        super.setUp();


    }

    public void testIsNumeric() {
        assertTrue(StringUtils.isNumeric("1"));
    }

    public void testCharacterForNumber() {
        for (int i = 65; i < 90; i++) {
//            assertEquals('A', Character.forDigit(i, 2));
            System.out.println(i + "," + (char) i);
        }
    }


    public void testCalendar() {
        Calendar c = GregorianCalendar.getInstance();
        System.out.println(c.get(Calendar.DAY_OF_WEEK));
        //3 - Tues
        c.add(Calendar.DATE, 4);
        System.out.println(c.get(Calendar.DAY_OF_WEEK));
        c.add(Calendar.DATE, 1);
        System.out.println(c.get(Calendar.DAY_OF_WEEK));

    }
}