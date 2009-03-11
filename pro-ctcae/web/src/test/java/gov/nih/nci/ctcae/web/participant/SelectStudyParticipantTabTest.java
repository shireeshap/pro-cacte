package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.web.WebTestCase;

import java.util.ArrayList;

/**
 * @author Harsh Agarwal
 * @crated Nov 24, 2008
 */
public class SelectStudyParticipantTabTest extends WebTestCase {
    private SelectStudyParticipantTab tab;
    private StudyParticipantCommand command;
    private CRF crf;
    Study study;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        tab = new SelectStudyParticipantTab();

        //tab.setFinderRepository(finderRepository);

        study = Fixture.createStudy("short", "long", "id");
        crf = Fixture.createCrf("test", CrfStatus.DRAFT, "1.0");
        crf.setId(1);
        crf.setStudy(study);
        request.setParameter("crfId", "" + crf.getId());
        command = new StudyParticipantCommand();

    }

    public void testOnDisplay() {
        assertNull(command.getStudy());
        replayMocks();
        tab.onDisplay(request, command);
        verifyMocks();
        assertEquals(study, command.getStudy());
    }

    public void testPostProcess() {


        Participant participant = Fixture.createParticipant("first", "last", "id");
        participant.setId(1);
        study = Fixture.createStudy("short", "long", "id");
        study.setId(1);

        command.setStudy(study);
        command.setParticipant(participant);
        ArrayList list = new ArrayList();
        list.add(new StudyParticipantAssignment());

        // expect(finderRepository.find(isA(StudyParticipantAssignmentQuery.class))).andReturn(list);
        replayMocks();
        tab.postProcess(request, command, null);
        verifyMocks();

        assertNotNull(command.getStudyParticipantAssignment());
    }
}