package gov.nih.nci.ctcae.web.login;

import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;

import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

/**FailedAuthenticationControllerTest class.
 * @author Amey
 */
public class FailedAuthenticationControllerTest extends AbstractWebIntegrationTestCase{
	FailedAuthenticationController controller;
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();	
		controller = new FailedAuthenticationController();
	}
	
	public void testHandleRequestInternal_WithValidLogin(){
		ModelAndView modelAndView = null;
		try{
			modelAndView = controller.handleRequestInternal(request, response);
		} catch (CtcAeSystemException e) {
			assertNull("Authenticated user is expected", e);
		} catch (Exception e) {
			assertNull("Authenticated user is expected", e);
		}
		assertNull("Authenticated user is present in the system", modelAndView);
	}
	
	public void testHandleRequestInternal_NoValidLogin(){
		ModelAndView modelAndView = new ModelAndView();
		SecurityContextHolder.clearContext();
		try{
			modelAndView = controller.handleRequestInternal(request, response);
		} catch (CtcAeSystemException e) {
			assertNotNull("No Authenticated user present in the system", e);
		} catch (Exception e) {
			assertNotNull("No Authenticated user present in the system", e);
		}
		assertNotNull("No authenticated user expected", modelAndView);
	}
	
}
