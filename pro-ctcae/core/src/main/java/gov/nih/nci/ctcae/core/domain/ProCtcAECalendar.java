package gov.nih.nci.ctcae.core.domain;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//
/**
 * User: Harsh
 * Date: Jan 19, 2009
 * Time: 12:13:05 PM.
 */
public class ProCtcAECalendar {

    /**
     * The html calendar.
     */
    private static List<List<String>> htmlCalendar = new ArrayList<List<String>>();

    /**
     * The calendar.
     */
    private Calendar calendar;
    private int cycleNumber;
    private int cycleDay;

    /**
     * The repetition period amount.
     */
    private int repetitionPeriodAmount;

    /**
     * The repetition period unit.
     */
    private String repetitionPeriodUnit;

    /**
     * The due date amount.
     */
    private int dueDateAmount;

    /**
     * The due date unit.
     */
    private String dueDateUnit;

    /**
     * The repeat on.
     */
    private String repeatOn;

    /**
     * The repeat until unit.
     */
    private String repeatUntilUnit;

    /**
     * The repeat until value.
     */
    private String repeatUntilValue;

    /**
     * The start date.
     */
    private Date startDate;

    /**
     * The due after period in mill.
     */
    private int dueAfterPeriodInMill;

    /**
     * The number of repetitions.
     */
    private int numberOfRepetitions;

    /**
     * The current index.
     */
    private int currentIndex;

    /**
     * The temp.
     */
    private Calendar temp;

    private int cycleLength;
    private String cycleSelectedDays;
    private int cycleRepetitionNumber;
    private String cycleLenghtUnit;

    /**
     * Instantiates a new pro ctc ae calendar.
     */
    public ProCtcAECalendar() {
        super();
        calendar = new GregorianCalendar();
        startDate = new Date();
        calendar.set(Calendar.DATE, 1);
    }


    /**
     * Gets the html calendar.
     *
     * @return the html calendar
     */
    public List<List<String>> getHtmlCalendar() {
        generateCalendar();
        return htmlCalendar;
    }

    /**
     * Generate calendar.
     */
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

    /**
     * Checks if is date within month.
     *
     * @param date the date
     * @return true, if is date within month
     */
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

    /**
     * Adds the.
     *
     * @param amount the amount
     */
    public void add(int amount) {
        calendar.add(Calendar.MONTH, amount);
    }

    /**
     * Gets the time.
     *
     * @return the time
     */
    public Date getTime() {
        return calendar.getTime();
    }

