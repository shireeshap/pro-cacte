package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.repository.StudyParticipantAssignmentRepository;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.web.WebTestCase;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import static org.easymock.EasyMock.expect;

import java.util.HashMap;

/**
 * @author Harsh Agarwal
 * @crated Nov 25, 2008
 */
public class ScheduleCrfControllerTest extends WebTestCase {
    private ScheduleCrfController controller;
    private StudyParticipantAssignmentRepository studyParticipantAssignmentRepository;
    private BindException errors;

    private StudyParticipantCommand command;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new ScheduleCrfController();
        studyParticipantAssignmentRepository = registerMockFor(StudyParticipantAssignmentRepository.class);
        errors = registerMockFor(BindException.class);
        controller.setStudyParticipantAssignmentRepository(studyParticipantAssignmentRepository);
        command = new StudyParticipantCommand();
        StudyParticipantAssignment a = new StudyParticipantAssignment();
        a.setId(1);
        command.setStudyParticipantAssignment(a);
        StudyParticipantCrf s = new StudyParticipantCrf();
        a.addStudyParticipantCrf(s);
    }

    public void testProcessFinish() throws Exception {

        expect(studyParticipantAssignmentRepository.save(command.getStudyParticipantAssignment())).andReturn(null);
        expect(studyParticipantAssignmentRepository.findById(1)).andReturn(command.getStudyParticipantAssignment());
        expect(errors.getModel()).andReturn(new HashMap());
        replayMocks();
        ModelAndView modelAndView = controller.processFinish(request, response, command, errors);
        verifyMocks();

        assertEquals("participant/confirmschedule", modelAndView.getViewName());


    }

}