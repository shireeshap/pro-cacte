package gov.nih.nci.ctcae.web.login;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;

/**CrfActivityDateComparatorTest class.
 * @author Amey
 */
public class CrfActivityDateComparatorTest extends AbstractWebTestCase{
	CRF crfOne, crfTwo;
	Study defaultStudy, secondaryStudy;
	CrfActivityDateComparator comparator;
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		defaultStudy = StudyTestHelper.getDefaultStudy();
		secondaryStudy = StudyTestHelper.getSecondaryStudy();
		comparator = new CrfActivityDateComparator();
	}
	
	public void testCompare(){
		crfOne = defaultStudy.getCrfs().get(0);
		int result = comparator.compare(crfOne, crfOne);
		assertEquals(0, result);
	}
	
	public void testCompare_differentCrfs(){
		crfOne = defaultStudy.getCrfs().get(0);
		crfTwo = secondaryStudy.getCrfs().get(0);
		int result = comparator.compare(crfOne, crfTwo);
		assertNotSame(0, result);
	}
	
	public void testCompare_differentCrfTitle(){
		crfOne = defaultStudy.getCrfs().get(0);
		crfTwo = secondaryStudy.getCrfs().get(0);
		crfTwo.setStudy(defaultStudy);
		int result = comparator.compare(crfOne, crfTwo);
		assertNotSame(0, result);
	}
	
	public void testCompare_differentActivityDate(){
		crfOne = defaultStudy.getCrfs().get(0);
		crfTwo = secondaryStudy.getCrfs().get(0);
		crfTwo.setStudy(defaultStudy);
		crfTwo.setTitle(crfOne.getTitle());
		crfTwo.setActivityDate(DateUtils.addDaysToDate(crfOne.getActivityDate(), 1));
		int result = comparator.compare(crfOne, crfTwo);
		assertNotSame(0, result);
	}
}