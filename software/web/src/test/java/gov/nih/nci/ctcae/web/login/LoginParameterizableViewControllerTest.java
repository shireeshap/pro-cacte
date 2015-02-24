package gov.nih.nci.ctcae.web.login;

import static org.easymock.EasyMock.expect;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

import java.util.Properties;

import org.springframework.web.servlet.ModelAndView;

/**LoginParameterizableViewControllerTest class.
 * @author Amey
 */
public class LoginParameterizableViewControllerTest extends AbstractWebIntegrationTestCase{
	LoginParameterizableViewController controller;
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
	}
	
	public void testHandleRequestInternal_en() throws Exception{
		ModelAndView modelAndView;
		request.setParameter(LANGUAGE, ENGLISH);
		expect(properties.getProperty(controller.HELP_VIDEO_URL_EN)).andReturn(controller.HELP_VIDEO_URL_EN);
		
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
		
		replayMocks();
		modelAndView = controller.handleRequestInternal(request, response);
		verifyMocks();
		
		assertEquals(controller.HELP_VIDEO_URL_ES, modelAndView.getModelMap().get(VIDEO_URL));
	}
}
