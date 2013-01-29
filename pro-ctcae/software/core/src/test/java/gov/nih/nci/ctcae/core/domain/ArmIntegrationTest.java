package gov.nih.nci.ctcae.core.domain;

import java.util.List;

import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;

/**
 * @author Mehul Gulati
 *         Date: Jul 13, 2009
 */
public class ArmIntegrationTest extends TestDataManager {

    public void testStudyArm() {

        Study study = StudyTestHelper.getDefaultStudy();
        assertEquals(2, study.getArms().size());
        assertEquals("test arm 1", study.getArms().get(0).getTitle());
        assertEquals("desc 1", study.getArms().get(0).getDescription());
        assertEquals("test arm 2", study.getArms().get(1).getTitle());
        assertEquals("desc 2", study.getArms().get(1).getDescription());
    }
    
    public void testEqualsAndHashCode(){
    	Study study = StudyTestHelper.getDefaultStudy();
    	Arm studyArm = study.getArms().get(0);
    	Arm fetchedArm = fetchArmFromRepository(studyArm.getTitle(), study.getId()).get(0);
    	Arm otherArm = fetchArmFromRepositoryForOtherStudy(studyArm.getTitle(), study.getId()).get(0);

    	assertTrue(studyArm.equals(fetchedArm));
    	assert(studyArm.hashCode() == fetchedArm.hashCode());
    	
    	assertFalse(studyArm.equals(otherArm));
    	assert(studyArm.hashCode() != otherArm.hashCode());
    }
    
    public void testIsDefaultArm(){
    	Study study = StudyTestHelper.getDefaultStudy();
    	Arm studyArm = study.getArms().get(0);
    	
    	assertFalse(studyArm.isDefaultArm());
    }
    
    public List<Arm> fetchArmFromRepository(String title, Integer studyId){
    	return hibernateTemplate.find("from Arm where title = ? and study.id = ?", new Object[]{title,studyId});
    }

    public List<Arm> fetchArmFromRepositoryForOtherStudy(String title, Integer studyId){
    	return hibernateTemplate.find("from Arm where title = ? and study.id <> ?", new Object[]{title,studyId});
    }


}
