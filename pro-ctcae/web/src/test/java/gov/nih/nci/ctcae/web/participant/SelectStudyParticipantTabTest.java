package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.query.StudyParticipantAssignmentQuery;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.form.FormDetailsTab;
import gov.nih.nci.ctcae.web.form.CreateFormCommand;
import gov.nih.nci.ctcae.web.form.StudyCrfAjaxFacade;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

import java.util.*;

/**
 * @author Harsh Agarwal
 * @crated Nov 24, 2008
 */
public class SelectStudyParticipantTabTest extends WebTestCase {
    private SelectStudyParticipantTab tab;
    private FinderRepository finderRepository;
    private StudyParticipantCommand command;
    private StudyCrf studyCrf;
    Study study;
    CRF crf;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        finderRepository = registerMockFor(FinderRepository.class);
        tab = new SelectStudyParticipantTab();

        tab.setFinderRepository(finderRepository);

        study = Fixture.createStudy("short", "long", "id");
        crf = Fixture.createCrf("test", CrfStatus.DRAFT, "1.0");
        studyCrf = new StudyCrf(1);
        studyCrf.setStudy(study);
        studyCrf.setCrf(crf);
        request.setParameter("studyCrfId", "" + studyCrf.getId());

    }

    public void testOnDisplay() {
        assertNull(command.getStudy());
        expect(finderRepository.findById(StudyCrf.class, Integer.valueOf(request.getParameter("studyCrfId")))).andReturn(studyCrf);
        replayMocks();
        tab.onDisplay(request, command);
        verifyMocks();
        assertEquals(study, command.getStudy());
    }

    public void testPostProcess() {

        command = new StudyParticipantCommand();

        Participant participant = Fixture.createParticipant("first", "last", "id");
        participant.setId(1);
        study = Fixture.createStudy("short", "long", "id");
        study.setId(1);

        command.setStudy(study);
        command.setParticipant(participant);
        ArrayList l = new ArrayList();
        l.add(new StudyParticipantAssignment());

        expect(finderRepository.find(isA(StudyParticipantAssignmentQuery.class))).andReturn(l);
        replayMocks();
        tab.postProcess(request, command, null);
        verifyMocks();

        assertNotNull(command.getStudyParticipantAssignment());
    }
}