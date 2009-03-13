package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.*;

import java.util.UUID;

/**
 * @author Vinay Kumar
 * @crated Mar 11, 2009
 */
public class AbstractInstanceLevelAuthorizationIntegrationTest extends AbstractHibernateIntegrationTestCase {

    protected Study study1, study2;

    protected User user, anotherUser;

    protected ClinicalStaff anotherClinicalStaff;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        study1 = createStudy("-10002");
        study2 = createStudy("-1003");

        study1.getStudySites().get(0).setOrganization(wake);
        study2.getStudySites().get(0).setOrganization(wake);

        study2 = studyRepository.save(study2);
        study1 = studyRepository.save(study1);



        anotherClinicalStaff = Fixture.createClinicalStaffWithOrganization("Paul", "Williams", "-123456", wake);
        anotherClinicalStaff = clinicalStaffRepository.save(anotherClinicalStaff);
        commitAndStartNewTransaction();


    }

    protected Participant createParticipant(String firstName, final StudySite studySite) {
        Participant participant = Fixture.createParticipant(firstName, "Doe", "12345");
        StudyParticipantAssignment studyParticipantAssignment = new StudyParticipantAssignment();
        studyParticipantAssignment.setStudySite(studySite);
        studyParticipantAssignment.setStudyParticipantIdentifier("-12345");
        participant.addStudyParticipantAssignment(studyParticipantAssignment);
        participant = participantRepository.save(participant);
        commitAndStartNewTransaction();
        return participant;
    }

    protected CRF createCRF(Study study) {

        Study savedStudy = studyRepository.findById(study.getId());
        assertEquals("must see his own study only", savedStudy, study);

        CRF crf = Fixture.createCrf();
        crf.setTitle("title" + UUID.randomUUID());
        crf.setStudy(savedStudy);
        crf = crfRepository.save(crf);

        assertNotNull("must save crf on his own study", crf);
        commitAndStartNewTransaction();
        return crf;
    }
}
