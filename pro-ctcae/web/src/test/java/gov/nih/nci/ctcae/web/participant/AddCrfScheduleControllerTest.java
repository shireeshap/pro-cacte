package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.study.AddStudySiteController;
import gov.nih.nci.ctcae.web.study.CreateStudyController;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.ArrayList;

/**
 * @author Vinay Kumar
 * @crated Nov 5, 2008
 */
public class AddCrfScheduleControllerTest extends WebTestCase {

    private AddCrfScheduleController controller;

    private ScheduleCrfController scheduleCrfController;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new AddCrfScheduleController();
        scheduleCrfController = new ScheduleCrfController();


    }

    public void testSupportedMethod() {
        assertEqualArrays("only get is supported", new String[]{"GET"}, controller.getSupportedMethods());
    }

    public void testHandleRequest() throws Exception {

        scheduleCrfController.handleRequest(request, response);
        Object command = ControllersUtils.getFormCommand(request, scheduleCrfController);
        assertNotNull("command must present in session", command);
        StudyParticipantCommand studyParticipantCommand = (StudyParticipantCommand) command;

        CRF crf = Fixture.createCrf("crf", CrfStatus.RELEASED, "1.0");
        StudyCrf studyCrf = new StudyCrf();
        studyCrf.setCrf(crf);

        StudyParticipantCrf studyParticipantCrf = new StudyParticipantCrf();
        studyParticipantCrf.setStudyCrf(studyCrf);

        StudyParticipantAssignment studyParticipantAssignment = new StudyParticipantAssignment();
        studyParticipantAssignment.addStudyParticipantCrf(studyParticipantCrf);


        studyParticipantCommand.setStudyParticipantAssignment(studyParticipantAssignment);

        request.setParameter("crfindex", "0");
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);

        assertNotNull("index must be present", modelAndView.getModelMap().get("scheduleindex"));
        assertNotNull("index must be present", modelAndView.getModelMap().get("crfindex"));

        assertEquals(0, modelAndView.getModelMap().get("scheduleindex"));
        assertEquals(0, modelAndView.getModelMap().get("crfindex"));


    }
}