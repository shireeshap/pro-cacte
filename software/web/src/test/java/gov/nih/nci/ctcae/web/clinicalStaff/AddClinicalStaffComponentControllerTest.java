package gov.nih.nci.ctcae.web.clinicalStaff;

import org.springframework.web.servlet.ModelAndView;

import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.helper.ClinicalStaffTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;

/**
 * @author Amey
 * AddClinicalStaffComponentControllerTest class
 */
public class AddClinicalStaffComponentControllerTest extends AbstractWebTestCase{
	
	private final static String ACTION = "action";
	private final static String ORGANIZATION_CLINICAL_STAFF_INDEX = "organizationClinicalStaffIndex";
	private final static String BLANK = "";
	private final static String MODEL_AND_VIEW_NAME = "clinicalStaff/organizationClinicalStaffSection";
	private final static String ORGANIZATIONAL_STAFF_INDEX = "organizationClinicalStaffIndex";
	AddClinicalStaffComponentController controller = new AddClinicalStaffComponentController();
	ClinicalStaffCommand command;
	
	@Override
	protected void onSetUp() throws Exception {
		// TODO Auto-generated method stub
		super.onSetUp();
		command = new ClinicalStaffCommand();
	}
	
	public void testDeleteSiteComponent() throws Exception{
		request.setParameter(ACTION, "delete");
		request.getSession().setAttribute(CreateClinicalStaffController.class.getName() +".FORM." + "clinicalStaffCommand", command);
		request.setParameter(ORGANIZATION_CLINICAL_STAFF_INDEX, "2");
		request.setMethod("GET");
		login(ClinicalStaffTestHelper.getDefaultClinicalStaff().getUser().getUsername());
		
		assertEquals(command.getIndexesToRemove().size(), 0);
		controller.handleRequest(request, response);
		assertEquals(command.getIndexesToRemove().size(), 1);
	}
	
	public void testAddSiteComponent() throws Exception{
		request.setParameter(ACTION, BLANK);
		request.getSession().setAttribute(CreateClinicalStaffController.class.getName() +".FORM." + "clinicalStaffCommand", command);
		command.getClinicalStaff().addOrganizationClinicalStaff(new OrganizationClinicalStaff());
		command.getClinicalStaff().addOrganizationClinicalStaff(new OrganizationClinicalStaff());
		request.setMethod("GET");
		
		ModelAndView modelAndView = controller.handleRequest(request, response);
		assertEquals(modelAndView.getViewName(), MODEL_AND_VIEW_NAME);
		assertEquals(modelAndView.getModelMap().get(ORGANIZATIONAL_STAFF_INDEX), 3);
	}

}
