package gov.nih.nci.ctcae.web.participant;

import java.util.*;
import java.text.SimpleDateFormat;

/**
 * User: Harsh
 * Date: Jan 19, 2009
 * Time: 12:13:05 PM
 */
public class StudyParticipantCrfCalendar {

    private static List<List<String>> htmlCalendar = new ArrayList<List<String>>();
    private Calendar calendar;

    public StudyParticipantCrfCalendar() {
        calendar = new GregorianCalendar();
    }

    public List<List<String>> getHtmlCalendar() {
        generateCalendar();
        return htmlCalendar;
    }

    private void generateCalendar() {
        htmlCalendar = new ArrayList<List<String>>();
        int numOfWeeks = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        int day = 1;
        for (int i = 0; i < numOfWeeks; i++) {
            ArrayList<String> l = new ArrayList<String>();
            for (int j = 0; j < 7; j++) {
                if (i == 0) {
                    if (j < (calendar.get(Calendar.DAY_OF_WEEK)) - 1) {
                        l.add("");
                        continue;
                    }
                }
                if (day <= lastDay) {
                    l.add("" + day++);
                } else {
                    for (; j < 7; j++) {
                        l.add("");
                    }
                }
            }
            htmlCalendar.add(l);
        }
    }

    public boolean isDateWithinMonth(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
        String monYear = sdf.format(date);

        int month = Integer.parseInt(monYear.substring(0, monYear.indexOf('-')));
        int year = Integer.parseInt(monYear.substring(monYear.indexOf('-') + 1));

        if (calendar.get(Calendar.MONTH) + 1 == month && calendar.get(Calendar.YEAR) == year) {
            return true;
        }

        return false;
    }

    public void add(int amount) {
        calendar.add(Calendar.MONTH, amount);
    }

    public Date getTime() {
        return calendar.getTime();
    }

}