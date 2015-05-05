package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

import org.springframework.web.servlet.ModelAndView;

/**
 * @author Amey
 * AddStudyComponentControllerTest class
 */
public class AddStudyComponentControllerTest extends AbstractWebIntegrationTestCase{
	AddStudyComponentController controller;
	Study study;
	StudyCommand command;
	private static final String COMPONENT_TYPE = "componentType";
	private static final String STUDY_SITE_ID = "studySiteId";
	private final static String STUDY_ORGANIZATION_CLINICAL_STAFF = "studyOrganizationClinicalStaff";
	private final static String ROLE = "role";
	private final static String VIEW_NAME = "study/ajax/studyOrganizationClinicalStaffSection";
	private final static String SOCS_INDEX = "studyOrganizationClinicalStaffIndex";
	private final static String ACTION = "action";
	private final static String DELETE = "delete";
	
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		controller = new AddStudyComponentController();
		study = StudyTestHelper.getDefaultStudy();
		command = new StudyCommand();
		command.setStudy(study);
		request.getSession().setAttribute(StudyController.class.getName() + ".FORM.command", command);
	}
	
	public void testHandleRequestInternal_addSocs() throws Exception{
		request.addParameter(COMPONENT_TYPE, STUDY_ORGANIZATION_CLINICAL_STAFF);
		request.addParameter(ROLE, Role.SITE_PI.getDisplayName());
		request.addParameter(STUDY_SITE_ID, study.getStudySites().get(0).getId().toString());
		Integer socsLastIndex = command.getStudyOrganizationClinicalStaffs().size() - 1;
		
		ModelAndView modelAndView = controller.handleRequestInternal(request, response);
		
		assertEquals(VIEW_NAME, modelAndView.getViewName());
		assertEquals(socsLastIndex + 1, modelAndView.getModelMap().get(SOCS_INDEX));
		assertEquals(1, command.getNewlyAddedSocsForSelectedSite().size());
		
	}
	
	public void testHandleRequestInternal_deleteSocs() throws Exception{
		request.addParameter(COMPONENT_TYPE, STUDY_ORGANIZATION_CLINICAL_STAFF);
		request.addParameter(ROLE, Role.SITE_PI.getDisplayName());
		request.addParameter(STUDY_SITE_ID, study.getStudySites().get(0).getId().toString());
		
		ModelAndView modelAndView = controller.handleRequestInternal(request, response);
		request.addParameter(ACTION, DELETE);
		Integer socsLastIndex = (Integer) modelAndView.getModelMap().get(SOCS_INDEX);
		request.addParameter(SOCS_INDEX, socsLastIndex.toString());
		
		modelAndView = controller.handleRequestInternal(request, response);
		
		assertEquals(0, command.getNewlyAddedSocsForSelectedSite().size());
		assertEquals(socsLastIndex - 1, command.getStudyOrganizationClinicalStaffs().size() - 1);
		
	}
}
