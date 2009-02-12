package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import gov.nih.nci.ctcae.core.repository.ClinicalStaffRepository;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Mehul Gulati
 * Date: Nov 24, 2008
 */
public class AddSiteControllerTest extends WebTestCase {

    private AddClinicalStaffComponentController controller;
    private CreateClinicalStaffController createClinicalStaffController;
    private WebControllerValidator validator;
    private ClinicalStaffRepository clinicalStaffRepository;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new AddClinicalStaffComponentController();
        createClinicalStaffController = new CreateClinicalStaffController();
        validator = new WebControllerValidatorImpl();

        clinicalStaffRepository = registerMockFor(ClinicalStaffRepository.class);
        createClinicalStaffController.setClinicalStaffRepository(clinicalStaffRepository);

        createClinicalStaffController.setWebControllerValidator(validator);

    }
    public void testSupportedMethod() {
        assertEqualArrays("only get is supported", new String[]{"GET"}, controller.getSupportedMethods());
    }

    public void testHandleRequest() throws Exception {
        request.setMethod("GET");

        createClinicalStaffController.handleRequest(request, response);
        Object command = ClinicalStaffControllerUtils.getClinicalStaffCommand(request);
        assertNotNull("command must be present in session", command);
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        assertNotNull("index must be present", modelAndView.getModelMap().get("index"));

    }

}
