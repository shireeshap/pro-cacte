package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.commons.utils.DateUtils;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.HashSet;

/**
 * @author
 * @crated Oct 7, 2008
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

    public void testSummaryQuery() throws Exception {
        SymptomSummaryReportQuery query = new SymptomSummaryReportQuery();
        query.filterBySymptomId(11);
        query.filterByAttribute(ProCtcQuestionType.SEVERITY);
        query.filterByResponse("Moderate");
        query.filterByParticipantGender("Male");
        query.filterByScheduleStartDate(DateUtils.parseDate("05/10/2009"), DateUtils.parseDate("05/12/2009"));
        query.filterByCrf(1);
        query.filterByStudySite(15);
        System.out.println(query.getQueryString());
        List result = genericRepository.find(query);
        for (Object obj : result) {
            Object[] a = (Object[]) obj;
            System.out.println(a[0] + "," + a[1]);
        }

    }

    public void testDetailsQuery() throws Exception {
        SymptomSummaryReportDetailsQuery query = new SymptomSummaryReportDetailsQuery();
        query.filterBySymptomId(11);
        query.filterByAttribute(ProCtcQuestionType.SEVERITY);
        query.filterByResponse("Moderate");
        query.filterByParticipantGender("Male");
        query.filterByScheduleStartDate(DateUtils.parseDate("05/10/2009"), DateUtils.parseDate("05/12/2009"));
        query.filterByCrf(1);
        query.filterByStudySite(15);
        System.out.println(query.getQueryString());
        List result = genericRepository.find(query);
        for (Object obj : result) {
            Object[] a = (Object[]) obj;
            System.out.println(a[0] + "," + a[1]);
        }

    }

    public void testParticipantCountQuery() throws Exception {
        SymptomSummaryParticipantCountQuery query = new SymptomSummaryParticipantCountQuery();
        query.filterBySymptomId(11);
        query.filterByAttribute(ProCtcQuestionType.SEVERITY);
        query.filterByResponse("Moderate");
        query.filterByParticipantGender("Male");
        query.filterByScheduleStartDate(DateUtils.parseDate("05/10/2009"), DateUtils.parseDate("05/12/2009"));
        query.filterByCrf(1);
        query.filterByStudySite(15);
        System.out.println(query.getQueryString());
        List result = genericRepository.find(query);
        System.out.println(result.get(0));

    }

    public void testSymptomOverTimeQuery() throws Exception {
        SymptomOverTimeReportQuery query = new SymptomOverTimeReportQuery();
        query.filterBySymptomId(11);
        query.filterByAttribute(ProCtcQuestionType.SEVERITY);
        query.filterByParticipantGender("Male");
//        query.filterByScheduleStartDate(DateUtils.parseDate("05/10/2009"), DateUtils.parseDate("05/12/2009"));
        query.filterByCrf(1);
        query.filterByStudySite(15);
        System.out.println(query.getQueryString());
        List result = genericRepository.find(query);
         for (Object obj : result) {
            Object[] a = (Object[]) obj;
            System.out.println(a[0] + "," + a[1]);
        }

    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}