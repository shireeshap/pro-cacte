package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.StudyCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Nov 5, 2008
 */
public class ScheduleCrfControllerTest extends WebTestCase {
    private ScheduleCrfController controller;
    private FinderRepository finderRepository;
    private StudyRepository studyRepository;

    private StudyParticipantCommand command;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new ScheduleCrfController();
        finderRepository = registerMockFor(FinderRepository.class);
        studyRepository = registerMockFor(StudyRepository.class);
        controller.setFinderRepository(finderRepository);
        controller.setStudyRepository(studyRepository);

        command= new StudyParticipantCommand();
    }

    public void testGetRequest() throws Exception {
//        request.setMethod("GET");
//        request.addParameter("studyCrfId", "1");
//        expect(finderRepository.findById(StudyCrf.class, Integer.valueOf(1))).andReturn(studyCrf);
//        replayMocks();
//        ModelAndView modelAndView = controller.handleRequest(request, response);
//        verifyMocks();
//        Map model = modelAndView.getModel();
//        Object command = model.get("command");
//        assertNotNull("must find command object", command);
//        assertTrue(command instanceof StudyCrf);

    }

    public void testPostRequest() throws Exception {

        request.setMethod("POST");
        request.setParameter("studyParticipantCrfs[0].startDate","10/10/2008");
        request.setParameter("_finish","true");
        request.setParameter("study","1");
        StudyCrf studyCrf = new StudyCrf();
        studyCrf.setCrf(new CRF());
        command.getStudyParticipants().add(new StudyParticipantCrf(studyCrf));
        ModelAndView modelAndView = controller.handleRequest(request, response);

        Map model = modelAndView.getModel();
        Object object = model.get("command");


        assertNotNull("must find command object", object);
        assertTrue(object instanceof StudyParticipantCommand);
        StudyParticipantCommand studyParticipantCommand= (StudyParticipantCommand) object;
        assertNotNull(command.getStudy());
        assertNotNull("must bind start date",command.getStudyParticipants().get(0).getStartDate());

    }
}