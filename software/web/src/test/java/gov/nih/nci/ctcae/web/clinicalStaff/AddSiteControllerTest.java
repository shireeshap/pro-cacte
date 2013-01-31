package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.helper.ClinicalStaffTestHelper;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import org.springframework.web.servlet.ModelAndView;

import static edu.nwu.bioinformatics.commons.testing.CoreTestCase.assertEqualArrays;

/**
 * @author Mehul Gulati
 *         Date: Nov 24, 2008
 */
public class AddSiteControllerTest extends AbstractWebTestCase {

    private AddClinicalStaffComponentController controller;
    private CreateClinicalStaffController createClinicalStaffController;
    private WebControllerValidator validator;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        controller = new AddClinicalStaffComponentController();
        createClinicalStaffController = new CreateClinicalStaffController();
        validator = new WebControllerValidatorImpl();
        createClinicalStaffController.setClinicalStaffRepository(clinicalStaffRepository);

        createClinicalStaffController.setWebControllerValidator(validator);

    }

    public void testSupportedMethod() {
        assertEqualArrays("only get is supported", new String[]{"GET"}, controller.getSupportedMethods());
    }

    public void testHandleRequest() throws Exception {
        request.setMethod("GET");
        request.addParameter("componentType", "site");
         login(ClinicalStaffTestHelper.getDefaultClinicalStaff().getUser().getUsername());
        createClinicalStaffController.handleRequest(request, response);
        Object command = ClinicalStaffControllerUtils.getClinicalStaffCommand(request);
        assertNotNull("command must be present in session", command);
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        assertNotNull("organizationClinicalStaffIndex must be present", modelAndView.getModelMap().get("organizationClinicalStaffIndex"));

    }

}
