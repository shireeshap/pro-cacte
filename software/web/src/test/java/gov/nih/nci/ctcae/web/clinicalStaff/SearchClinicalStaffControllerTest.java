package gov.nih.nci.ctcae.web.clinicalStaff;

import org.springframework.web.servlet.ModelAndView;

import gov.nih.nci.ctcae.web.WebTestCase;

/**
 * @author Amey
 * SearchClinicalStaffControllerTest class
 */

public class SearchClinicalStaffControllerTest extends WebTestCase {
	SearchClinicalStaffController controller;
	private final static String VIEW_NAME = "clinicalStaff/searchClinicalStaff";
	private final static String SEARCH_STRING = "searchString";
	private static String CLINICAL_STAFF_SEARCH_STRING = "clinicalStaffSearchString";
	private final static String SEARCH_STRING_VALUE = "admin";
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		controller = new SearchClinicalStaffController();
	}
	
	public void testHandleRequestInternal_balankSearchString() throws Exception{
		ModelAndView modelAndView = controller.handleRequest(request, response);
		
		assertEquals(VIEW_NAME, modelAndView.getViewName());
		assertNull("No searchString is expected", modelAndView.getModelMap().get(SEARCH_STRING));
		assertNull("No searchString is expected", request.getSession().getAttribute(CLINICAL_STAFF_SEARCH_STRING));
	}
	
	public void testHandleRequestInternal_withSearchString() throws Exception{
		request.setParameter(SEARCH_STRING, SEARCH_STRING_VALUE);
		ModelAndView modelAndView = controller.handleRequest(request, response);
		
		assertEquals(VIEW_NAME, modelAndView.getViewName());
		assertEquals("searchString is expected", modelAndView.getModelMap().get(SEARCH_STRING), SEARCH_STRING_VALUE);
		assertEquals("searchString is expected", request.getSession().getAttribute(CLINICAL_STAFF_SEARCH_STRING), SEARCH_STRING_VALUE);
	}
}
