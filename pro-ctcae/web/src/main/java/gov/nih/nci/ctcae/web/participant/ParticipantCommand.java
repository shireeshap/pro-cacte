package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

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
    private List<StudySite> studySites;

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

    public StudyParticipantAssignment createStudyParticipantAssignments(StudySite studySite, String studyParticipantIdentifier) {
        StudyParticipantAssignment studyParticipantAssignment = new StudyParticipantAssignment();
        studyParticipantAssignment.setStudySite(studySite);
        studyParticipantAssignment.setParticipant(getParticipant());
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
        if (!participant.getStudyParticipantAssignments().contains(studyParticipantAssignment)) {
            participant.addStudyParticipantAssignment(studyParticipantAssignment);
        }
        return studyParticipantAssignment;
    }


    public void assignCrfsToParticipant(StudyParticipantAssignment studyParticipantAssignment, CRFRepository crfRepository, HttpServletRequest request) throws ParseException {
        Study study = studyParticipantAssignment.getStudySite().getStudy();
        CRFQuery crfQuery = new CRFQuery();
        crfQuery.filterByStudyId(study.getId());
        Collection<CRF> crfCollection = crfRepository.find(crfQuery);
        for (CRF crf : crfCollection) {
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
                crfRepository.generateSchedulesFromCrfCalendar(crf, studyParticipantCrf);
            }
        }


    }

    public void apply(CRFRepository crfRepository, HttpServletRequest request) {
        if (getStudySites() != null) {
            for (StudySite studySite : getStudySites()) {
                setSiteName(studySite.getOrganization().getName());
                StudyParticipantAssignment studyParticipantAssignment = createStudyParticipantAssignments(studySite, request.getParameter("participantStudyIdentifier_" + studySite.getId()));
                try {
                    assignCrfsToParticipant(studyParticipantAssignment, crfRepository, request);
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
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
