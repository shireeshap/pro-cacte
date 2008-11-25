package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.StudyCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.core.repository.StudyParticipantAssignmentRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
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
        command= new StudyParticipantCommand();
    }

    public void testProcessFinish() throws Exception {

        expect(studyParticipantAssignmentRepository.save(command.getStudyParticipantAssignment())).andReturn(null);
        expect(errors.getModel()).andReturn(new HashMap());
        replayMocks();
        ModelAndView modelAndView = controller.processFinish(request, response, command, errors);
        verifyMocks();

        assertEquals("participant/confirmschedule", modelAndView.getViewName());


    }

}