package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.web.AbstractWebTestCase;

public class StudySiteTabTest extends AbstractWebTestCase {
	
	StudySitesTab tab = new StudySitesTab();
	
	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
		request.setParameter("studyId", "1");
	}
	
	public void testGetRequiredPrivilege(){
		login("ethan.basch@demo.com");
		assertEquals("PRIVILEGE_ADD_STUDY_SITE.study.1", tab.getRequiredPrivilege());
	}

}
