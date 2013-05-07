package gov.nih.nci.ctcae.web.form;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;

/**
 * @author AmeyS
 * Testcases for HiddenFormController.java
 *
 */
public class HiddenFormControllerTest extends WebTestCase{
	
	private HiddenFormController controller;
	private CrfAjaxFacade crfAjaxFacade;
	private List<CRF> crfs;
	private CRF crf;
	private static String GET_METHOD = "GET";
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		controller = new HiddenFormController();
		crfAjaxFacade = registerMockFor(CrfAjaxFacade.class);
		controller.setCrfAjaxFacade(crfAjaxFacade);
		crf = new CRF();
		crfs = new ArrayList<CRF>();
		crfs.add(crf);
	}

	public void testHiddenFormController(){
		String supportedMethod  = (String) controller.getSupportedMethods()[0];
		assertEquals(GET_METHOD, supportedMethod);
	}
	
	
	public void testHandleRequestInternal(){
		try {
			expect(crfAjaxFacade.getHiddenCrfs()).andReturn(crfs).anyTimes();
			
			replayMocks();
			ModelAndView modelAndView = controller.handleRequestInternal(request, response);
			verifyMocks();
			
			assertEquals("form/hiddenForms", modelAndView.getViewName());
			assertEquals(1, ((List<CRF>) modelAndView.getModelMap().get("crfs")).size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
