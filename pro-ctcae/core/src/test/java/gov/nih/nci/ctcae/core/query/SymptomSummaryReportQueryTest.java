package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.query.reports.ReportParticipantCountQuery;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import java.util.List;

/**
 * @author Harsh Agarwal
 * @since Oct 7, 2008
 */
public class SymptomSummaryReportQueryTest extends AbstractDependencyInjectionSpringContextTests {

    private GenericRepository genericRepository;

    private static final String[] context = new String[]{
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-core.xml",
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-datasource.xml",
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-util.xml"
    };

    @Override
    protected String[] getConfigLocations() {
        return context;
    }



    public void testDetailsQuery() throws Exception {
        StudyParticipantCrfItemQuery query = new StudyParticipantCrfItemQuery();
        query.filterBySymptomId(11);
//        query.filterByAttributes(ProCtcQuestionType.SEVERITY);
        query.filterByScheduleStartDate(DateUtils.parseDate("05/10/2009"), DateUtils.parseDate("05/12/2009"));
        query.filterByCrf(1);
        query.filterByStudySite(15);
        List result = genericRepository.find(query);
        for (Object obj : result) {
            Object[] a = (Object[]) obj;
        }

    }

    public void testParticipantCountQuery() throws Exception {
        ReportParticipantCountQuery query = new ReportParticipantCountQuery();
        query.filterBySymptomId(11);
//        query.filterByAttributes(ProCtcQuestionType.SEVERITY);
        query.filterByScheduleStartDate(DateUtils.parseDate("05/10/2009"), DateUtils.parseDate("05/12/2009"));
        query.filterByCrf(1);
        query.filterByStudySite(15);
        List result = genericRepository.find(query);

    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}