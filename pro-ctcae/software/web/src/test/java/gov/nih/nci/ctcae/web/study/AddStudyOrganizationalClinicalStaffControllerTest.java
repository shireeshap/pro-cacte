package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.servlet.ModelAndView;

/**
 * @author Amey
 * AddStudyOrganizationalClinicalStaffControllerTest class
 *
 */
public class AddStudyOrganizationalClinicalStaffControllerTest extends AbstractWebTestCase {
	
	AddStudyOrganizationalClinicalStaffController controller;
	StudyCommand command;
	private static String DELETE_LCRA = "deleteLCRA";
	private static String DELETE_PI = "deletePI";
	private static String DELETE_ODC = "deleteODC";
	private static String Add_LCRA = "addLeadCRA";
	private static String Add_PI = "addPI";
	private static String Add_ODC = "addODC";
	private static String LCRA_INDEX_TO_REMOVE = "lcraIndexToRemove";
	private static String ODC_INDEX_TO_REMOVE = "odcIndexToRemove";
	private static String PI_INDEX_TO_REMOVE = "piIndexToRemove";
	private static String GET = "GET";
	private static String ACTION = "action";
	private static String VIEW_NAME = "study/ajax/oneStudyOrganizationalClinicalStaffSection";
	private static String INDEX = "index";
	private static String COMMAND = "command";
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		controller = new AddStudyOrganizationalClinicalStaffController();
		command = new StudyCommand();
		request.getSession().setAttribute(StudyController.class.getName() + ".FORM.command", command);
	}
	
	public void testAddStudyOrganizationalClinicalStaffController(){
		assertEquals(1, controller.getSupportedMethods().length);
		assertEquals(GET,controller.getSupportedMethods()[0]);
	}
	
	public void testHandleRequestInternal_AddLcra() throws Exception{
		request.addParameter(ACTION, Add_LCRA);
		Integer lcraLastIndex = command.getLeadCRAs().size() - 1;
		
		ModelAndView modelAndView = controller.handleRequestInternal(request, response);

		assertEquals(VIEW_NAME, modelAndView.getViewName());
		lcraLastIndex++;
		assertEquals(lcraLastIndex.toString(), modelAndView.getModelMap().get(INDEX).toString());
		assertEquals(Role.LEAD_CRA, command.getLeadCRAs().get(0).getRole());
	}
	
	public void testHandleRequestInternal_AddSitePI() throws Exception{
		request.addParameter(ACTION, Add_PI);
		Integer lcraLastIndex = command.getLeadCRAs().size() - 1;
		
		ModelAndView modelAndView = controller.handleRequestInternal(request, response);

		assertEquals(VIEW_NAME, modelAndView.getViewName());
		lcraLastIndex++;
		assertEquals(lcraLastIndex.toString(), modelAndView.getModelMap().get(INDEX).toString());
		assertEquals(Role.PI, command.getLeadCRAs().get(0).getRole());
	}
	
	public void testPattern(){
		String regex = "<img[\t\n\r]*(.*?)";
		String input = "<img src=\\\"javascript:alert(351)>\\\"";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		
		String value = matcher.replaceAll("");
		System.out.println(value);
	}
}