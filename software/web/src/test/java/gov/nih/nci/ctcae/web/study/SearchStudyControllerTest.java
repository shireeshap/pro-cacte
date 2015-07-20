package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.web.AbstractWebTestCase;

import org.springframework.web.servlet.ModelAndView;

/**
 * @author Amey
 * SearchStudyControllerTest class
 */
public class SearchStudyControllerTest extends AbstractWebTestCase{
	SearchStudyController controller;
	private static final String VIEW_NAME = "study/searchStudy";
	private static final String SEARCH_TEXT = "searchText";
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		controller = new SearchStudyController();
	}
	
	public void testHandleRequestInternal() throws Exception {
		request.setParameter(SEARCH_TEXT, "RTOG");
		
		ModelAndView modelAndView = controller.handleRequestInternal(request, response);
		assertEquals(VIEW_NAME, modelAndView.getViewName());
		assertEquals("RTOG", modelAndView.getModelMap().get(SEARCH_TEXT));
	}
}
