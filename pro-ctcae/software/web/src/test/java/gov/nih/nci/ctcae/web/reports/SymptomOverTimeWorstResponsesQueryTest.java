package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.query.reports.SymptomOverTimeWorstResponsesQuery;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;

import java.util.List;

/**
 * @author Harsh Agarwal
 * @since July 28, 2009
 */
public class SymptomOverTimeWorstResponsesQueryTest extends AbstractWebTestCase {

	
	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
    	saveCsv(true);
	}
	
    public void testQuery() throws Exception {
        SymptomOverTimeWorstResponsesQuery query = new SymptomOverTimeWorstResponsesQuery("cycle");
        CRF crf = StudyTestHelper.getDefaultStudy().getCrfs().get(0);
        query.filterByCrf(crf.getId());
        query.filterByArm(crf.getStudy().getArms().get(0));
        System.out.println(crf.getTitle());
        System.out.println(crf.getStudy().getArms().get(0).getId());
        System.out.println(StudyTestHelper.getDefaultStudy().getArms().get(0).getId());

        List list = genericRepository.find(query);
        assertEquals(20, list.size());
    }

    @Override
    protected void onTearDownInTransaction() throws Exception {
    	super.onTearDownInTransaction();
    }
    
    protected final boolean isTestDataPresent() {
        return false ;
    }

}