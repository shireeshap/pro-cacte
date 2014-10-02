package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.RoleStatus;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Amey
 * ChangeStatusControllerTest test
 */
public class ChangeStatusControllerTest  extends AbstractWebIntegrationTestCase{
	ChangeStatusController controller;
	StudyOrganizationClinicalStaff studyOrganizationClinicalStaff;
	BindException errors;
	private static final String ID = "id";
	private static final String STATUS = "status";
	private static final String VIEW_NAME = "study/changeStatus";
	private static final String TAB_NUMBER = "tabNumber";
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		controller = new ChangeStatusController();
		studyOrganizationClinicalStaff = StudyTestHelper.getDefaultStudy().getAllStudyOrganizationClinicalStaffs().get(0);
		controller.setStudyOrganizationClinicalStaffRepository(studyOrganizationClinicalStaffRepository);
		errors = registerMockFor(BindException.class);
		
	}
	
	public void testChangeStatusController() {
		controller = new ChangeStatusController();
		
		assertEquals(VIEW_NAME, controller.getFormView());
		assertTrue(controller.isSessionForm());
		assertEquals(StudyOrganizationClinicalStaff.class.getName(), controller.getCommandClass().getName());
	}
	
	public void testFormBackingObject() throws Exception {
		request.setParameter(ID, studyOrganizationClinicalStaff.getId().toString());
		
		StudyOrganizationClinicalStaff command = (StudyOrganizationClinicalStaff) controller.formBackingObject(request);
		
		assertEquals(studyOrganizationClinicalStaff, command);
	}
	
	public void testOnSubmit_StatusActive() throws Exception {
		RoleStatus status = RoleStatus.ACTIVE;
		request.setParameter(STATUS, status.getDisplayName());
		request.setParameter(TAB_NUMBER, "2");
		
		
		ModelAndView modelAndView = controller.onSubmit(request, response, studyOrganizationClinicalStaff, errors);
		
		assertNotSame(status, studyOrganizationClinicalStaff.getRoleStatus());
		assertEquals("editStudy?tab=2&studyId=" + StudyTestHelper.getDefaultStudy().getId(), ((RedirectView) modelAndView.getView()).getUrl());
	}
	
	
	public void testOnSubmit_StatusInActive() throws Exception {
		RoleStatus status = RoleStatus.IN_ACTIVE;
		request.setParameter(STATUS, status.getDisplayName());
		request.setParameter(TAB_NUMBER, "2");
		controller.setClinicalStaffRepository(clinicalStaffRepository);
		
		
		ModelAndView modelAndView = controller.onSubmit(request, response, studyOrganizationClinicalStaff, errors);
		
		assertNotSame(status, studyOrganizationClinicalStaff.getRoleStatus());
		assertEquals("editStudy?tab=2&studyId=" + StudyTestHelper.getDefaultStudy().getId(), ((RedirectView) modelAndView.getView()).getUrl());
	}
}