package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;
import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;

/**
 * @author Amey
 * FetchStudyControllerTest class
 */
public class FetchStudyControllerTest extends AbstractWebIntegrationTestCase {
	FetchStudyController controller;
	StudyAjaxFacade studyAjaxFacade;
	private static String SEARCH_TEXT = "searchText";
	
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		controller = new FetchStudyController();
		studyAjaxFacade = new StudyAjaxFacade();
		studyAjaxFacade.setStudyRepository(studyRepository);
		controller.setStudyAjaxFacade(studyAjaxFacade);
	}
	
	public void testHandleRequestInternal_validSearchString() throws Exception {
		request.getSession().setAttribute(SEARCH_TEXT, "collection");
		
		ModelAndView modelAndView = controller.handleRequestInternal(request, response);
		
		JSONObject obj = ((JSONObject) modelAndView.getModel().get("shippedRecordSet"));
		assertEquals("1", obj.get("totalRecords").toString());
	}
	
	public void testHandleRequestInternal_blankSearchString() throws Exception {
		request.getSession().setAttribute(SEARCH_TEXT, "");
		
		ModelAndView modelAndView = controller.handleRequestInternal(request, response);
		
		JSONObject obj = ((JSONObject) modelAndView.getModel().get("shippedRecordSet"));
		assertEquals("2", obj.get("totalRecords").toString());
	}
	
	public void testHandleRequestInternal_NonExistingStudySearchString() throws Exception {
		request.getSession().setAttribute(SEARCH_TEXT, "wrongStudy");
		
		ModelAndView modelAndView = controller.handleRequestInternal(request, response);
		
		JSONObject obj = ((JSONObject) modelAndView.getModel().get("shippedRecordSet"));
		assertEquals("0", obj.get("totalRecords").toString());
	}
}
