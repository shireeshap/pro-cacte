package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.core.helper.CrfTestHelper;
import gov.nih.nci.ctcae.core.query.reports.SymptomOverTimeWorstResponsesQuery;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * @author Harsh Agarwal
 * @since July 28, 2009
 */
public class SymptomOverTimeWorstResponsesQueryTest extends AbstractWebTestCase {

    public void testQuery() {

        SymptomOverTimeWorstResponsesQuery query = new SymptomOverTimeWorstResponsesQuery("cycle");
        CRF crf = StudyTestHelper.getDefaultStudy().getCrfs().get(0);
        query.filterByCrf(crf.getId());
        query.filterByArm(crf.getStudy().getArms().get(0).getId());
        query.filterBySymptom("Acne");

        List list = genericRepository.find(query);
        assertEquals(17, list.size());

    }


}