package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.domain.Gender;

import java.util.List;
import java.util.ArrayList;

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
        ListValues lov1 = new ListValues("shortTitle", "Short Title");
        ListValues lov3 = new ListValues("assignedIdentifier", "Assigned Identifier");

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

    public List<ListValues> getParticipantSearchType() {
        List<ListValues> col = new ArrayList<ListValues>();
        ListValues lov1 = new ListValues("fn", "First Name");
        ListValues lov2 = new ListValues("ln", "Last Name");
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


}