    /**
     * Gets the number of repetitions.
     *
     * @param repeatUntil      the repeat until
     * @param repeatUntilValue the repeat until value
     * @param startDate        the start date
     * @param repeatEvery      the repeat every
     * @param repeatEveryValue the repeat every value
     * @return the number of repetitions
     * @throws ParseException the parse exception
     */
    private int getNumberOfRepetitions(String repeatUntil, String repeatUntilValue, Date startDate, String repeatEvery, int repeatEveryValue) throws ParseException {
        int numberOfRepetitions = 0;
        if ("Number".equals(repeatUntil)) {
            numberOfRepetitions = Integer.parseInt(repeatUntilValue);
        }
        if ("Indefinitely".equals(repeatUntil)) {
            numberOfRepetitions = 100;
        }
        if ("Date".equals(repeatUntil)) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date endDate = sdf.parse(repeatUntilValue);
            int numberOfDays = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
            if ("Days".equals(repeatEvery)) {
                numberOfRepetitions = (numberOfDays / repeatEveryValue) + 1;
            }
            if ("Weeks".equals(repeatEvery)) {
                numberOfRepetitions = numberOfDays / 7;
            }
            if ("Months".equals(repeatEvery)) {
                numberOfRepetitions = numberOfDays / 30;
            }
        }
        return numberOfRepetitions;

    }

    /**
     * Prepare schedules.
     *
     * @throws ParseException the parse exception
     */
    public void prepareSchedules(ParticipantSchedule.ScheduleType scheduleType) throws ParseException {
        if (scheduleType.equals(ParticipantSchedule.ScheduleType.GENERAL)) {
            numberOfRepetitions = getNumberOfRepetitions(repeatUntilUnit, repeatUntilValue, startDate, repetitionPeriodUnit, repetitionPeriodAmount);
            dueAfterPeriodInMill = getDuePeriodInMillis(dueDateUnit, dueDateAmount);
        }
        if (scheduleType.equals(ParticipantSchedule.ScheduleType.CYCLE)) {
            String[] selectedDays = cycleSelectedDays.split(",");
            numberOfRepetitions = selectedDays.length * cycleRepetitionNumber;
            dueAfterPeriodInMill = 24 * 60 * 60 * 1000;
        }
        temp = getCalendarForDate(startDate);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.MONTH, temp.get(Calendar.MONTH));
        calendar.set(Calendar.YEAR, temp.get(Calendar.YEAR));

        currentIndex = 0;
    }

    /**
     * Checks for more schedules.
     *
     * @return true, if successful
     */
    public boolean hasMoreSchedules() {
        return currentIndex < numberOfRepetitions;
    }

    /**
     * Gets the next scehdule.
     *
     * @return the next scehdule
     */
    public Calendar getNextGeneralScehdule() {
        if (currentIndex > 0) {
            if ("Days".equals(repetitionPeriodUnit)) {
                temp.add(Calendar.DATE, repetitionPeriodAmount);
            }
            if ("Weeks".equals(repetitionPeriodUnit)) {
                temp.add(Calendar.WEEK_OF_MONTH, repetitionPeriodAmount);
            }
            if ("Months".equals(repetitionPeriodUnit)) {
                temp.add(Calendar.MONTH, repetitionPeriodAmount);
            }
        }
        currentIndex++;
        return temp;
    }

    public Calendar getNextCycleScehdule() {
        if (!StringUtils.isBlank(cycleSelectedDays)) {
            String[] selectedDays = cycleSelectedDays.split(",");
            int index = currentIndex % selectedDays.length;
            int add = 0;
            int currentday = Integer.parseInt(selectedDays[index]);
            if (index == 0) {
                int lastday = Integer.parseInt(selectedDays[selectedDays.length - 1]);
                if (currentIndex == 0) {
                    add = currentday - 1;
                } else {
                    add = cycleLength - lastday + currentday;
                }
            } else {
                int previousday = Integer.parseInt(selectedDays[index - 1]);
                add = currentday - previousday;
            }

            temp.add(Calendar.DATE, add);
            currentIndex++;
            return temp;
        }
        return null;
    }

    /**
     * Gets the calendar for date.
     *
     * @param date the date
     * @return the calendar for date
     */
    public static Calendar getCalendarForDate(Date date) {
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

        int month = Integer.parseInt(sdfMonth.format(date));
        int day = Integer.parseInt(sdfDay.format(date));
        int year = Integer.parseInt(sdfYear.format(date));

        Calendar c1 = Calendar.getInstance();
        c1.set(year, month - 1, day);
        c1.set(Calendar.AM_PM, 0);
        c1.set(Calendar.HOUR, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);
        return c1;
    }


    /**
     * Gets the due period in millis.
     *
     * @param dueAfter      the due after
     * @param dueAfterValue the due after value
     * @return the due period in millis
     */
    private int getDuePeriodInMillis(String dueAfter, int dueAfterValue) {
        int dueAfterPeriodInMillis = 0;
        if ("Hours".equals(dueAfter)) {
            dueAfterPeriodInMillis = dueAfterValue * 60 * 60 * 1000;
        }
        if ("Days".equals(dueAfter)) {
            dueAfterPeriodInMillis = dueAfterValue * 24 * 60 * 60 * 1000;
        }
        if ("Weeks".equals(dueAfter)) {
            dueAfterPeriodInMillis = dueAfterValue * 7 * 24 * 60 * 60 * 1000;
        }
        return dueAfterPeriodInMillis;
    }

    /**
     * Gets the calendar.
     *
     * @return the calendar
     */
    public Calendar getCalendar() {
        return calendar;
    }

    /**
     * Sets the calendar.
     *
     * @param calendar the new calendar
     */
    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    /**
     * Gets the repetition period amount.
     *
     * @return the repetition period amount
     */
    public int getRepetitionPeriodAmount() {
        return repetitionPeriodAmount;
    }

    /**
     * Sets the repetition period amount.
     *
     * @param repetitionPeriodAmount the new repetition period amount
     */
    public void setRepetitionPeriodAmount(int repetitionPeriodAmount) {
        this.repetitionPeriodAmount = repetitionPeriodAmount;
    }

    /**
     * Gets the repetition period unit.
     *
     * @return the repetition period unit
     */
    public String getRepetitionPeriodUnit() {
        return repetitionPeriodUnit;
    }

    /**
     * Sets the repetition period unit.
     *
     * @param repetitionPeriodUnit the new repetition period unit
     */
    public void setRepetitionPeriodUnit(String repetitionPeriodUnit) {
        this.repetitionPeriodUnit = repetitionPeriodUnit;
    }

    /**
     * Gets the due date amount.
     *
     * @return the due date amount
     */
    public int getDueDateAmount() {
        return dueDateAmount;
    }

    /**
     * Sets the due date amount.
     *
     * @param dueDateAmount the new due date amount
     */
    public void setDueDateAmount(int dueDateAmount) {
        this.dueDateAmount = dueDateAmount;
    }

    /**
     * Gets the due date unit.
     *
     * @return the due date unit
     */
    public String getDueDateUnit() {
        return dueDateUnit;
    }

    /**
     * Sets the due date unit.
     *
     * @param dueDateUnit the new due date unit
     */
    public void setDueDateUnit(String dueDateUnit) {
        this.dueDateUnit = dueDateUnit;
    }

    /**
     * Gets the repeat on.
     *
     * @return the repeat on
     */
    public String getRepeatOn() {
        return repeatOn;
    }

    /**
     * Sets the repeat on.
     *
     * @param repeatOn the new repeat on
     */
    public void setRepeatOn(String repeatOn) {
        this.repeatOn = repeatOn;
    }

    /**
     * Gets the repeat until unit.
     *
     * @return the repeat until unit
     */
    public String getRepeatUntilUnit() {
        return repeatUntilUnit;
    }

    /**
     * Sets the repeat until unit.
     *
     * @param repeatUntilUnit the new repeat until unit
     */
    public void setRepeatUntilUnit(String repeatUntilUnit) {
        this.repeatUntilUnit = repeatUntilUnit;
    }

    /**
     * Gets the repeat until value.
     *
     * @return the repeat until value
     */
    public String getRepeatUntilValue() {
        return repeatUntilValue;
    }

    /**
     * Sets the repeat until value.
     *
     * @param repeatUntilValue the new repeat until value
     */
    public void setRepeatUntilValue(String repeatUntilValue) {
        this.repeatUntilValue = repeatUntilValue;
    }

    /**
     * Gets the start date.
     *
     * @return the start date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date.
     *
     * @param startDate the new start date
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the due after period in mill.
     *
     * @return the due after period in mill
     */
    public int getDueAfterPeriodInMill() {
        return dueAfterPeriodInMill;
    }


    public void setGeneralScheduleParameters(int repetitionPeriodAmount, String repetitionPeriodUnit, int dueDateAmount, String dueDateUnit, String repeatUntilUnit, String repeatUntilValue, Date startDate) {
        this.repetitionPeriodAmount = repetitionPeriodAmount;
        this.repetitionPeriodUnit = repetitionPeriodUnit;
        this.dueDateAmount = dueDateAmount;
        this.dueDateUnit = dueDateUnit;
        this.repeatUntilUnit = repeatUntilUnit;
        this.repeatUntilValue = repeatUntilValue;
        this.startDate = startDate;
        calendar = getCalendarForDate(startDate);
    }

    public void setCycleParameters(int cycleLength, String cycleSelectedDays, int cycleRepetitionNumber, String cycleLengthUnit, Date startDate, int cycleNumber) {
        this.cycleLength = getDaysForUnit(cycleLength, cycleLengthUnit);
        if (cycleSelectedDays.indexOf(",") == 0) {
            this.cycleSelectedDays = cycleSelectedDays.substring(1);
        } else {
            this.cycleSelectedDays = cycleSelectedDays;
        }
        this.cycleRepetitionNumber = cycleRepetitionNumber;
        this.startDate = startDate;
        this.calendar = getCalendarForDate(startDate);
        this.cycleNumber = cycleNumber;

    }

    public static void incrementCalendar(Calendar c, int cycleLength, String cycleLengthUnit) {
        int days = getDaysForUnit(cycleLength, cycleLengthUnit);
        c.add(Calendar.DATE, days);
    }

    private static int getDaysForUnit(int cycleLength, String cycleLengthUnit) {
        int multiplier = 1;
        if (cycleLengthUnit.equals("Weeks")) {
            multiplier = 7;
        }
        if (cycleLengthUnit.equals("Months")) {
            multiplier = 30;
        }
        return cycleLength * multiplier;

    }

    public int getCycleNumber() {
        return cycleNumber;
    }

    public int getCycleDay() {
        return cycleDay;
    }

}