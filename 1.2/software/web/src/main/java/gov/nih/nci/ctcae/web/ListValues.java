package gov.nih.nci.ctcae.web;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.ctcae.core.domain.*;

import java.util.*;

//
/**
 * The Class ListValues.
 *
 * @author Vinay Kumar
 * @since Oct 18, 2008
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
    public static List<ListValues> getGenderType() {
        return getEnumTypes(Gender.class);
    }


    public static List<ListValues> getRoleStatusType() {
        return getEnumTypes(RoleStatus.class);
    }

    private static List<ListValues> getEnumTypes(final Class<? extends CodedEnum> enumClass) {
        List<ListValues> valuesList = new ArrayList<ListValues>();
        valuesList.add(new ListValues("", "Please select"));

        CodedEnum[] constants = enumClass.getEnumConstants();

        for (CodedEnum codedEnum : constants) {
            valuesList.add(new ListValues(codedEnum.getDisplayName(), codedEnum.getDisplayName()));
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

    public static List<ListValues> getNotificationRequired() {
        List<ListValues> valuesList = new ArrayList<ListValues>();

        valuesList.add(new ListValues("false", "Please select"));
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
        valuesList.add(new ListValues("other", "other"));
        return valuesList;
    }


    /**
     * Gets the study sites.
     *
     * @param organizations the organizations
     * @return the study sites
     */
    public static List<ListValues> getOrganizationsHavingStudySite(Set<Organization> organizations) {
        List<ListValues> valuesList = new ArrayList<ListValues>();
        if (organizations.size() > 1) {
            valuesList.add(new ListValues("", "Please select"));
        }
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
//        ListValues lov0 = new ListValues("", "Please select");
        ListValues lov1 = new ListValues("Days", "Days");
        ListValues lov2 = new ListValues("Weeks", "Weeks");
        ListValues lov3 = new ListValues("Months", "Months");
//        col.add(lov0);
        col.add(lov1);
        col.add(lov2);
        col.add(lov3);
        return col;
    }

    public static List<ListValues> getCyclePlannedRepetitions() {
        List<ListValues> col = new ArrayList<ListValues>();
        ListValues lov2 = new ListValues("-2", "Number of times");
        ListValues lov3 = new ListValues("-1", "Indefinite");
        col.add(lov2);
        col.add(lov3);
        return col;
    }

    public static List<ListValues> getCalendarRepeatUntilUnits() {
        List<ListValues> col = new ArrayList<ListValues>();
//        ListValues lov0 = new ListValues("", "Please select");
        ListValues lov1 = new ListValues("Date", "Date");
        ListValues lov2 = new ListValues("Number", "Number of repetitions");
        ListValues lov3 = new ListValues("Indefinitely", "Indefinitely");
//        col.add(lov0);
        col.add(lov3);
        col.add(lov1);
        col.add(lov2);
        return col;
    }

    public static List<ListValues> getCalendarDueDateUnits() {
        List<ListValues> col = new ArrayList<ListValues>();
//        ListValues lov0 = new ListValues("", "Please select");
        ListValues lov1 = new ListValues("Hours", "Hours");
        ListValues lov2 = new ListValues("Days", "Days");
        ListValues lov3 = new ListValues("Weeks", "Weeks");
//        col.add(lov0);
        col.add(lov1);
        col.add(lov2);
        col.add(lov3);
        return col;
    }


    public static List<ProCtcTerm> getSymptomsForCRF(CRF crf) {
        List<ProCtcTerm> allSymptoms = new ArrayList<ProCtcTerm>();
        HashSet<ProCtcTerm> proCtcTerms = new HashSet<ProCtcTerm>();
        for (CrfPageItem crfPageItem : crf.getAllCrfPageItems()) {
            proCtcTerms.add(crfPageItem.getProCtcQuestion().getProCtcTerm());
        }
        for (ProCtcTerm proCtcTerm : proCtcTerms) {
            allSymptoms.add(proCtcTerm);
        }
        Collections.sort(allSymptoms, new ProCtcTermComparator());
        return allSymptoms;
    }


    public static List<ListValues> getNotificationOptions() {
        List<ListValues> col = new ArrayList<ListValues>();
        ListValues lov1 = new ListValues(Role.TREATING_PHYSICIAN.getCode(), Role.TREATING_PHYSICIAN.getScreenText());
        ListValues lov2 = new ListValues(Role.NURSE.getCode(), Role.NURSE.getScreenText());
        ListValues lov3 = new ListValues(Role.SITE_CRA.getCode(), Role.SITE_CRA.getScreenText());
        ListValues lov4 = new ListValues(Role.SITE_PI.getCode(), Role.SITE_PI.getScreenText());
        ListValues lov5 = new ListValues(Role.LEAD_CRA.getCode(), Role.LEAD_CRA.getScreenText());
        ListValues lov6 = new ListValues(Role.PI.getCode(), Role.PI.getScreenText());
        col.add(lov1);
        col.add(lov2);
        col.add(lov3);
        col.add(lov4);
        col.add(lov5);
        col.add(lov6);
        return col;
    }


}
