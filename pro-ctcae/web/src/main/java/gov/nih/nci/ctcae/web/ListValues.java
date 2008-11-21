package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.domain.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Oct 18, 2008
 */
public class ListValues {

    private String code;

    private String desc;

    public ListValues() {
        // TODO Auto-generated constructor stub
    }

    public ListValues(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(final String desc) {
        this.desc = desc;
    }

    public List<ListValues> getStudySearchType() {
        List<ListValues> col = new ArrayList<ListValues>();
        ListValues lov1 = new ListValues("shortTitle", "Short title");
        ListValues lov3 = new ListValues("assignedIdentifier", "Study identifier");

        col.add(lov1);
        col.add(lov3);

        return col;
    }

    public List<ListValues> getGenderType() {
        List<ListValues> valuesList = new ArrayList<ListValues>();
        valuesList.add(new ListValues("", "Please select"));

        for (Gender gender : Gender.values()) {
            valuesList.add(new ListValues(gender.getDisplayText(), gender.getDisplayText()));
        }

        return valuesList;
    }

    public List<ListValues> getRaceType() {
        List<ListValues> valuesList = new ArrayList<ListValues>();
        valuesList.add(new ListValues("", "Please select"));

        for (Race race : Race.values()) {
            valuesList.add(new ListValues(race.getDisplayText(), race.getDisplayText()));
        }

        return valuesList;
    }

    public List<ListValues> getEthnicityType() {
        List<ListValues> valuesList = new ArrayList<ListValues>();
        valuesList.add(new ListValues("", "Please select"));

        for (Ethnicity ethnicity : Ethnicity.values()) {
            valuesList.add(new ListValues(ethnicity.getDisplayText(), ethnicity.getDisplayText()));
        }

        return valuesList;
    }

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

    public static List<ListValues> getResponseRequired() {
        List<ListValues> valuesList = new ArrayList<ListValues>();

        valuesList.add(new ListValues("true", "Yes"));
        valuesList.add(new ListValues("false", "No"));

        return valuesList;
    }


    public static List<ListValues> getStudySites(ArrayList<Organization> organizations) {
        List<ListValues> valuesList = new ArrayList<ListValues>();
        valuesList.add(new ListValues("", "Please select"));

        for (Organization organization : organizations) {
            valuesList.add(new ListValues(String.valueOf(organization.getId()), organization.getDisplayName()));
        }

        return valuesList;

    }


    public static List<ListValues> getCrfItemAllignments() {
        List<ListValues> valuesList = new ArrayList<ListValues>();

        for (CrfItemAllignment crfItemAllignment : CrfItemAllignment.values()) {
            valuesList.add(new ListValues(crfItemAllignment.getDisplayName(), crfItemAllignment.getDisplayName()));

        }
        return valuesList;


    }
}
