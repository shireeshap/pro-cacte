package gov.nih.nci.ctcae.commons.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * @author
 */
public class DateUtils extends edu.nwu.bioinformatics.commons.DateUtils {
    protected static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    protected static DateFormat dashedDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    protected static final Log logger = LogFactory.getLog(DateUtils.class);

    public static Date parseDate(String dateString) throws ParseException {

        return dateFormat.parse(dateString);

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
        return dateFormat.format(date);

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

}
