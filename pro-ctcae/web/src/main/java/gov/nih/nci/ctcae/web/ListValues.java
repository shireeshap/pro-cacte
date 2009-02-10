package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.domain.*;

import java.util.ArrayList;
import java.util.List;

//
/**
 * The Class ListValues.
 *
 * @author Vinay Kumar
 * @crated Oct 18, 2008
 */
public class ListValues {

    /**
     * The code.
     */
    private String code;

    /**
     * The desc.
     */
    private String desc;

    /**
     * Instantiates a new list values.
     */
    public ListValues() {
        super();
    }

    /**
     * Instantiates a new list values.
     *
     * @param code the code
     * @param desc the desc
     */
    public ListValues(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Gets the code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the code.
     *
     * @param code the new code
     */
    public void setCode(final String code) {
        this.code = code;
    }

    /**
     * Gets the desc.
     *
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Sets the desc.
     *
     * @param desc the new desc
     */
    public void setDesc(final String desc) {
        this.desc = desc;
    }

    /**
     * Gets the study search type.
     *
     * @return the study search type
     */
    public List<ListValues> getStudySearchType() {
        List<ListValues> col = new ArrayList<ListValues>();
        ListValues lov1 = new ListValues("shortTitle", "Short title");
        ListValues lov3 = new ListValues("assignedIdentifier", "Study identifier");

        col.add(lov1);
        col.add(lov3);

        return col;
    }

    /**
     * Gets the gender type.
     *
     * @return the gender type
     */
    public List<ListValues> getGenderType() {
        List<ListValues> valuesList = new ArrayList<ListValues>();
        valuesList.add(new ListValues("", "Please select"));

        for (Gender gender : Gender.values()) {
            valuesList.add(new ListValues(gender.getDisplayText(), gender.getDisplayText()));
        }

        return valuesList;
    }

    /**
     * Gets the race type.
     *
     * @return the race type
     */
    public List<ListValues> getRaceType() {
        List<ListValues> valuesList = new ArrayList<ListValues>();
        valuesList.add(new ListValues("", "Please select"));

        for (Race race : Race.values()) {
            valuesList.add(new ListValues(race.getDisplayText(), race.getDisplayText()));
        }

        return valuesList;
    }

    /**
     * Gets the ethnicity type.
     *
     * @return the ethnicity type
     */
    public List<ListValues> getEthnicityType() {
        List<ListValues> valuesList = new ArrayList<ListValues>();
        valuesList.add(new ListValues("", "Please select"));

        for (Ethnicity ethnicity : Ethnicity.values()) {
            valuesList.add(new ListValues(ethnicity.getDisplayText(), ethnicity.getDisplayText()));
        }

        return valuesList;
    }

    /**
     * Gets the participant search type.
     *
     * @return the participant search type
     */
    public List<ListValues> getParticipantSearchType() {
        List<ListValues> col = new ArrayList<ListValues>();
        ListValues lov1 = new ListValues("fn", "First name");
        ListValues lov2 = new ListValues("ln", "Last name");
        ListValues lov3 = new ListValues("idtf", "Identifier");
        // ListValues lov3 = new ListValues("g", "gender");
        // ListValues lov4 = new ListValues("r", "race");
        // ListValues lov5 = new ListValues("e", "ethnicity");
        col.add(lov1);
        col.add(lov2);
        col.add(lov3);
        // col.add(lov4);
        // col.add(lov5);
        return col;
    }

    /**
     * Gets the response required.
     *
     * @return the response required
     */
    public static List<ListValues> getResponseRequired() {
        List<ListValues> valuesList = new ArrayList<ListValues>();

        valuesList.add(new ListValues("true", "Yes"));
        valuesList.add(new ListValues("false", "No"));

        return valuesList;
    }

    /**
     * Gets the recall periods.
     *
     * @return the recall periods
     */
    public static List<ListValues> getRecallPeriods() {
        List<ListValues> valuesList = new ArrayList<ListValues>();

        for (RecallPeriod recallPeriod : RecallPeriod.values()) {
            valuesList.add(new ListValues(recallPeriod.getDisplayName(), recallPeriod.getDisplayName()));
        }


        return valuesList;
    }


    /**
     * Gets the study sites.
     *
     * @param organizations the organizations
     * @return the study sites
     */
    public static List<ListValues> getStudySites(ArrayList<Organization> organizations) {
        List<ListValues> valuesList = new ArrayList<ListValues>();
        valuesList.add(new ListValues("", "Please select"));

        for (Organization organization : organizations) {
            valuesList.add(new ListValues(String.valueOf(organization.getId()), organization.getDisplayName()));
        }

        return valuesList;

    }


    /**
     * Gets the crf item allignments.
     *
     * @return the crf item allignments
     */
    public static List<ListValues> getCrfItemAllignments() {
        List<ListValues> valuesList = new ArrayList<ListValues>();

        for (CrfItemAllignment crfItemAllignment : CrfItemAllignment.values()) {
            valuesList.add(new ListValues(crfItemAllignment.getDisplayName(), crfItemAllignment.getDisplayName()));

        }
        return valuesList;


    }

    public static List<ListValues> getCalendarRepetitionUnits() {
        List<ListValues> col = new ArrayList<ListValues>();
        ListValues lov1 = new ListValues("Days", "Days");
        ListValues lov2 = new ListValues("Weeks", "Weeks");
        ListValues lov3 = new ListValues("Months", "Months");
        col.add(lov1);
        col.add(lov2);
        col.add(lov3);
        return col;
    }

    public static List<ListValues> getCalendarRepeatUntilUnits() {
        List<ListValues> col = new ArrayList<ListValues>();
        ListValues lov1 = new ListValues("Date", "Date");
        ListValues lov2 = new ListValues("Number", "Number of repititions");
        ListValues lov3 = new ListValues("Indefinitely", "Indefinitely");
        col.add(lov1);
        col.add(lov2);
        col.add(lov3);
        return col;
    }

    public static List<ListValues> getCalendarDueDateUnits() {
        List<ListValues> col = new ArrayList<ListValues>();
        ListValues lov1 = new ListValues("Hours", "Hours");
        ListValues lov2 = new ListValues("Days", "Days");
        ListValues lov3 = new ListValues("Weeks", "Weeks");
        col.add(lov1);
        col.add(lov2);
        col.add(lov3);
        return col;
    }
}
