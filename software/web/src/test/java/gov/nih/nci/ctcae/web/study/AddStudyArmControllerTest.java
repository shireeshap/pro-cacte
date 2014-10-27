package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.web.AbstractWebTestCase;

import org.springframework.web.servlet.ModelAndView;

/**
 * @author Amey
 * AddStudyArmControllerTest class
 */
public class AddStudyArmControllerTest extends AbstractWebTestCase{
	AddStudyArmController controller;
	StudyCommand command;
	private static final String GET = "GET";
	private static final String VIEW_NAME = "study/ajax/oneStudyArmSection";
	private static final String ACTION = "action";
	private static final String INDEX = "index";
	private static final String DELETE_ARM = "deleteArm";
	private static final String ARM_INDEX = "armIndex";
	

	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		controller = new AddStudyArmController();
		command = new StudyCommand();
		request.getSession().setAttribute(StudyController.class.getName()+ ".FORM.command", command);
	}
	
	public void testAddStudyArmController(){
		assertEquals(1, controller.getSupportedMethods().length);
		assertEquals(GET, controller.getSupportedMethods()[0]);
	}
	
	public void testHandleRequestInternal_addArm() throws Exception{
		request.addParameter(ACTION, "");
		
		ModelAndView modelAndView = controller.handleRequestInternal(request, response);
		
		assertEquals(0, modelAndView.getModelMap().get(INDEX));
		assertEquals(VIEW_NAME, modelAndView.getViewName());
	}
	
	public void testHandleRequestInternal_deleteArm() throws Exception{
		request.addParameter(ACTION, DELETE_ARM);
		request.addParameter(ARM_INDEX, "0");
		
		ModelAndView modelAndView = controller.handleRequestInternal(request, response);
		
		assertEquals(1, command.getArmIndicesToRemove().size());
	}
}
