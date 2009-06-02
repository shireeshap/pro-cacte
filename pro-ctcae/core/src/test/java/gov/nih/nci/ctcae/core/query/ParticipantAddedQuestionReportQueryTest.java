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
public class ParticipantAddedQuestionReportQueryTest extends AbstractDependencyInjectionSpringContextTests {

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

    public void testQuery() throws Exception {
        ParticipantAddedQuestionsReportQuery query = new ParticipantAddedQuestionsReportQuery();
        query.filterByCrf(3);
//        query.filterByStudySite(15);
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