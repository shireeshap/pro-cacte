package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

import java.util.ArrayList;

/**
 * @author Vinay Kumar
 * @crated Oct 24, 2008
 */
public class FormControllersUtilsTest extends WebTestCase {

    private CreateFormController createFormController;
    private ProCtcTermRepository pro;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createFormController = new CreateFormController();
       // finderRepository = registerMockFor(FinderRepository.class);
        //createFormController.setP(finderRepository);
    }

    public void testNoCommandInCreateForm() {


        assertNull("no command should present in session", FormControllersUtils.getFormCommand(request));


    }

    public void testCommandInGetRequestOfCreateForm() throws Exception {

        request.setMethod("GET");
        //expect(finderRepository.find(isA(ProCtcTermQuery.class))).andReturn(new ArrayList<ProCtcTerm>());
        replayMocks();

        createFormController.handleRequest(request, response);
        verifyMocks();
        assertNotNull("command must present in session", FormControllersUtils.getFormCommand(request));

    }
}
