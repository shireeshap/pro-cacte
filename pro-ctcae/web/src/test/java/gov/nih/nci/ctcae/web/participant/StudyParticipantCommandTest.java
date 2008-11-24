package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.form.CreateFormCommand;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

/**
 * @author Harsh Agarwal
 * @crated Nov 24, 2008
 */
public class StudyParticipantCommandTest extends WebTestCase {

    private StudyParticipantCommand command;
    Participant participant;
    Study study;
    StudyParticipantAssignment studyParticipantAssignment;
    StudyParticipantCrf studyParticipantCrf;
    String objectsIndexesToRemove = "0-1" ;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        command = new StudyParticipantCommand();

        participant = Fixture.createParticipant("first", "last", "id");
        study = Fixture.createStudyWithStudySite("short", "long", "id", Fixture.createOrganization("test", "test"));

        studyParticipantAssignment = new StudyParticipantAssignment();
        studyParticipantAssignment.setStudySite(study.getStudySites().get(0));
        studyParticipantAssignment.setParticipant(participant);

        CRF crf = Fixture.createCrf("test", CrfStatus.RELEASED, "1.0");
        study.addCrf(crf);

        studyParticipantCrf = new StudyParticipantCrf();
        studyParticipantCrf.setStudyCrf(study.getStudyCrfs().get(0));

        studyParticipantCrf.addStudyParticipantCrfSchedule(new StudyParticipantCrfSchedule());
        studyParticipantCrf.addStudyParticipantCrfSchedule(new StudyParticipantCrfSchedule());
        studyParticipantCrf.addStudyParticipantCrfSchedule(new StudyParticipantCrfSchedule());

        studyParticipantAssignment.addStudyParticipantCrf(studyParticipantCrf);

        command.setStudyParticipantAssignment(studyParticipantAssignment);
        command.setStudy(study);
        command.setParticipant(participant);
        command.setObjectsIndexesToRemove(objectsIndexesToRemove);


    }

    public void testGetters() {

        assertEquals(command.getStudy(), study);
        assertEquals(command.getParticipant(), participant);
        assertEquals(command.getStudyParticipantAssignment(), studyParticipantAssignment);
        assertEquals(command.getObjectsIndexesToRemove(), objectsIndexesToRemove);
    }

    public void testRemoveCrfSchedules() {

        assertEquals(3, command.getStudyParticipantAssignment().getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules().size());
        command.removeCrfSchedules();
        assertEquals(2, command.getStudyParticipantAssignment().getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules().size());
    }


}