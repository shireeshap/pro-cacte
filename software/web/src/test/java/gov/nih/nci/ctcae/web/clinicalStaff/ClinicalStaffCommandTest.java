package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import junit.framework.TestCase;

/**
 * @author Amey
 * ClinicalStaffCommandTest class
 */
public class ClinicalStaffCommandTest extends TestCase{
	ClinicalStaffCommand clinicalStaffCommand;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		clinicalStaffCommand = new ClinicalStaffCommand();
	}
	
	public void testClinicalStaffCommand(){
		assertNotNull(clinicalStaffCommand.getClinicalStaff());
		assertEquals(1, clinicalStaffCommand.getClinicalStaff().getOrganizationClinicalStaffs().size());
	}
	
	public void testSetClinicalStaff(){
		ClinicalStaff clinicalStaff = new ClinicalStaff();
		
		clinicalStaffCommand.setClinicalStaff(clinicalStaff);
		assertFalse(clinicalStaffCommand.isAdmin());
		assertFalse(clinicalStaffCommand.getCca());
	}
	
	public void testApply_checkCCA(){
		ClinicalStaff clinicalStaff = new ClinicalStaff();
		clinicalStaff.setUser(new User());
		clinicalStaffCommand.setClinicalStaff(clinicalStaff);
		clinicalStaffCommand.setCca(true);
		clinicalStaffCommand.setUserAccount(true);

		clinicalStaffCommand.apply();
		
		assertTrue(clinicalStaffCommand.getClinicalStaff().getUser().hasRole(Role.CCA));
		assertFalse(clinicalStaffCommand.getClinicalStaff().getUser().hasRole(Role.ADMIN));
	}
	
	public void testApply_checkAdmin(){
		ClinicalStaff clinicalStaff = new ClinicalStaff();
		clinicalStaff.setUser(new User());
		clinicalStaffCommand.setClinicalStaff(clinicalStaff);
		clinicalStaffCommand.setAdmin(true);
		clinicalStaffCommand.setUserAccount(true);

		clinicalStaffCommand.apply();
		
		assertTrue(clinicalStaffCommand.getClinicalStaff().getUser().hasRole(Role.ADMIN));
		assertFalse(clinicalStaffCommand.getClinicalStaff().getUser().hasRole(Role.CCA));
	}

}
