package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;


/**
 * @author Harsh Agarwal
 * Date: Oct 23, 2008
 */
public class ScheduleCrfAjaxFacadeIntegrationTest extends AbstractWebIntegrationTestCase {

    private ScheduleCrfAjaxFacade scheduleCrfAjaxFacade;
    private ParticipantRepository participantRepository;
    private OrganizationRepository organizationRepository;
    private StudyRepository studyRepository;
    private Participant participant;
    private Study study;
    private Organization organization;
    private StudySite studySite;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();

        organization = Fixture.createOrganization("Test","TEST");
        organizationRepository.save(organization);

        study = Fixture.createStudyWithStudySite("my study","abc","addd", organization);
        studyRepository.save(study);

        participant = Fixture.createParticipantWithStudyAssignment("Mehul", "Gulati", "1234",study.getStudySites().get(0));
        participant = participantRepository.save(participant);
    }

    public void testSearchParticipantForStudy(){

        ArrayList<Participant> participants = ( ArrayList<Participant>)scheduleCrfAjaxFacade.matchParticipants("gu",study.getId());
        assertEquals(1, participants.size());

        participants = ( ArrayList<Participant>)scheduleCrfAjaxFacade.matchParticipants("abc",study.getId());
        assertEquals(0, participants.size());

    }

    public void testSearchStudyForParticipant(){

        ArrayList<Study> studies = ( ArrayList<Study>)scheduleCrfAjaxFacade.matchStudies("my",participant.getId());
        assertEquals(1, studies.size());

        studies = ( ArrayList<Study>)scheduleCrfAjaxFacade.matchStudies("xyzz",participant.getId());
        assertEquals(0, studies.size());
    }

    public ScheduleCrfAjaxFacade getScheduleCrfAjaxFacade() {
        return scheduleCrfAjaxFacade;
    }

    public void setScheduleCrfAjaxFacade(ScheduleCrfAjaxFacade scheduleCrfAjaxFacade) {
        this.scheduleCrfAjaxFacade = scheduleCrfAjaxFacade;
    }

    public ParticipantRepository getParticipantRepository() {
        return participantRepository;
    }

    public void setParticipantRepository(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public OrganizationRepository getOrganizationRepository() {
        return organizationRepository;
    }

    public void setOrganizationRepository(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public StudyRepository getStudyRepository() {
        return studyRepository;
    }

    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }
}