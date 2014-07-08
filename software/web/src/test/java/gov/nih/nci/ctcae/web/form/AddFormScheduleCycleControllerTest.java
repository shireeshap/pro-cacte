package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRFCycleDefinition;
import gov.nih.nci.ctcae.core.domain.FormArmSchedule;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.web.WebTestCase;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author AmeyS 
 * TestCases for AddFormScheduleCycleController.
 * Includes tests for adding new crfCycleDefinitions to a CRF.
 */
public class AddFormScheduleCycleControllerTest extends WebTestCase {

    private AddFormScheduleCycleController controller;
    private CreateFormCommand command;
    private FormArmSchedule formArmSchedule;
    private CRFCycleDefinition crfCycleDefinition;
    private CRFCycleDefinition crfCycleDefinition2;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new AddFormScheduleCycleController();
        command = new CreateFormCommand();
        StudySite studySite = new StudySite();
        command.setMyOrg(studySite);
        formArmSchedule = new FormArmSchedule();
        crfCycleDefinition = new CRFCycleDefinition();
        crfCycleDefinition2 = new CRFCycleDefinition();
    }
    
    public void testHandleRequestInternal() throws Exception{
    	command.setSelectedFormArmSchedule(formArmSchedule);
    	formArmSchedule.addCrfCycleDefinition(crfCycleDefinition);
    	crfCycleDefinition.setFormArmSchedule(formArmSchedule);
    	crfCycleDefinition.setOrder(0);
    	formArmSchedule.addCrfCycleDefinition(crfCycleDefinition2);
    	crfCycleDefinition2.setFormArmSchedule(formArmSchedule);
    	crfCycleDefinition2.setOrder(1);
    	
    	request.getSession().setAttribute(FormController.class.getName()+".FORM."+"command", command);
    	
    	assertEquals("asserting that 2 cycleDefinitions are allready present", 2, command.getSelectedFormArmSchedule().getCrfCycleDefinitions().size());    	
    	replayMocks();
    	ModelAndView modelAndView = controller.handleRequestInternal(request, response);
    	verifyMocks();
    	
    	assertEquals("form/ajax/formScheduleCycleDefinition", modelAndView.getViewName());
    	assertEquals("expect new added cycleDefinition's index to be 2", 2, modelAndView.getModel().get("cycleDefinitionIndex"));
    	assertEquals("expect number of crfCycleDefinitions to be 3", 3, command.getSelectedFormArmSchedule().getCrfCycleDefinitions().size());    	
    }
}