package gov.nih.nci.ctcae.core.domain;

import java.util.List;

import gov.nih.nci.ctcae.core.helper.TestDataManager;

public class RolePrivilegeTest extends TestDataManager{

	private static String PRIVILEGE_CREATE_ADMIN= "PRIVILEGE_CREATE_ADMIN";
	private static Integer IDENTIFIER = -404; 
	private static String PRIVILEGE = "Privilege";
	private static String ROLEPRIVILEGE	= "RolePrivilege";
	private static String FIELD_PRIVILEGE_NAME = "privilegeName";
	
	RolePrivilege rolePrivilege1;
	RolePrivilege rolePrivilege2;
	Privilege privilege1;
	Privilege privilege2;
	
	public RolePrivilegeTest(){
		privilege1 = new Privilege();
		privilege1.setId(-404);
		privilege1.setPrivilegeName("gov.nih.nci.ctcae.core.domain.StudyOrganization.GROUP");
		privilege1.setDisplayName("gov.nih.nci.ctcae.core.domain.StudyOrganization.GROUP");
		
		privilege2 = new Privilege();
		privilege2.setId(-403);
		privilege2.setPrivilegeName("gov.nih.nci.ctcae.core.domain.Study.GROUP");
		privilege2.setDisplayName("gov.nih.nci.ctcae.core.domain.Study.GROUP");
		
		rolePrivilege1 = new RolePrivilege();
		rolePrivilege1.setId(-11);
		rolePrivilege1.setPrivilege(privilege1);
		rolePrivilege1.setRole(Role.ADMIN);
		
		rolePrivilege2 = new RolePrivilege();
		rolePrivilege2.setId(-12);
		rolePrivilege2.setPrivilege(privilege2);
		rolePrivilege2.setRole(Role.CCA);
	}
	
	public void testPrivilegeEqualsAndHashCode(){
		Privilege privilege = getPrivelege(PRIVILEGE_CREATE_ADMIN, PRIVILEGE, FIELD_PRIVILEGE_NAME).get(0);
		Privilege otherPrivilege = getPrivelegeById(PRIVILEGE, IDENTIFIER).get(0);
		
		assertTrue(privilege.getPrivilegeName().equals(PRIVILEGE_CREATE_ADMIN));
	
		assertFalse(privilege.equals(otherPrivilege));
		assertFalse(privilege.hashCode() == otherPrivilege.hashCode());
		assertFalse(privilege.getDisplayName().equals(otherPrivilege.getDisplayName()));
		
		assertTrue(privilege.equals(getPrivelegeById(PRIVILEGE, privilege.getId()).get(0)));
		assertTrue(privilege.hashCode() == (getPrivelegeById(PRIVILEGE, privilege.getId()).get(0)).hashCode());
		assertTrue(privilege.getDisplayName().equals((getPrivelegeById(PRIVILEGE, privilege.getId()).get(0)).getDisplayName()));
	}
	
	public void testRolePrivilegeHashcodeAndEquals(){
		Privilege otherPrivilege = getPrivelegeById(PRIVILEGE, -404).get(0);
		
		assertTrue(otherPrivilege.equals(privilege1));
		assertEquals(otherPrivilege.hashCode(), privilege1.hashCode());
	}
	
	public void testGetPrivilegeAndRole(){
		
		assertEquals(rolePrivilege1.getPrivilege(), privilege1);
		assertNotSame(rolePrivilege1.getPrivilege(), privilege2);
		
		assertEquals(rolePrivilege2.getRole(), Role.CCA);
	}
	
	
	public List<Privilege> getPrivelege(String privelegeName, String domainObject, String field){
		return hibernateTemplate.find("from "+ domainObject +" where "+ field +" = ?",new Object[]{privelegeName});
	}
	
	public List<Privilege> getPrivelegeById(String domainObject, Integer id){
		return hibernateTemplate.find("from "+ domainObject +" where id = ?",new Object[]{id});
	}
}
