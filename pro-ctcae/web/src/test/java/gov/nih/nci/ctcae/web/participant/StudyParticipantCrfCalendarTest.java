package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.web.WebTestCase;

import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * @author Harsh Agarwal
 * @crated Nov 24, 2008
 */
public class StudyParticipantCrfCalendarTest extends WebTestCase {

    StudyParticipantCrfCalendar studyParticipantCrfCalendar;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        studyParticipantCrfCalendar = new StudyParticipantCrfCalendar();
    }

    public void testIsDateWithinMonth() {

        Date d = new Date();
        assertTrue(studyParticipantCrfCalendar.isDateWithinMonth(d));
        Calendar c = Calendar.getInstance();
        c.set(2009, 2, 1);
        d = c.getTime();
        assertFalse(studyParticipantCrfCalendar.isDateWithinMonth(d));
    }
}