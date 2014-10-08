package gov.nih.nci.ctcae.web.study;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.service.AuthorizationServiceImpl;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Amey
 * StudyAjaxFacadeTest class
 */
public class StudyAjaxFacadeTest extends AbstractWebIntegrationTestCase {
	StudyAjaxFacade facade = new StudyAjaxFacade();
	AuthorizationServiceImpl authorizationServiceImpl;
	private static final String SORT = "shortTitle";
	private static final String DIR = "ASC";
	private static final String USER_LOGIN = "Ethan.Basch@demo.com";
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		facade = new StudyAjaxFacade();
		facade.setStudyRepository(studyRepository);
		authorizationServiceImpl = registerMockFor(AuthorizationServiceImpl.class);
		facade.setAuthorizationServiceImpl(authorizationServiceImpl);
	}
	
	public void testResultCount_validSearchString() {
		String searchStrings[] = {"collection"};
		
		Long resultCount = facade.resultCount(searchStrings);
		
		assertEquals("1", resultCount.toString());
	}
	
	public void testResultCount_blankSearchString() {
		String searchStrings[] = {""};
		
		Long resultCount = facade.resultCount(searchStrings);
		
		assertEquals("2", resultCount.toString());
	}
	
	public void testSearchStudies_validSearchString() {
		String searchStrings[] = {"collection"};
		
		List<Study> studies = facade.searchStudies(searchStrings, 0, 25, SORT, DIR, 25);
		
		assertEquals(1, studies.size());
	}
	
	public void testSearchStudies_blankSearchString() {
		String searchStrings[] = {"collection"};
		
		List<Study> studies = facade.searchStudies(searchStrings, 0, 25, SORT, DIR, 25);
		
		assertEquals(1, studies.size());
	}
	
	public void testMatchStudy_adminLogin() {
		List<Study> studies = facade.matchStudy("collection", Privilege.PRIVILEGE_EDIT_STUDY);
		
		assertEquals(1, studies.size());
	}
	
	public void testMatchStudy_nonAdminLogin() {
		List<Role> roles = new ArrayList<Role>();
		roles.add(Role.PI);
		expect(authorizationServiceImpl.findRolesForPrivilege(isA(User.class), isA(String.class))).andReturn(roles).once();
		expect(authorizationServiceImpl.hasRole(isA(Study.class), isA(List.class), isA(User.class))).andReturn(false).once();
		
		replayMocks();
		login(USER_LOGIN);
		List<Study> studies = facade.matchStudy("collection", Privilege.PRIVILEGE_EDIT_STUDY);
		verifyMocks();
		
		assertEquals(0, studies.size());
	}
}
