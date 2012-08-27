package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.ProCtcAECalendar;
import gov.nih.nci.ctcae.web.WebTestCase;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Harsh Agarwal
 * @since Nov 24, 2008
 */
public class StudyParticipantCrfCalendarTest extends WebTestCase {

    ProCtcAECalendar proCtcAECalendar;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        proCtcAECalendar = new ProCtcAECalendar();
    }

    public void testIsDateWithinMonth() {

        Date d = new Date();
        assertTrue(proCtcAECalendar.isDateWithinMonth(d));
        Calendar c = Calendar.getInstance();
        c.set(2009, 2, 1);
        d = c.getTime();
//        assertFalse(proCtcAECalendar.isDateWithinMonth(d));
    }
}