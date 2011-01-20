package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.*;

//

/**
 * The Class StudyParticipantAssignment.
 *
 * @author
 * @since Oct 7, 2008
 */

@Entity
@Table(name = "STUDY_PARTICIPANT_ASSIGNMENTS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "SEQ_STUDY_PARTICIPANT_ASSIG_ID")})
public class StudyParticipantAssignment extends BaseVersionable {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    /**
     * The study participant identifier.
     */
    @Column(name = "study_participant_identifier", nullable = false)
    private String studyParticipantIdentifier;

    @Column(name = "off_treatment_date", nullable = true)
    private Date offTreatmentDate;

    @Column(name = "study_start_date", nullable = true)
    private Date studyStartDate;

    @Column(name = "on_hold_treatment_date", nullable = true)
    private Date onHoldTreatmentDate;

    @Column(name = "off_hold_treatment_date", nullable = true)
    private Date offHoldTreatmentDate;

    /**
     * The participant.
     */
    @JoinColumn(name = "participant_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Participant participant;

    /**
     * The study site.
     */
    @JoinColumn(name = "study_site_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private StudySite studySite;

    /**
     * The study participant crfs.
     */
    @OneToMany(mappedBy = "studyParticipantAssignment", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<StudyParticipantCrf> studyParticipantCrfs = new ArrayList<StudyParticipantCrf>();

    @OneToMany(mappedBy = "studyParticipantAssignment", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<StudyParticipantClinicalStaff> studyParticipantClinicalStaffs = new ArrayList<StudyParticipantClinicalStaff>();

    @Transient
    private StudyParticipantClinicalStaff treatingPhysician;
    @Transient
    private StudyParticipantClinicalStaff researchNurse;
    @Transient
    private List<StudyParticipantClinicalStaff> notificationClinicalStaff;
    @Transient
    private List<StudyParticipantClinicalStaff> sitePIs;
    @Transient
    private List<StudyParticipantClinicalStaff> siteCRAs;

    @ManyToOne
    @JoinColumn(name = "arm_id", nullable = false)
    private Arm arm;

    @OneToMany(mappedBy = "studyParticipantAssignment", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<StudyParticipantMode> studyParticipantModes = new ArrayList();

    @Column(name = "call_time_zone", nullable = true)
    private String callTimeZone;

    @Column(name = "call_am_pm", nullable = true)
    private String callAmPm;

    @Column(name = "call_hour", nullable = true)
    private Integer callHour;

    @Column(name = "call_minute", nullable = true)
    private Integer callMinute;

//    @Column(name = "reminder_time_zone", nullable = true)
//    private String reminderTimeZone;
//
//    @Column(name = "reminder_am_pm", nullable = true)
//    private String reminderAmPm;
//
//    @Column(name = "reminder_hour", nullable = true)
//    private Integer reminderHour;
//
//    @Column(name = "reminder_minute", nullable = true)
//    private Integer reminderMinute;

    @OneToMany(mappedBy = "studyParticipantAssignment", fetch = FetchType.LAZY)
    @OrderBy("id desc")
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<StudyParticipantReportingModeHistory> studyParticipantReportingModeHistoryItems = new ArrayList();

    /**
     * Instantiates a new study participant assignment.
     */
    public StudyParticipantAssignment() {
    }


    /* (non-Javadoc)
      * @see gov.nih.nci.ctcae.core.domain.Persistable#getId()
      */

    public Integer getId() {
        return id;
    }

    /* (non-Javadoc)
      * @see gov.nih.nci.ctcae.core.domain.Persistable#setId(java.lang.Integer)
      */

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the study participant identifier.
     *
     * @return the study participant identifier
     */
    public String getStudyParticipantIdentifier() {
        return studyParticipantIdentifier;
    }

    /**
     * Sets the study participant identifier.
     *
     * @param studyParticipantIdentifier the new study participant identifier
     */
    public void setStudyParticipantIdentifier(String studyParticipantIdentifier) {
        this.studyParticipantIdentifier = studyParticipantIdentifier;
    }

    /**
     * Gets the participant.
     *
     * @return the participant
     */
    public Participant getParticipant() {
        return participant;
    }

    /**
     * Sets the participant.
     *
     * @param participant the new participant
     */
    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    /**
     * Gets the study site.
     *
     * @return the study site
     */
    public StudySite getStudySite() {
        return studySite;
    }

    /**
     * Sets the study site.
     *
     * @param studySite the new study site
     */
    public void setStudySite(StudySite studySite) {
        this.studySite = studySite;
    }

    /**
     * Gets the study participant crfs.
     *
     * @return the study participant crfs
     */
    public List<StudyParticipantCrf> getStudyParticipantCrfs() {
        return studyParticipantCrfs;
    }

    /**
     * Adds the study participant crf.
     *
     * @param studyParticipantCrf the study participant crf
     */
    public void addStudyParticipantCrf(StudyParticipantCrf studyParticipantCrf) {
        if (studyParticipantCrf != null) {
            studyParticipantCrf.setStudyParticipantAssignment(this);
            studyParticipantCrfs.add(studyParticipantCrf);
        }
    }

    public Date getStudyStartDate() {
        return studyStartDate;
    }

    public void setStudyStartDate(Date studyStartDate) {
        this.studyStartDate = studyStartDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudyParticipantAssignment that = (StudyParticipantAssignment) o;

        if (participant != null ? !participant.equals(that.participant) : that.participant != null) return false;
        if (studyParticipantIdentifier != null ? !studyParticipantIdentifier.equals(that.studyParticipantIdentifier) : that.studyParticipantIdentifier != null)
            return false;
        if (studySite != null ? !studySite.equals(that.studySite) : that.studySite != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = studyParticipantIdentifier != null ? studyParticipantIdentifier.hashCode() : 0;
        result = 31 * result + (participant != null ? participant.hashCode() : 0);
        result = 31 * result + (studySite != null ? studySite.hashCode() : 0);
        return result;
    }

    public List<StudyParticipantClinicalStaff> getStudyParticipantClinicalStaffs() {
        return studyParticipantClinicalStaffs;
    }

    public void addStudyParticipantClinicalStaff(StudyParticipantClinicalStaff studyParticipantClinicalStaff) {
        if (studyParticipantClinicalStaff != null && studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff() != null) {
            StudyOrganization expectedStudyOrganization = studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getStudyOrganization();
            if (!expectedStudyOrganization.equals(this.getStudySite())) {
                String errorMessage = String.format("study site clinical staff belongs to study site %s. It does not belongs to study site %s of study participant assignment. " +
                        "So this study site clincal staff can not be added.",
                        expectedStudyOrganization, this.getStudySite());
                logger.error(errorMessage);
                throw new CtcAeSystemException(errorMessage);
            }
            studyParticipantClinicalStaff.setStudyParticipantAssignment(this);
            if (!studyParticipantClinicalStaff.isPersisted()) {
                getStudyParticipantClinicalStaffs().add(studyParticipantClinicalStaff);
            }
            logger.debug(String.format("added study participant clinical staff %s to study participant assignment %s", studyParticipantClinicalStaff.toString(), toString()));
        }
    }

    public StudyParticipantClinicalStaff getTreatingPhysician() {

        if (treatingPhysician == null) {
            treatingPhysician = getPrimaryByRole(Role.TREATING_PHYSICIAN);
        }
        if (treatingPhysician == null) {
            StudyParticipantClinicalStaff studyParticipantClinicalStaff = new StudyParticipantClinicalStaff();
            studyParticipantClinicalStaff.setPrimary(true);
            treatingPhysician = studyParticipantClinicalStaff;
        }
        return treatingPhysician;
    }

    public void setTreatingPhysician(StudyParticipantClinicalStaff treatingPhysician) {
        this.treatingPhysician = treatingPhysician;
    }

    public StudyParticipantClinicalStaff getResearchNurse() {
        if (researchNurse == null) {
            researchNurse = getPrimaryByRole(Role.NURSE);
        }
        if (researchNurse == null) {
            StudyParticipantClinicalStaff studyParticipantClinicalStaff = new StudyParticipantClinicalStaff();
            studyParticipantClinicalStaff.setPrimary(true);
            researchNurse = studyParticipantClinicalStaff;
        }
        return researchNurse;
    }

    public void setResearchNurse(StudyParticipantClinicalStaff researchNurse) {
        this.researchNurse = researchNurse;
    }

    public List<StudyParticipantClinicalStaff> getNotificationClinicalStaff() {
        if (notificationClinicalStaff == null) {
            notificationClinicalStaff = new ArrayList<StudyParticipantClinicalStaff>();
            for (StudyParticipantClinicalStaff studyParticipantClinicalStaff : studyParticipantClinicalStaffs) {
                if (!studyParticipantClinicalStaff.isPrimary()) {
                    if (studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getRole().equals(Role.NURSE) || studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getRole().equals(Role.TREATING_PHYSICIAN)) {
                        notificationClinicalStaff.add(studyParticipantClinicalStaff);
                    }
                }
            }
        }
        return notificationClinicalStaff;
    }

    public void addNotificationClinicalStaff(StudyParticipantClinicalStaff notificationClinicalStaff) {
        if (notificationClinicalStaff != null) {
            this.notificationClinicalStaff.add(notificationClinicalStaff);
        }
    }

    private StudyParticipantClinicalStaff getPrimaryByRole(Role role) {
        for (StudyParticipantClinicalStaff studyParticipantClinicalStaff : studyParticipantClinicalStaffs) {
            if (studyParticipantClinicalStaff.isPrimary()) {
                if (studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getRole().equals(role)) {
                    if (studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getRoleStatus().equals(RoleStatus.ACTIVE)) {
                        return studyParticipantClinicalStaff;
                    }
                }
            }
        }
        return null;
    }

    private List<StudyParticipantClinicalStaff> getListByRole(Role role) {

        List<StudyParticipantClinicalStaff> staff = new ArrayList<StudyParticipantClinicalStaff>();
        Set<StudyOrganizationClinicalStaff> myStaff = new HashSet<StudyOrganizationClinicalStaff>();

        for (StudyParticipantClinicalStaff studyParticipantClinicalStaff : studyParticipantClinicalStaffs) {
            if (studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getRole().equals(role)) {
                if (studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getRoleStatus().equals(RoleStatus.ACTIVE)) {
                    staff.add(studyParticipantClinicalStaff);
                    myStaff.add(studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff());
                }
            }
        }
        for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : getStudySite().getStudyOrganizationClinicalStaffs()) {
            if (studyOrganizationClinicalStaff.getRole().equals(role)) {
                if (studyOrganizationClinicalStaff.getRoleStatus().equals(RoleStatus.ACTIVE)) {
                    if (!myStaff.contains(studyOrganizationClinicalStaff)) {
                        StudyParticipantClinicalStaff studyParticipantClinicalStaff = new StudyParticipantClinicalStaff();
                        studyParticipantClinicalStaff.setStudyOrganizationClinicalStaff(studyOrganizationClinicalStaff);
                        studyParticipantClinicalStaff.setNotify(true);
                        addStudyParticipantClinicalStaff(studyParticipantClinicalStaff);
                        staff.add(studyParticipantClinicalStaff);
                    }
                }
            }
        }
        return staff;
    }

    public List<StudyParticipantClinicalStaff> getSitePIs() {
        if (sitePIs == null) {
            sitePIs = getListByRole(Role.SITE_PI);
        }
        return sitePIs;
    }


    public List<StudyParticipantClinicalStaff> getSiteCRAs() {
        if (siteCRAs == null) {
            siteCRAs = getListByRole(Role.SITE_CRA);
        }
        return siteCRAs;
    }

    public Date getOffTreatmentDate() {
        return offTreatmentDate;
    }

    public void setOffTreatmentDate(Date offTreatmentDate) {
        this.offTreatmentDate = offTreatmentDate;
    }

    public Arm getArm() {
        return arm;
    }

    public void setArm(Arm arm) {
        this.arm = arm;
    }

    public Date getOnHoldTreatmentDate() {
        return onHoldTreatmentDate;
    }

    public void setOnHoldTreatmentDate(Date onHoldTreatmentDate) {
        this.onHoldTreatmentDate = onHoldTreatmentDate;
    }

    public Date getOffHoldTreatmentDate() {
        return offHoldTreatmentDate;
    }

    public void setOffHoldTreatmentDate(Date offHoldTreatmentDate) {
        this.offHoldTreatmentDate = offHoldTreatmentDate;
    }

    public List<StudyParticipantMode> getStudyParticipantModes() {
        return studyParticipantModes;
    }


    public void addStudyParticipantMode(StudyParticipantMode studyParticipantMode) {
        if (studyParticipantMode != null) {
            studyParticipantMode.setStudyParticipantAssignment(this);
            studyParticipantModes.add(studyParticipantMode);
        }
    }

    public String getCallTimeZone() {
        return callTimeZone;
    }

    public void setCallTimeZone(String callTimeZone) {
        this.callTimeZone = callTimeZone;
    }

    public String getCallAmPm() {
        return callAmPm;
    }

    public void setCallAmPm(String callAmPm) {
        this.callAmPm = callAmPm;
    }

    public Integer getCallHour() {
        return callHour;
    }

    public void setCallHour(Integer callHour) {
        this.callHour = callHour;
    }

    public Integer getCallMinute() {
        return callMinute;
    }

    public void setCallMinute(Integer callMinute) {
        this.callMinute = callMinute;
    }

//    public String getReminderTimeZone() {
//        return reminderTimeZone;
//    }
//
//    public void setReminderTimeZone(String reminderTimeZone) {
//        this.reminderTimeZone = reminderTimeZone;
//    }
//
//    public String getReminderAmPm() {
//        return reminderAmPm;
//    }
//
//    public void setReminderAmPm(String reminderAmPm) {
//        this.reminderAmPm = reminderAmPm;
//    }
//
//    public Integer getReminderHour() {
//        return reminderHour;
//    }
//
//    public void setReminderHour(Integer reminderHour) {
//        this.reminderHour = reminderHour;
//    }
//
//    public Integer getReminderMinute() {
//        return reminderMinute;
//    }
//
//    public void setReminderMinute(Integer reminderMinute) {
//        this.reminderMinute = reminderMinute;
//    }

    @Transient
    public List<AppMode> getSelectedAppModes() {
        List<AppMode> appModes = new ArrayList();
        for (StudyParticipantMode studyParticipantMode : studyParticipantModes) {
            appModes.add(studyParticipantMode.getMode());
        }
        return appModes;
    }

    @Transient
    public List<StudyParticipantReportingModeHistory> getEligiblePartcipantModeReportingHistoryItems() {
        List<StudyParticipantReportingModeHistory> histItems = new ArrayList();
        for (StudyParticipantReportingModeHistory studyParticipantModeRptHistory : studyParticipantReportingModeHistoryItems) {
            if(studyParticipantModeRptHistory.getEffectiveEndDate()==null){
                histItems.add(studyParticipantModeRptHistory);
            }
        }
        return histItems;
    }

    public void addStudyParticipantModeHistory(StudyParticipantReportingModeHistory studyParticipantModeHistory) {
        if (studyParticipantModeHistory != null) {
            studyParticipantModeHistory.setStudyParticipantAssignment(this);
            studyParticipantReportingModeHistoryItems.add(studyParticipantModeHistory);
        }
    }

    public List<StudyParticipantReportingModeHistory> getStudyParticipantReportingModeHistoryItems() {
        return studyParticipantReportingModeHistoryItems;
    }

    public void setStudyParticipantReportingModeHistoryItems(List<StudyParticipantReportingModeHistory> studyParticipantReportingModeHistory) {
        this.studyParticipantReportingModeHistoryItems = studyParticipantReportingModeHistory;
    }
}

