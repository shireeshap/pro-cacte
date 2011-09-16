package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.PasswordPolicy;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

/**
 * The Class ParticipantCommand.
 *
 * @author Mehul, Suneel, Vinay G
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


    private Map<Integer, String> studySubjectIdentifierMap = new HashMap<Integer, String>();

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
    private List<String> participantModes = new ArrayList<String>();

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

    private Date onHoldTreatmentDate;  //BJ - added for capturing the dates for OnHold (see ParticipantOnHoldController)
    private Date offHoldTreatmentDate; //BJ - added for capturing the dates for OffHold (see ParticipantOffHoldController)
    private Date newStartDate;
    private Integer armId;

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

    public Date getNewStartDate() {
        return newStartDate;
    }

    public void setNewStartDate(Date newStartDate) {
        this.newStartDate = newStartDate;
    }

    public Integer getArmId() {
        return armId;
    }

    public void setArmId(Integer armId) {
        this.armId = armId;
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

    public void assignArm(StudySite studySite) {

    }

    public void assignCrfsToParticipant() throws ParseException {
        StudyParticipantAssignment studyParticipantAssignment = getSelectedStudyParticipantAssignment();
        Date studyStartDate = studyParticipantAssignment.getStudyStartDate();
        Study study = studyParticipantAssignment.getStudySite().getStudy();
        for (CRF crf : study.getCrfs()) {
            if (crf.getStatus().equals(CrfStatus.RELEASED)) {
                if (crf.getChildCrf() == null || crf.getChildCrf().getStatus().equals(CrfStatus.DRAFT)) {
                    StudyParticipantCrf studyParticipantCrf = new StudyParticipantCrf();
                    if (studyStartDate != null) {
                        studyParticipantCrf.setStartDate(studyStartDate);
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

    public void setParticipantModesAndReminders(StudySite studySite, StudyParticipantAssignment studyParticipantAssignment, HttpServletRequest request) {
        String participantMode = request.getParameter("participantModes_" + studySite.getId());
        if (participantModes.size() > 0) {
            participantModes.clear();
        }
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
        String homePaperLanguage = request.getParameter("home_paper_lang_" + studySite.getId());
        String homeWebLanguage = request.getParameter("home_web_lang_" + studySite.getId());
        //String newStartDate = request.getParameter("study_date_" + studyParticipantAssignment.getStudySite().getId());
        //Date updatedStartDate = null;
        //try{
        	//updatedStartDate =  DateUtils.parseDate(newStartDate);
        //} catch(Exception e){
        	//logger.error("error parsing start date");
        //}
//        SupportedLanguageEnum homeWebLanguage = SupportedLanguageEnum.getByCode(homeWebLang);
        String ivrsLanguage = request.getParameter("ivrs_lang_" + studySite.getId());
        String clinicPaperLanguage = request.getParameter("clinic_paper_lang_" + studySite.getId());
        String clinicWebLanguage = request.getParameter("clinic_web_lang_" + studySite.getId());
        if (participantMode != null) {
            if (participantMode.equals("HOMEWEB")) {
                callHour = null;
                callMinute = null;
                callAmPm = null;
                callTimeZone = null;
            }
        } else {
            callHour = null;
            callMinute = null;
            callAmPm = null;
            callTimeZone = null;
        }
        
        boolean amPmHasChanged = false;
        boolean timeHasChanged = false;
        boolean timeZoneHasChanged = false;
        boolean reminderCallOptionHasChanged = false;
        
        if(studyParticipantAssignment.getCallAmPm() != null && !studyParticipantAssignment.getCallAmPm().equalsIgnoreCase(callAmPm)){
        	amPmHasChanged = true;
        }
        if((studyParticipantAssignment.getCallHour() != null && !studyParticipantAssignment.getCallHour().equals(callHour)) || 
        		(studyParticipantAssignment.getCallMinute() != null && !studyParticipantAssignment.getCallMinute().equals(callMinute))){
        	timeHasChanged = true;
        }
        if(studyParticipantAssignment.getCallTimeZone() != null && !studyParticipantAssignment.getCallTimeZone().equalsIgnoreCase(callTimeZone)){
        	timeZoneHasChanged = true;
        }
        if(studyParticipantAssignment.getStudyParticipantModes() != null && studyParticipantAssignment.getStudyParticipantModes().size() > 0 &&
        		studyParticipantAssignment.getStudyParticipantModes().get(0).getCall() != call){
        	reminderCallOptionHasChanged = true;
        }
        
        //update outdated ivrsSchedules status to CANCELLED if start Date has moved.
        //do we create new SPCrfSchedules when start date is moved backwards???
//        if(updatedStartDate has changed)){
//            for(IvrsSchedule ivrsSchedule: studyParticipantAssignment.getIvrsScheduleList()){
//    	    	if(ivrsSchedule.getPreferredCallTime().before(updatedStartDate) && ivrsSchedule.getCallStatus().equals(IvrsCallStatus.PENDING)){
//    				ivrsSchedule.setCallStatus(IvrsCallStatus.CANCELLED);
//    			}
//            }
//        }

        //update pending IvrsSchedules if time has been updated and mode is IVRS
        if((participantMode != null && participantMode.equals("IVRS")) && 
        		(timeZoneHasChanged || timeHasChanged || amPmHasChanged || reminderCallOptionHasChanged)){
        	Date finalDate = null;
            for(IvrsSchedule ivrsSchedule: studyParticipantAssignment.getIvrsScheduleList()){
            	if(ivrsSchedule.getCallStatus().equals(IvrsCallStatus.PENDING)){
                	if(timeZoneHasChanged || timeHasChanged || amPmHasChanged){
                    	Calendar newCal = Calendar.getInstance();
                    	//convert time from local timZone to requested timeZone first
                    	Date newDate = DateUtils.getDateInTimeZone(ivrsSchedule.getPreferredCallTime(), callTimeZone);
                    	newCal.setTimeInMillis(newDate.getTime());
                    	newCal.setTimeZone(TimeZone.getTimeZone(callTimeZone));
                    	//set to new values from UI
                		if(callHour == 12){
                			newCal.set(Calendar.HOUR, 0);
                    	} else {
                    		newCal.set(Calendar.HOUR, callHour);
                    	}
                		newCal.set(Calendar.MINUTE, callMinute);
                		if(callAmPm.equalsIgnoreCase("am")){
                			newCal.set(Calendar.AM_PM, Calendar.AM);
                    	} else {
                    		newCal.set(Calendar.AM_PM, Calendar.PM);
                    	} 
                		newCal.set(Calendar.SECOND, 0);
                		newCal.set(Calendar.MILLISECOND, 0);
                    	
                		//always save in the local timezone.
                		finalDate = DateUtils.getDateInTimeZone(newCal.getTime(), Calendar.getInstance().getTimeZone().getID());
                    	ivrsSchedule.setPreferredCallTime(finalDate);
                    	ivrsSchedule.setNextCallTime(finalDate);
                	}
                	//if reminders chkbox is unchecked change the callCount to just the one.
                	if(reminderCallOptionHasChanged){
                		if(!call){
                    		ivrsSchedule.setCallCount(1);
                    	} else {
                    		ivrsSchedule.setCallCount(studyParticipantAssignment.getStudySite().getStudy().getCallBackFrequency() + 1);
                    	}
                	}
            	}
            }
        }
        
        studyParticipantAssignment.setCallAmPm(callAmPm);
        studyParticipantAssignment.setCallHour(callHour);
        studyParticipantAssignment.setCallMinute(callMinute);
        studyParticipantAssignment.setHomePaperLanguage(homePaperLanguage);
        studyParticipantAssignment.setHomeWebLanguage(homeWebLanguage);
        studyParticipantAssignment.setIvrsLanguage(ivrsLanguage);
        studyParticipantAssignment.setClinicPaperLanguage(clinicPaperLanguage);
        studyParticipantAssignment.setClinicWebLanguage(clinicWebLanguage);
        if (callTimeZone != null) {
            if (callTimeZone.equals(""))
                studyParticipantAssignment.setCallTimeZone(null);
            else
                studyParticipantAssignment.setCallTimeZone(callTimeZone);
        } else
            studyParticipantAssignment.setCallTimeZone(callTimeZone);
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

    public void setParticipantModeHistory(StudySite studySite, StudyParticipantAssignment studyParticipantAssignment, HttpServletRequest request) {
        String participantMode = request.getParameter("participantModes_" + studySite.getId());
        String participantClinicMode = request.getParameter("participantClinicModes_" + studySite.getId());
        boolean blFlgAddHomeMode = true;
        boolean blFlgAddClinicMode = true;
        if (participantMode == null) {
            blFlgAddHomeMode = false;
        }
        if (participantClinicMode == null) {
            blFlgAddClinicMode = false;
        }
        if (blFlgAddHomeMode || blFlgAddClinicMode) {
            for (StudyParticipantReportingModeHistory studyParticipantReportingModeHistory : studyParticipantAssignment.getStudyParticipantReportingModeHistoryItems()) {
                if (studyParticipantReportingModeHistory.getEffectiveEndDate() == null) {
                    if (studyParticipantReportingModeHistory.getMode().equals(AppMode.HOMEWEB) || studyParticipantReportingModeHistory.getMode().equals(AppMode.HOMEBOOKLET) || studyParticipantReportingModeHistory.getMode().equals(AppMode.IVRS)) {
                        if (blFlgAddHomeMode) {
                            AppMode mode = AppMode.valueOf(participantMode);
                            if (studyParticipantReportingModeHistory.getMode().equals(mode)) {
                                blFlgAddHomeMode = false;
                            } else {
                                studyParticipantReportingModeHistory.setEffectiveEndDate(new Date());
                            }
                        }

                    } else {
                        if (blFlgAddClinicMode) {
                            AppMode mode = AppMode.valueOf(participantClinicMode);
                            if (studyParticipantReportingModeHistory.getMode().equals(mode)) {
                                blFlgAddClinicMode = false;
                            } else {
                                studyParticipantReportingModeHistory.setEffectiveEndDate(new Date());
                            }
                        }


                    }
                }
            }
        }
        if (blFlgAddClinicMode) {
            StudyParticipantReportingModeHistory hist = new StudyParticipantReportingModeHistory();
            AppMode mode = AppMode.valueOf(participantClinicMode);
            hist.setMode(mode);
            studyParticipantAssignment.addStudyParticipantModeHistory(hist);
        }
        if (blFlgAddHomeMode) {
            StudyParticipantReportingModeHistory hist = new StudyParticipantReportingModeHistory();
            AppMode mode = AppMode.valueOf(participantMode);
            hist.setMode(mode);
            studyParticipantAssignment.addStudyParticipantModeHistory(hist);
        }
    }

    public StudyParticipantAssignment getSelectedStudyParticipantAssignment() {

        List<StudyParticipantAssignment> studyParticipantAssignments = participant.getStudyParticipantAssignments();
        if (CollectionUtils.isNotEmpty(studyParticipantAssignments)) {
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
                studyParticipantAssignment.getNotificationClinicalStaff().remove(studyParticipantClinicalStaff);
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

    public Map<Integer, String> getStudySubjectIdentifierMap() {
        return studySubjectIdentifierMap;
    }

    public void setStudySubjectIdentifierMap(Map<Integer, String> studySubjectIdentifierMap) {
        this.studySubjectIdentifierMap = studySubjectIdentifierMap;
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

    public void initialize() {
        if (participant.getUser() != null) {
            if (participant.getUser().getUserRoles() != null) participant.getUser().getUserRoles().size();
        }
        if (participant.getStudyParticipantAssignments() != null) {
            participant.getStudyParticipantAssignments().size();
            for (StudyParticipantAssignment studyParticipantAssignment : participant.getStudyParticipantAssignments()) {
                if (studyParticipantAssignment.getStudyParticipantCrfs() != null) {
                    for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
                        if (studyParticipantCrf.getStudyParticipantCrfSchedules() != null)
                            studyParticipantCrf.getStudyParticipantCrfSchedules().size();
                        for (CRFPage crfPage : studyParticipantCrf.getCrf().getCrfPagesSortedByPageNumber()) {
                            crfPage.getCrfPageItems().size();
                        }
                        if (studyParticipantCrf.getCrf().getFormArmSchedules() != null)
                            studyParticipantCrf.getCrf().getFormArmSchedules().size();
                        for (FormArmSchedule formArmSchedule : studyParticipantCrf.getCrf().getFormArmSchedules()) {
                            if (formArmSchedule.getCrfCalendars() != null) formArmSchedule.getCrfCalendars().size();
                            if (formArmSchedule.getCrfCycleDefinitions() != null)
                                formArmSchedule.getCrfCycleDefinitions().size();
                            for (CRFCycleDefinition crfCycleDefinition : formArmSchedule.getCrfCycleDefinitions()) {
                                if (crfCycleDefinition.getCrfCycles() != null) crfCycleDefinition.getCrfCycles().size();
                            }
                        }
                    }
                }

                if (studyParticipantAssignment.getStudySite() != null) {
                    StudyOrganization studySite = studyParticipantAssignment.getStudySite();
                    if (studySite.getStudy() != null) {
                        studySite.getStudy().getCrfs().size();
                        studySite.getStudy().getStudyOrganizations().size();
                        studySite.getStudy().getStudySponsor();
                        studySite.getStudy().getArms().size();
                        studySite.getStudy().getAllStudyOrganizationClinicalStaffs().size();
                        studySite.getStudy().getStudyModes().size();
                        studySite.getStudy().getHomeModes();
                    }
                }


            }
        }


    }
}
