package gov.nih.nci.ctcae.core.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

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
     * The Due date.
     */
    private Date dueDate;

    /**
     * The due after period in mill.
     */
    private long dueAfterPeriodInMill;

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

        return calendar.get(Calendar.MONTH) + 1 == month && calendar.get(Calendar.YEAR) == year;

    }

    public boolean isDateAfterMonth(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
        String monYear = sdf.format(date);

        int month = Integer.parseInt(monYear.substring(0, monYear.indexOf('-')));
        int year = Integer.parseInt(monYear.substring(monYear.indexOf('-') + 1));

        return calendar.get(Calendar.MONTH) + 1 < month && calendar.get(Calendar.YEAR) == year;

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
    
    //IMP: returns 1-12......modified for UI.
    public int getMonth() {
        return calendar.get(Calendar.MONTH) + 1;
    }
    
    //returns current year -1900
    public int getYear() {
        return calendar.get(Calendar.YEAR);
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
            dueAfterPeriodInMill = getDuePeriodInMillis(dueDateUnit, dueDateAmount);
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
            int add;
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
            cycleDay = currentday;
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
    private long getDuePeriodInMillis(String dueAfter, int dueAfterValue) {
        long dueAfterPeriodInMillis = 0;
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
     * Gets the due Date for the given Calendar Date.
     *  @param c      the due after
     * @param dueAfter      the due after
     * @param dueAfterValue the due after value
     * @return the due date
     */
    public Date getDueDateForCalendarDate(Calendar c, String dueAfter, int dueAfterValue) {        
        Calendar tempCalendar = (Calendar)c.clone();
        tempCalendar.setTimeInMillis(c.getTimeInMillis());
        if ("Hours".equals(dueAfter)) {
            tempCalendar.add(Calendar.HOUR,dueAfterValue-1);
        }else if ("Days".equals(dueAfter)) {
            tempCalendar.add(Calendar.DATE,dueAfterValue-1);
        }
        else if ("Weeks".equals(dueAfter)) {
            tempCalendar.add(Calendar.WEEK_OF_YEAR,dueAfterValue-1);
        }

        return tempCalendar.getTime();
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
    public long getDueAfterPeriodInMill() {
        return dueAfterPeriodInMill;
    }


    public void setGeneralScheduleParameters(CRFCalendar crfCalendar, Date calendarStartDate) {

        this.repetitionPeriodAmount = Integer.parseInt(crfCalendar.getRepeatEveryValue());
        this.repetitionPeriodUnit = crfCalendar.getRepeatEveryUnit();
        this.dueDateAmount = Integer.parseInt(crfCalendar.getDueDateValue());
        this.dueDateUnit = crfCalendar.getDueDateUnit();
        this.repeatUntilUnit = crfCalendar.getRepeatUntilUnit();
        this.repeatUntilValue = crfCalendar.getRepeatUntilValue();
        this.startDate = calendarStartDate;
        calendar = getCalendarForDate(startDate);
    }

    public void setCycleParameters(CRFCycle crfCycle, Date calendarStartDate, int inCycleNumber) {
        CRFCycleDefinition cycleDefinition = crfCycle.getCrfCycleDefinition();
        String cycleDays = crfCycle.getCycleDays();

        cycleLength = getDaysForUnit(cycleDefinition.getCycleLength(), cycleDefinition.getCycleLengthUnit());
        dueDateUnit = cycleDefinition.getDueDateUnit();
        String dueDateValue = cycleDefinition.getDueDateValue();
        if (StringUtils.isBlank(cycleDefinition.getDueDateUnit()) || StringUtils.isBlank(cycleDefinition.getDueDateValue())) {
            dueDateValue = "1";
            dueDateUnit = "Days";
        }
        dueDateAmount = Integer.parseInt(dueDateValue);

        if (!StringUtils.isBlank(cycleDays) && crfCycle.getCycleDays().indexOf(",") == 0) {
            cycleSelectedDays = crfCycle.getCycleDays().substring(1);
        } else {
            cycleSelectedDays = crfCycle.getCycleDays();
        }

        cycleRepetitionNumber = 1;
        startDate = calendarStartDate;
        calendar = getCalendarForDate(startDate);
        cycleNumber = inCycleNumber;

    }

    public Calendar incrementCalendar() {
        Calendar temp = getCalendarForDate(startDate);
        temp.add(Calendar.DATE, cycleLength);
        return temp;
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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

}