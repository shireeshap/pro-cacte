package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import org.apache.commons.lang.StringUtils;
import static org.easymock.EasyMock.expect;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Mehul Gulati
 *         Date: Nov 24, 2008
 */
public class CreateClinicalStaffControllerTest extends WebTestCase {
    private CreateClinicalStaffController createClinicalStaffController;
    private ClinicalStaffCommand clinicalStaffCommand;
    private ClinicalStaff clinicalStaff;
    private ClinicalStaffRepository clinicalStaffRepository;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createClinicalStaffController = new CreateClinicalStaffController();
        clinicalStaffCommand = (ClinicalStaffCommand) createClinicalStaffController.formBackingObject(request);
        clinicalStaffRepository = registerMockFor(ClinicalStaffRepository.class);
        clinicalStaffCommand.setClinicalStaff(clinicalStaff);
        createClinicalStaffController.setClinicalStaffRepository(clinicalStaffRepository);
        //  createClinicalStaffController.onSubmit(null, null, clinicalStaffCommand, null);
        //   MockHttpServletRequest r = new MockHttpServletRequest();
    }

    public void testConstructor() {
        assertEquals("clinicalStaff/createClinicalStaff", createClinicalStaffController.getFormView());
    }


    public void testGetRequest() throws Exception {
        request.setMethod("GET");
        request.addParameter("clinicalStaffId", "1");
        expect(clinicalStaffRepository.findById(new Integer(1))).andReturn(clinicalStaff);
        replayMocks();
        ModelAndView modelAndView = createClinicalStaffController.handleRequest(request, response);
        verifyMocks();
    }


    public void testFormBackingObject() throws Exception {
        assertNotNull(clinicalStaffCommand);
        assertNull(clinicalStaffCommand.getClinicalStaff());
        assertTrue(StringUtils.isBlank(clinicalStaffCommand.getOrganizationClinicalStaffIndexToRemove()));

    }

}
