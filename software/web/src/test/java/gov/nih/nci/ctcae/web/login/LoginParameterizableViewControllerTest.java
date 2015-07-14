package gov.nih.nci.ctcae.web.login;

import static org.easymock.EasyMock.expect;
import gov.nih.nci.ctcae.core.domain.Alert;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;
import gov.nih.nci.ctcae.web.alert.AlertAjaxFacade;

import java.util.ArrayList;
import java.util.Properties;

import org.springframework.web.servlet.ModelAndView;

/**LoginParameterizableViewControllerTest class.
 * @author Amey
 */
public class LoginParameterizableViewControllerTest extends AbstractWebIntegrationTestCase{
	LoginParameterizableViewController controller;
	AlertAjaxFacade alertAjaxFacade;
	Properties properties;
	private static final String LANGUAGE = "lang";
	private static final String VIDEO_URL = "videoUrl";
	private static final String ENGLISH = "en";
	private static final String SPANISH = "es";
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		controller = new LoginParameterizableViewController();
		properties = registerMockFor(Properties.class);
		controller.setProperties(properties);
		alertAjaxFacade = registerMockFor(AlertAjaxFacade.class);
		controller.setAlertAjaxFacade(alertAjaxFacade);
	}
	
	public void testHandleRequestInternal_en() throws Exception{
		ModelAndView modelAndView;
		request.setParameter(LANGUAGE, ENGLISH);
		expect(properties.getProperty(controller.HELP_VIDEO_URL_EN)).andReturn(controller.HELP_VIDEO_URL_EN);
		expect(alertAjaxFacade.fetchUpcommingAlerts()).andReturn(new ArrayList<Alert>()).anyTimes();
		
		replayMocks();
		modelAndView = controller.handleRequestInternal(request, response);
		verifyMocks();
		
		assertEquals(controller.HELP_VIDEO_URL_EN, modelAndView.getModelMap().get(VIDEO_URL));
	}
	
	public void testHandleRequestInternal_es() throws Exception{
		ModelAndView modelAndView;
		request.setParameter(LANGUAGE, SPANISH);
		expect(properties.getProperty(controller.HELP_VIDEO_URL_EN)).andReturn(controller.HELP_VIDEO_URL_EN);
		expect(properties.getProperty(controller.HELP_VIDEO_URL_ES)).andReturn(controller.HELP_VIDEO_URL_ES);
		expect(alertAjaxFacade.fetchUpcommingAlerts()).andReturn(new ArrayList<Alert>()).anyTimes();
		
		replayMocks();
		modelAndView = controller.handleRequestInternal(request, response);
		verifyMocks();
		
		assertEquals(controller.HELP_VIDEO_URL_ES, modelAndView.getModelMap().get(VIDEO_URL));
	}
}
