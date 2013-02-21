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

    public void testQuery() throws Exception {
    	
    	deleteTestData();
    	createTestData();
    	
        SymptomOverTimeWorstResponsesQuery query = new SymptomOverTimeWorstResponsesQuery("cycle");
        CRF crf = StudyTestHelper.getDefaultStudy().getCrfs().get(0);
        query.filterByCrf(crf.getId());
        query.filterByArm(crf.getStudy().getArms().get(0));

        List list = genericRepository.find(query);
        assertEquals(20, list.size());

    }


}