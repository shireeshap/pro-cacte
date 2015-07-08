package gov.nih.nci.ctcae.web.clinicalStaff;

import static org.easymock.EasyMock.expect;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.web.WebTestCase;

/**
 * @author Amey
 * ViewClinicalStaffControllerTest class
 */

public class ViewClinicalStaffControllerTest extends WebTestCase{
	ViewClinicalStaffController controller;
	private final String CLINICAL_STAFF_ID = "clinicalStaffId";
	private final static String CLINICAL_STAFF_ID_VALUE ="1";
	private final static String CLINICAL_STAFF_SEARCH_STRING = "clinicalStaffSearchString";
	private final static String CLINICAL_STAFF_SEARCH_STRING_VALUE = "admin";
	private final static String VIEW_NAME = "clinicalStaff/viewClinicalStaff";
	private final static String SEARCH_STRING = "searchString";
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		controller = new ViewClinicalStaffController();
	}
	
	public void testViewClinicalStaffController(){
		controller = new ViewClinicalStaffController();
		
		assertEquals(VIEW_NAME, controller.getFormView());
		assertTrue(controller.isSessionForm());
		assertTrue(controller.isBindOnNewForm());
	}
	
	public void testFormBackingObject_nullId() throws Exception{
		ClinicalStaff clinicalStaff;
		request.getSession().setAttribute(CLINICAL_STAFF_SEARCH_STRING, CLINICAL_STAFF_SEARCH_STRING_VALUE);
		
		clinicalStaff = (ClinicalStaff) controller.formBackingObject(request);
		
		assertNull("No clinical is expected to be null", clinicalStaff);
		assertEquals("searchString is expected", CLINICAL_STAFF_SEARCH_STRING_VALUE, request.getAttribute(SEARCH_STRING));
	}
	
	public void testFormBackingObject_CSnotFound() throws Exception{
		ClinicalStaff clinicalStaff = new ClinicalStaff();
		clinicalStaff.setId(Integer.parseInt(CLINICAL_STAFF_ID_VALUE));
		ClinicalStaffRepository clinicalStaffRepository = registerMockFor(ClinicalStaffRepository.class);
		request.setParameter(CLINICAL_STAFF_ID, CLINICAL_STAFF_ID_VALUE);
		request.getSession().setAttribute(CLINICAL_STAFF_SEARCH_STRING, CLINICAL_STAFF_SEARCH_STRING_VALUE);
		expect(clinicalStaffRepository.findById(Integer.parseInt(CLINICAL_STAFF_ID_VALUE))).andReturn(clinicalStaff);
		
		replayMocks();
		controller.setClinicalStaffRepository(clinicalStaffRepository);
		ClinicalStaff formBackingObject = (ClinicalStaff) controller.formBackingObject(request);
		verifyMocks();
		
		assertEquals("clinical is not expected to be null", clinicalStaff, formBackingObject);
		assertEquals("searchString is expected", CLINICAL_STAFF_SEARCH_STRING_VALUE, request.getAttribute(SEARCH_STRING));
	}
}