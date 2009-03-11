package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.web.WebTestCase;
import org.apache.commons.lang.StringUtils;

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
}