package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.PasswordPolicy;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//

/**
 * The Class ParticipantCommand.
 */
public class ParticipantCommand {

    /**
     * The participant.
     */
    private Participant participant;

    private int organizationId;

    private StudyParticipantAssignment selectedStudyParticipantAssignment;

    private Integer selectedStudyParticipantAssignmentId;
    /**
     * The study id.
     */
    private List<StudySite> studySites = new ArrayList<StudySite>();

    /**
     * The site name.
     */
    private String siteName;
    private PasswordPolicy passwordPolicy;

    private String mode;

    private List<StudyParticipantClinicalStaff> notificationStaffToRemove = new ArrayList<StudyParticipantClinicalStaff>();
    private Set<Organization> clinicalStaffOrgs = new HashSet<Organization>();
    private boolean readOnly = false;
    private boolean readOnlyUserName = true;

    private boolean odc;
    private boolean admin;
    private boolean edit = false;
    private List<String> participantModes = new ArrayList();

    private boolean email = false;
    private boolean call = false;
    private boolean text = false;

    private String callTimeZone;
    private String callAmPm;
    private Integer callHour;
    private Integer callMinute;
    private String reminderTimeZone;
    private String reminderAmPm;
    private Integer reminderHour;
    private Integer reminderMinute;

    public boolean isOdc() {
        return odc;
    }

    public void setOdc(boolean odc) {
        this.odc = odc;
    }

    /**
     * Instantiates a new participant command.
     */
    public ParticipantCommand() {
        super();
        participant = new Participant();
        participant.setUser(new User());
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
     * Gets the site id.
     *
     * @return the site id
     */
    public int getOrganizationId() {
        return organizationId;
    }

    /**
     * Sets the site id.
     *
     * @param organizationId the new site id
     */
    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }


    /**
     * Gets the site name.
     *
     * @return the site name
     */
    public String getSiteName() {
        return siteName;
    }

    /**
     * Sets the site name.
     *
     * @param siteName the new site name
     */
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public List<StudySite> getStudySites() {
        return studySites;
    }

    public void setStudySites(List<StudySite> studySites) {
        this.studySites = studySites;
    }

    public PasswordPolicy getPasswordPolicy() {
        return passwordPolicy;
    }

    public void setPasswordPolicy(PasswordPolicy passwordPolicy) {
        this.passwordPolicy = passwordPolicy;
    }

