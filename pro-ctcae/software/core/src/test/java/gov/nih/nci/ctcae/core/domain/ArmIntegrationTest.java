package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;

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


}
