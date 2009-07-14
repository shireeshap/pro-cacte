package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;

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
    /**
     * The study id.
     */
    private List<StudySite> studySites = new ArrayList<StudySite>();

    /**
     * The site name.
     */
    private String siteName;

    private String notificationIndexToRemove;

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

    public StudyParticipantAssignment createStudyParticipantAssignment(StudySite studySite, String studyParticipantIdentifier) {

        StudyParticipantAssignment studyParticipantAssignment = new StudyParticipantAssignment();
        studyParticipantAssignment.setStudySite(studySite);
        studyParticipantAssignment.setStudyParticipantIdentifier(studyParticipantIdentifier);
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

    public void assignCrfsToParticipant(StudyParticipantAssignment studyParticipantAssignment, CRFRepository crfRepository, HttpServletRequest request) throws ParseException {
        Study study = studyParticipantAssignment.getStudySite().getStudy();
        for (CRF crf : study.getCrfs()) {
            crf = crfRepository.findById(crf.getId());
            if (crf.getStatus().equals(CrfStatus.RELEASED)) {
                StudyParticipantCrf studyParticipantCrf = new StudyParticipantCrf();
                String sDate = request.getParameter("form_date_" + crf.getId());
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

    public void apply(CRFRepository crfRepository, HttpServletRequest request) throws ParseException {

        //if new participant then clear all study participant assignments. otherwise it will create problem in case of any validation error.
        if (!participant.isPersisted()) {
            participant.removeAllStudyParticipantAssignments();
            for (StudySite studySite : getStudySites()) {
                setSiteName(studySite.getOrganization().getName());
                StudyParticipantAssignment studyParticipantAssignment = createStudyParticipantAssignment(studySite, request.getParameter("participantStudyIdentifier_" + studySite.getId()));
                assignCrfsToParticipant(studyParticipantAssignment, crfRepository, request);
            }
        } else {
            for (StudyParticipantAssignment studyParticipantAssignment : participant.getStudyParticipantAssignments()) {
                String studyParticipantIdentifier = request.getParameter("participantStudyIdentifier_" + studyParticipantAssignment.getStudySite().getId());
                if (!StringUtils.isBlank(studyParticipantIdentifier)) {
                    studyParticipantAssignment.setStudyParticipantIdentifier(studyParticipantIdentifier);
                }
            }
        }
    }

    public String getNotificationIndexToRemove() {
        return notificationIndexToRemove;
    }

    public void setNotificationIndexToRemove(String notificationIndexToRemove) {
        this.notificationIndexToRemove = notificationIndexToRemove;
    }

    public StudyParticipantAssignment getSelectedStudyParticipantAssignment() {
        List<StudyParticipantAssignment> studyParticipantAssignments = participant.getStudyParticipantAssignments();
        if (!studyParticipantAssignments.isEmpty() && selectedStudyParticipantAssignment == null) {
            selectedStudyParticipantAssignment = studyParticipantAssignments.get(0);
        }
        return selectedStudyParticipantAssignment;
    }

    public void setSelectedStudyParticipantAssignment(StudyParticipantAssignment selectedStudyParticipantAssignment) {
        this.selectedStudyParticipantAssignment = selectedStudyParticipantAssignment;
    }

    public void assignStaff() {
        for (StudyParticipantAssignment studyParticipantAssignment : getParticipant().getStudyParticipantAssignments()) {
            studyParticipantAssignment.addStudyParticipantClinicalStaff(studyParticipantAssignment.getTreatingPhysician());
            studyParticipantAssignment.addStudyParticipantClinicalStaff(studyParticipantAssignment.getResearchNurse());
            for (StudyParticipantClinicalStaff studyParticipantClinicalStaff : studyParticipantAssignment.getNotificationClinicalStaff()) {
                studyParticipantAssignment.addStudyParticipantClinicalStaff(studyParticipantClinicalStaff);
            }
        }
    }
}
