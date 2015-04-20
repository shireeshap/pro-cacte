package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.AppMode;
import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueIdentifierForStudyValidator;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;

import org.springframework.validation.BindException;

/**
 * @author Amey
 * StudyDetailsTabTest class
 */
public class StudyDetailsTabTest extends AbstractWebTestCase {
	StudyDetailsTab tab;
	BindException errors;
	StudyCommand command;
	Study study;
	private static final String USER_LOGIN = "Ethan.Basch@demo.com";
	private static final String CALL_BACK_HOUR = "call_back_hour";
	private static final String CALL_BACK_FREQUENCY = "call_back_frequency";
	private static final String IVRS = "IVRS";
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		tab = new StudyDetailsTab();
		tab.setUserRepository(userRepository);
		command = new StudyCommand();
		errors = new BindException(command, StudyCommand.class.getName());
		UniqueIdentifierForStudyValidator validator = new UniqueIdentifierForStudyValidator();
		validator.setStudyRepository(studyRepository);
		tab.setUniqueIdentifierForStudyValidator(validator);
		study = StudyTestHelper.getDefaultStudy();
	}
	
	public void testGetRequiredPrivilege() {
		assertEquals(Privilege.PRIVILEGE_CREATE_STUDY, tab.getRequiredPrivilege());
	}
	
	public void testValidate_nonUniqueIdentifier() {
		command.setStudy(new Study());
		command.getStudy().setAssignedIdentifier(study.getAssignedIdentifier());
		
		tab.validate(command, errors);
		
		assertTrue(errors.hasErrors());
	}
	
	public void testValidate_uniqueIdentifier() {
		command.setStudy(new Study());
		command.getStudy().setAssignedIdentifier(study.getAssignedIdentifier() + "_1");
		
		tab.validate(command, errors);
		
		assertFalse(errors.hasErrors());
	}
	
	public void testOnDisplay_nonCCA() {
		login(USER_LOGIN);
		
		try {
			tab.onDisplay(request, command);
			
		} catch (Exception e) {
			assertTrue(e instanceof CtcAeSystemException);
		}
	}
	
	
	public void testOnDisplay_admin() {
		login(SYSTEM_ADMIN);
		
		try {
			tab.onDisplay(request, command);
			
		} catch (Exception e) {
			assertFalse(e instanceof CtcAeSystemException);
		}
		
		assertTrue(command.isAdmin());
	}
	
	public void testPostProcess_addModes() {
		command.setAppModes(new String[] {IVRS});
		request.addParameter(CALL_BACK_HOUR, "10");
		request.addParameter(CALL_BACK_FREQUENCY, "10");
		
		tab.postProcess(request, command, errors);
		
		assertEquals(AppMode.IVRS, command.getStudy().getStudyModes().get(0).getMode());
		assertEquals("10", command.getStudy().getCallBackHour().toString());
		assertEquals("10", command.getStudy().getCallBackFrequency().toString());
	}
}
