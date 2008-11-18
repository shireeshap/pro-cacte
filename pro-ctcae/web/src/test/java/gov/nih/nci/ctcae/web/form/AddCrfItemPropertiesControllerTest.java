package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CrfItem;
import gov.nih.nci.ctcae.core.domain.CrfItemAllignment;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Vinay Kumar
 * @crated Nov 17, 2008
 */
public class AddCrfItemPropertiesControllerTest extends WebTestCase {
    private AddCrfItemPropertiesController controller;
    private CreateFormCommand command;
    private FinderRepository finderRepository;
    private ProCtcQuestion proCtcQuestion;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new AddCrfItemPropertiesController();
        finderRepository = registerMockFor(FinderRepository.class);

        command = new CreateFormCommand();
        controller.setFinderRepository(finderRepository);
        proCtcQuestion = new ProCtcQuestion();
        proCtcQuestion.setId(1);
    }

    public void testPostRequest() throws Exception {
        request.getSession().setAttribute(CreateFormController.class.getName() + ".FORM." + "command", command);
        request.setParameter("questionId", "1");
        request.setParameter("instructions", "test inst");
        request.setParameter("responseRequired", "true");
        request.setParameter("crfItemAllignment", CrfItemAllignment.VERTICAL.getDisplayName());

        command.getStudyCrf().getCrf().addOrUpdateCrfItem(proCtcQuestion, 1);
        expect(finderRepository.findAndInitializeProCtcQuestion(1)).andReturn(proCtcQuestion);
        replayMocks();
        controller.handleRequestInternal(request, response);
        verifyMocks();

        CrfItem crfItem = command.getStudyCrf().getCrf().getCrfItemByQuestion(proCtcQuestion);
        assertEquals("must bind instructions", "test inst", crfItem.getInstructions());
        assertTrue("must bind responseRequired", crfItem.getResponseRequired());
        assertEquals("must bind crfItemAllignment", CrfItemAllignment.VERTICAL, crfItem.getCrfItemAllignment());

    }

    public void testGetRequest() throws Exception {
        request.getSession().setAttribute(CreateFormController.class.getName() + ".FORM." + "command", command);
        request.setMethod("GET");
        request.setParameter("questionId", "1");

        command.getStudyCrf().getCrf().addOrUpdateCrfItem(proCtcQuestion, 1);
        expect(finderRepository.findAndInitializeProCtcQuestion(1)).andReturn(proCtcQuestion);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        verifyMocks();

        assertNotNull(modelAndView.getModel().get("crfItem"));
        assertNotNull(modelAndView.getModel().get("responseRequired"));
        assertNotNull(modelAndView.getModel().get("crfItemAllignments"));
    }
}