    public StudyParticipantAssignment createStudyParticipantAssignment(StudySite studySite, String studyParticipantIdentifier, String armId) {

        StudyParticipantAssignment studyParticipantAssignment = new StudyParticipantAssignment();
        studyParticipantAssignment.setStudySite(studySite);
        studyParticipantAssignment.setStudyParticipantIdentifier(studyParticipantIdentifier);

        for (Arm arm : studySite.getStudy().getArms()) {
            if (StringUtils.isBlank(armId)) {
                studyParticipantAssignment.setArm(studySite.getStudy().getArms().get(0));
            } else {
                if (arm.getId().equals(Integer.parseInt(armId))) {
                    studyParticipantAssignment.setArm(arm);
                }
            }
        }

        for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : studyParticipantAssignment.getStudySite().getStudyOrganizationClinicalStaffs()) {
            if (studyOrganizationClinicalStaff.getRole().equals(Role.SITE_PI) || studyOrganizationClinicalStaff.getRole().equals(Role.SITE_CRA)) {
                StudyParticipantClinicalStaff studyParticipantClinicalStaff = new StudyParticipantClinicalStaff();
                studyParticipantClinicalStaff.setPrimary(false);
                studyParticipantClinicalStaff.setNotify(true);
                studyParticipantClinicalStaff.setStudyOrganizationClinicalStaff(studyOrganizationClinicalStaff);
                studyParticipantAssignment.addStudyParticipantClinicalStaff(studyParticipantClinicalStaff);
            }
        }
        participant.addStudyParticipantAssignment(studyParticipantAssignment);
        return studyParticipantAssignment;
    }

    public void assignCrfsToParticipant(StudyParticipantAssignment studyParticipantAssignment, CRFRepository crfRepository, HttpServletRequest request, String studyStartDate) throws ParseException {
        Study study = studyParticipantAssignment.getStudySite().getStudy();
        for (CRF crf : study.getCrfs()) {
            crf = crfRepository.findById(crf.getId());
            if (crf.getStatus().equals(CrfStatus.RELEASED)) {
                if (crf.getChildCrf() == null || crf.getChildCrf().getStatus().equals(CrfStatus.DRAFT)) {
                    StudyParticipantCrf studyParticipantCrf = new StudyParticipantCrf();
                    String sDate = studyStartDate;
                    if (!StringUtils.isBlank(sDate)) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        studyParticipantCrf.setStartDate(simpleDateFormat.parse(sDate));
                    } else {
                        studyParticipantCrf.setStartDate(crf.getEffectiveStartDate());
                    }
                    studyParticipantCrf.setCrf(crf);
                    studyParticipantAssignment.addStudyParticipantCrf(studyParticipantCrf);
                    studyParticipantCrf.createSchedules();
                }
            }
        }
    }

    public void apply(CRFRepository crfRepository, HttpServletRequest request) throws ParseException {

        //if new participant then clear all study participant assignments. otherwise it will create problem in case of any validation error.
        if (!participant.isPersisted()) {
            participant.removeAllStudyParticipantAssignments();
            for (StudySite studySite : getStudySites()) {
                setSiteName(studySite.getOrganization().getName());
                StudyParticipantAssignment studyParticipantAssignment = createStudyParticipantAssignment(studySite, request.getParameter("participantStudyIdentifier_" + studySite.getId()), request.getParameter("arm_" + studySite.getId()));
                setParticipantModesAndReminders(studySite, studyParticipantAssignment, request);
                String studyStartDate = request.getParameter("study_date_" + studySite.getId());
                if (!StringUtils.isBlank(studyStartDate)) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    studyParticipantAssignment.setStudyStartDate(simpleDateFormat.parse(studyStartDate));
                }
                assignCrfsToParticipant(studyParticipantAssignment, crfRepository, request, studyStartDate);
            }
        } else {
            for (StudyParticipantAssignment studyParticipantAssignment : participant.getStudyParticipantAssignments()) {
                setParticipantModesAndReminders(studyParticipantAssignment.getStudySite(), studyParticipantAssignment, request);
                String studyParticipantIdentifier = request.getParameter("participantStudyIdentifier_" + studyParticipantAssignment.getStudySite().getId());
                if (!StringUtils.isBlank(studyParticipantIdentifier)) {
                    studyParticipantAssignment.setStudyParticipantIdentifier(studyParticipantIdentifier);
                }
            }
        }
    }

    public void setParticipantModesAndReminders(StudySite studySite, StudyParticipantAssignment studyParticipantAssignment, HttpServletRequest request) {
        String participantMode = request.getParameter("participantModes_" + studySite.getId());
        if (participantMode != null) {
            participantModes.add(participantMode);
        }
        String participantClinicMode = request.getParameter("participantClinicModes_" + studySite.getId());
        if (participantClinicMode != null) {
            participantModes.add(participantClinicMode);
        }
        Boolean email = ServletRequestUtils.getBooleanParameter(request, "email_" + studySite.getId(), false);
        Boolean call = ServletRequestUtils.getBooleanParameter(request, "call_" + studySite.getId(), false);
        Boolean text = ServletRequestUtils.getBooleanParameter(request, "text_" + studySite.getId(), false);
        Integer callHour = ServletRequestUtils.getIntParameter(request, "call_hour_" + studySite.getId(), 1);
        Integer callMinute = ServletRequestUtils.getIntParameter(request, "call_minute_" + studySite.getId(), 5);
        String callAmPm = request.getParameter("call_ampm_" + studySite.getId());
        String callTimeZone = request.getParameter("call_timeZone_" + studySite.getId());
        Integer reminderHour = ServletRequestUtils.getIntParameter(request, "reminder_hour_" + studySite.getId(), 1);
        Integer reminderMinute = ServletRequestUtils.getIntParameter(request, "reminder_minute_" + studySite.getId(), 5);
        String reminderAmPm = request.getParameter("reminder_ampm_" + studySite.getId());
        String reminderTimeZone = request.getParameter("reminder_timeZone_" + studySite.getId());
        if (participantMode != null) {
            if (participantMode.equals("HOMEWEB")) {
                callHour = null;
                callMinute = null;
                callAmPm = null;
                callTimeZone = null;
                reminderAmPm = null;
                reminderHour = null;
                reminderMinute = null;
                reminderTimeZone = null;
            }
        } else {
            callHour = null;
            callMinute = null;
            callAmPm = null;
            callTimeZone = null;
            reminderAmPm = null;
            reminderHour = null;
            reminderMinute = null;
            reminderTimeZone = null;
        }
        studyParticipantAssignment.setCallAmPm(callAmPm);
        studyParticipantAssignment.setCallHour(callHour);
        studyParticipantAssignment.setCallMinute(callMinute);
        studyParticipantAssignment.setCallTimeZone(callTimeZone);
        studyParticipantAssignment.setReminderAmPm(reminderAmPm);
        studyParticipantAssignment.setReminderHour(reminderHour);
        studyParticipantAssignment.setReminderMinute(reminderMinute);
        studyParticipantAssignment.setReminderTimeZone(reminderTimeZone);
        studyParticipantAssignment.getStudyParticipantModes().clear();
        if (getParticipantModes().size() > 0) {
            for (String string : getParticipantModes()) {
                AppMode mode = AppMode.valueOf(string);
                if (mode != null) {
                    StudyParticipantMode studyParticipantMode = new StudyParticipantMode();
                    studyParticipantMode.setMode(mode);
                    studyParticipantMode.setEmail(email);
                    studyParticipantMode.setCall(call);
                    studyParticipantMode.setText(text);
                    studyParticipantAssignment.addStudyParticipantMode(studyParticipantMode);
                }
            }
        }

    }

    public StudyParticipantAssignment getSelectedStudyParticipantAssignment() {

        List<StudyParticipantAssignment> studyParticipantAssignments = participant.getStudyParticipantAssignments();
        if (studyParticipantAssignments.get(0).getId() != null) {
            for (StudyParticipantAssignment studyParticipantAssignment : studyParticipantAssignments) {
                if (studyParticipantAssignment.getId().equals(selectedStudyParticipantAssignmentId)) {
                    return studyParticipantAssignment;
                }
            }
        } else {
            selectedStudyParticipantAssignment = studyParticipantAssignments.get(0);
        }

        return selectedStudyParticipantAssignment;
    }


    public void assignStaff() {
        for (StudyParticipantAssignment studyParticipantAssignment : getParticipant().getStudyParticipantAssignments()) {
            studyParticipantAssignment.addStudyParticipantClinicalStaff(studyParticipantAssignment.getTreatingPhysician());
            studyParticipantAssignment.addStudyParticipantClinicalStaff(studyParticipantAssignment.getResearchNurse());
            for (StudyParticipantClinicalStaff studyParticipantClinicalStaff : studyParticipantAssignment.getNotificationClinicalStaff()) {
                studyParticipantAssignment.addStudyParticipantClinicalStaff(studyParticipantClinicalStaff);
            }

            for (StudyParticipantClinicalStaff studyParticipantClinicalStaff : notificationStaffToRemove) {
                studyParticipantAssignment.getStudyParticipantClinicalStaffs().remove(studyParticipantClinicalStaff);
            }
            notificationStaffToRemove.clear();
        }
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void addNotificationStaffToRemove(StudyParticipantClinicalStaff studyParticipantClinicalStaff) {
        notificationStaffToRemove.add(studyParticipantClinicalStaff);
    }

    public Set<Organization> getClinicalStaffOrgs() {
        return clinicalStaffOrgs;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnlyUserName(boolean readOnlyUserName) {
        this.readOnlyUserName = readOnlyUserName;
    }

    public boolean isReadOnlyUserName() {
        return readOnlyUserName;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAdmin() {
        return admin;
    }

    public boolean getAdmin() {
        return admin;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public boolean isEdit() {
        return edit;
    }

    public List<String> getParticipantModes() {
        return participantModes;
    }

    public void setParticipantModes(List<String> participantModes) {
        this.participantModes = participantModes;
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean getEmail() {
        return email;
    }


    public boolean isCall() {
        return call;
    }

    public boolean getCall() {
        return call;
    }

    public void setCall(boolean call) {
        this.call = call;
    }

    public boolean isText() {
        return text;
    }

    public boolean getText() {
        return text;
    }

    public void setText(boolean text) {
        this.text = text;
    }

    public Integer getSelectedStudyParticipantAssignmentId() {
        return selectedStudyParticipantAssignmentId;
    }

    public void setSelectedStudyParticipantAssignmentId(Integer selectedStudyParticipantAssignmentId) {
        this.selectedStudyParticipantAssignmentId = selectedStudyParticipantAssignmentId;
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

    public String getReminderTimeZone() {
        return reminderTimeZone;
    }

    public void setReminderTimeZone(String reminderTimeZone) {
        this.reminderTimeZone = reminderTimeZone;
    }

    public String getReminderAmPm() {
        return reminderAmPm;
    }

    public void setReminderAmPm(String reminderAmPm) {
        this.reminderAmPm = reminderAmPm;
    }

    public Integer getReminderHour() {
        return reminderHour;
    }

    public void setReminderHour(Integer reminderHour) {
        this.reminderHour = reminderHour;
    }

    public Integer getReminderMinute() {
        return reminderMinute;
    }

    public void setReminderMinute(Integer reminderMinute) {
        this.reminderMinute = reminderMinute;
    }
}
