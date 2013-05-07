package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;

/**
 * @author AmeyS
 * Testcases for EditFormController.java
 *
 */
public class EditFormControllerTest extends AbstractWebTestCase {
	private EditFormController controller;
	private CreateFormCommand command;
	private Flow<CreateFormCommand> flow;
	private CRF crf;
	private Tab tab;
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		controller = new EditFormController();
		command = new CreateFormCommand();
		controller.setUserRepository(userRepository);
		controller.setGenericRepository(genericRepository);
		command.setCrf(StudyTestHelper.getDefaultStudy().getCrfs().get(0));
		controller.setPrivilegeAuthorizationCheck(privilegeAuthorizationCheck);
		crf = new CRF();
	}

	public void testGetInitialPage(){
		assertEquals(0, controller.getInitialPage(request));
	}
	
	public void testGetFlow_releasedForm(){
		command.setCrf(StudyTestHelper.getDefaultStudy().getCrfs().get(0));
		flow = controller.getFlow(command);
		assertEquals("form/confirmForm", flow.getTab(0).getViewName());
	}
	
	public void testGetFlow_draftForm(){
		crf.setStudy(StudyTestHelper.getDefaultStudy());
		command.setCrf(crf);
		flow = controller.getFlow(command);
		assertEquals(4, flow.getTabCount());
	}
	
	public void testShouldSave(){
		tab = new CalendarTemplateTab();
		assertTrue(controller.shouldSave(request, command, tab));
		
		tab = new Tab("", "", "form/site_rules");
		assertFalse(controller.shouldSave(request, command, tab));
	}
}
