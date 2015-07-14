package gov.nih.nci.ctcae.core.domain;

import java.util.List;

import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;

/**
 * @author Mehul Gulati
 *         Date: Jul 13, 2009
 */
public class ArmIntegrationTest extends TestDataManager {

	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
	}
	
    public void testStudyArm() {

    	System.out.println("testStudyArm starting..");
    	Study study = StudyTestHelper.getDefaultStudy();
        assertEquals(2, study.getArms().size());
        Arm arm  = new Arm();
        arm.setStudy(study);
        arm.setTitle("test arm 1");
        arm.setDescription("desc 1");
        assertTrue(study.getArms().contains(arm));
        arm.setTitle("test arm 2");
        arm.setDescription("desc 2");
        assertTrue(study.getArms().contains(arm));

//        assertEquals("", study.getArms().get(0).getTitle());
//        assertEquals("desc 1", study.getArms().get(0).getDescription());
//        assertEquals("test arm 2", study.getArms().get(1).getTitle());
//        assertEquals("desc 2", study.getArms().get(1).getDescription());
        System.out.println("testStudyArm complete..");
    }
    
    public void testEqualsAndHashCode(){
    	System.out.println("testEqualsAndHashCode starting..");
    	Study study = StudyTestHelper.getDefaultStudy();
    	Arm studyArm = study.getArms().get(0);
    	Arm fetchedArm = fetchArmFromRepository(studyArm.getTitle(), study.getId()).get(0);
    	Arm otherArm = fetchArmFromRepositoryForOtherStudy(study.getId()).get(0);

    	assertTrue(studyArm.equals(fetchedArm));
    	assertEquals(studyArm.hashCode(), fetchedArm.hashCode());
    	
    	assertFalse(studyArm.equals(otherArm));
    	assertNotSame(studyArm.hashCode(), otherArm.hashCode());
    	System.out.println("tessEqualsAndHashCode complete..");
    }
    
    public void testIsDefaultArm(){
    	System.out.println("testIsDefaultArm starting..");
    	Study study = StudyTestHelper.getDefaultStudy();
    	Arm studyArm = study.getArms().get(0);
    	
    	assertFalse(studyArm.isDefaultArm());
    	System.out.println("testIsDefaultArm complete..");
    }
    
    public List<Arm> fetchArmFromRepository(String title, Integer studyId){
    	return hibernateTemplate.find("from Arm where title = ? and study.id = ?", new Object[]{title,studyId});
    }

    public List<Arm> fetchArmFromRepositoryForOtherStudy(Integer studyId){
    	return hibernateTemplate.find("from Arm where study.id <> ?", new Object[]{studyId});
    }

    @Override
    protected void onTearDownInTransaction() throws Exception {
    	super.onTearDownInTransaction();
    }
    
}
