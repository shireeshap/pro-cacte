package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

import javax.servlet.ServletException;

/**
 * @author Amey
 * EditStudyControllerTest class
 */
public class EditStudyControllerTest extends AbstractWebIntegrationTestCase{
	EditStudyController controller;
	 private static final String STUDY_ID = "studyId";
	 StudyCommand command;
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		controller = new EditStudyController();
		controller.setStudyRepository(studyRepository);
		controller.setGenericRepository(genericRepository);
	}
	
	public void testFormBackingObject() throws ServletException {
		request.setParameter(STUDY_ID, StudyTestHelper.getDefaultStudy().getId().toString());
		
		command = (StudyCommand) controller.formBackingObject(request);
		
		assertEquals(StudyTestHelper.getDefaultStudy(), command.getStudy());
	}
}
