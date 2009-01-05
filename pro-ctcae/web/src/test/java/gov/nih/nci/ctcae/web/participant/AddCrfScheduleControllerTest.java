package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Vinay Kumar
 * @crated Nov 5, 2008
 */
public class AddCrfScheduleControllerTest extends WebTestCase {

	private AddCrfScheduleController controller;
	private FinderRepository finderRepository;

	private ScheduleCrfController scheduleCrfController;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		controller = new AddCrfScheduleController();
		finderRepository = registerMockFor(FinderRepository.class);
		controller.setFinderRepository(finderRepository);
		scheduleCrfController = new ScheduleCrfController();


	}

	public void testSupportedMethod() {
		assertEqualArrays("only get is supported", new String[]{"GET"}, controller.getSupportedMethods());
	}

	public void testHandleRequest() throws Exception {

		scheduleCrfController.handleRequest(request, response);
		Object command = ParticipantControllerUtils.getStudyParticipantCommand(request);
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
		expect(finderRepository.findById(CRF.class, 10)).andReturn(new CRF());
		ModelAndView modelAndView = controller.handleRequestInternal(request, response);
		verifyMocks();
		assertNotNull("index must be present", modelAndView.getModelMap().get("scheduleindex"));
		assertNotNull("index must be present", modelAndView.getModelMap().get("crfindex"));

		assertEquals(0, modelAndView.getModelMap().get("scheduleindex"));
		assertEquals(0, modelAndView.getModelMap().get("crfindex"));


	}
}