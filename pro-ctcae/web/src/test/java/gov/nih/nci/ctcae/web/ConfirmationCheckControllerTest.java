package gov.nih.nci.ctcae.web;

import org.springframework.web.servlet.ModelAndView;

/**
 * @author Vinay Kumar
 * @crated Jan 06, 2008
 */
public class ConfirmationCheckControllerTest extends WebTestCase {

	private ConfirmationCheckController controller;


	@Override
	protected void setUp() throws Exception {
		super.setUp();
		controller = new ConfirmationCheckController();


	}

	public void testSupportedMethod() {
		assertEqualArrays("only get is supported", new String[]{"GET"}, controller.getSupportedMethods());
	}

	public void testInvalidHandleRequest() throws Exception {

		ModelAndView modelAndView = controller.handleRequest(request, response);
		assertNull("must return null model and view", modelAndView);


	}

	public void testHandleDeleteCrfRequest() throws Exception {
		request.addParameter("confirmationType", "deleteCrf");
		request.addParameter("selectedCrfPageNumber", "1");

		ModelAndView modelAndView = controller.handleRequest(request, response);
		assertEquals("selectedCrfPageNumber must be present", "1", modelAndView.getModelMap().get("selectedCrfPageNumber"));


	}
}