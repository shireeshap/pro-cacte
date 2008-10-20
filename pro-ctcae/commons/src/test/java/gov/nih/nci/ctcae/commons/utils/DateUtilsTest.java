package gov.nih.nci.ctcae.commons.utils;

import junit.framework.TestCase;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import edu.nwu.bioinformatics.commons.testing.CoreTestCase;

/**
 * @author
 * @created Sep 26, 2008
 */
public class DateUtilsTest extends CoreTestCase {

    Date futureDate = DateUtils.createDate(2012, Calendar.JULY, 13);
    Date pastDate = DateUtils.createDate(2008, Calendar.JANUARY, 21);

    public void testFormatDashedDate() throws ParseException {
        assertEquals("2012-07-13", DateUtils.formatDashedFormart(futureDate));
        assertEquals("2008-01-21", DateUtils.formatDashedFormart(pastDate));


    }

    public void testFormatDate() throws ParseException {
        assertEquals("07/13/2012", DateUtils.format(futureDate));
        assertEquals("01/21/2008", DateUtils.format(pastDate));


    }

    public void testParseDate() throws ParseException {
        assertEquals(Integer.valueOf(0), Integer.valueOf(DateUtils.compareDate(DateUtils.parseDate("07/13/2012"), futureDate)));
        assertEquals(Integer.valueOf(0), Integer.valueOf(DateUtils.compareDate(DateUtils.parseDate("01/21/2008"), pastDate)));


    }

    public void testParseDashedDate() throws ParseException {
        assertEquals(Integer.valueOf(0), Integer.valueOf(DateUtils.compareDate(DateUtils.parseDashedDate("2012-07-13"), futureDate)));
        assertEquals(Integer.valueOf(0), Integer.valueOf(DateUtils.compareDate(DateUtils.parseDashedDate("2008-01-21"), pastDate)));


    }

    public void testCompareDate() throws ParseException {

        assertEquals(Integer.valueOf(1), Integer.valueOf(DateUtils.compareDate(DateUtils.parseDashedDate("2012-07-14"), futureDate)));
        assertEquals(Integer.valueOf(1), Integer.valueOf(DateUtils.compareDate(DateUtils.parseDashedDate("2012-08-13"), futureDate)));
        assertEquals(Integer.valueOf(1), Integer.valueOf(DateUtils.compareDate(DateUtils.parseDashedDate("2013-07-13"), futureDate)));
        assertEquals(Integer.valueOf(-1), Integer.valueOf(DateUtils.compareDate(DateUtils.parseDashedDate("2008-01-20"), pastDate)));


    }
}
