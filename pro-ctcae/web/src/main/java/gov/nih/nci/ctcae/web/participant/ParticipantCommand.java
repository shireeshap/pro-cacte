package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.repository.CRFRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.text.ParseException;

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

    /**
     * The study id.
     */
    private StudySite[] studySite;

    /**
     * The site name.
     */
    private String siteName;

    /**
     * Instantiates a new participant command.
     */
    public ParticipantCommand() {
        super();
        participant = new Participant();
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

    public StudySite[] getStudySite() {
        return studySite;
    }

    public void setStudySite(StudySite[] studySite) {
        this.studySite = studySite;
    }

    public StudyParticipantAssignment createStudyParticipantAssignments(StudySite studySite, String studyParticipantIdentifier) {
        StudyParticipantAssignment studyParticipantAssignment = new StudyParticipantAssignment();
        studyParticipantAssignment.setStudySite(studySite);
        studyParticipantAssignment.setParticipant(getParticipant());
        studyParticipantAssignment.setStudyParticipantIdentifier(studyParticipantIdentifier);
        participant.addStudyParticipantAssignment(studyParticipantAssignment);
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
                studyParticipantCrf.setCrf(crf);
                studyParticipantAssignment.addStudyParticipantCrf(studyParticipantCrf);
                crfRepository.generateSchedulesFromCrfCalendar(crf, studyParticipantCrf, request.getParameter("form_date_" + crf.getId()));
            }
        }


    }
}
