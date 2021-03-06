package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.query.StudyParticipantAssignmentQuery;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantAssignmentRepository;
import gov.nih.nci.ctcae.web.WebTestCase;

import java.util.ArrayList;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

/**
 * @author Harsh Agarwal
 * @since Nov 24, 2008
 */
public class SelectStudyParticipantTabTest extends WebTestCase {
    private SelectStudyParticipantTab tab;
    private StudyParticipantCommand command;
    private CRF crf;
    Study study;
    protected StudyParticipantAssignmentRepository studyParticipantAssignmentRepository;
    protected CRFRepository crfRepository;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        tab = new SelectStudyParticipantTab();
        studyParticipantAssignmentRepository = registerMockFor(StudyParticipantAssignmentRepository.class);
        tab.setStudyParticipantAssignmentRepository(studyParticipantAssignmentRepository);
        crfRepository = registerMockFor(CRFRepository.class);
        tab.setCrfRepository(crfRepository);
        study = Fixture.createStudy("short", "long", "id");
        crf = Fixture.createCrf("test", CrfStatus.DRAFT, "1.0");
        crf.setId(1);
        crf.setStudy(study);
        request.setParameter("crfId", "" + crf.getId());
        command = new StudyParticipantCommand();

    }

    public void testOnDisplay() {
        assertNull(command.getStudy());
        expect(crfRepository.findById(crf.getId())).andReturn(crf);
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

        expect(studyParticipantAssignmentRepository.find(isA(StudyParticipantAssignmentQuery.class))).andReturn(list);
        replayMocks();
        tab.postProcess(request, command, null);
        verifyMocks();

        assertNotNull(command.getStudyParticipantAssignment());
    }
}