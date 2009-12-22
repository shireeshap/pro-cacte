package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfAddedQuestion;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfScheduleAddedQuestion;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import java.util.List;

/**
 * @author
 * @since Oct 7, 2008
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
        List result = genericRepository.find(query);
        for (Object obj : result) {
            Object[] a = (Object[]) obj;
            System.out.println(a[0] + "," + a[1]);
        }
    }

    public void testDetailsQuery() throws Exception {
        ParticipantAddedQuestionsDetailsQuery query = new ParticipantAddedQuestionsDetailsQuery();
        query.filterByCrf(3);
//        query.filterByStudySite(15);
        query.filterBySymptom("Acne");
        List result = genericRepository.find(query);
        for (Object obj : result) {
            Object[] m = (Object[]) obj;
            StudyParticipantCrfScheduleAddedQuestion a = (StudyParticipantCrfScheduleAddedQuestion) m[1];
            StudyParticipantCrfAddedQuestion b = (StudyParticipantCrfAddedQuestion) m[0];
//
//            StudyParticipantCrfAddedQuestion o = genericRepository.findById(StudyParticipantCrfAddedQuestion.class, ((StudyParticipantCrfAddedQuestion) m[0]).getId());
//            o.getStudyParticipantCrfScheduleAddedQuestions();
//            for (StudyParticipantCrfScheduleAddedQuestion q : o.getStudyParticipantCrfScheduleAddedQuestions()) {
//                System.out.println(q.getProCtcQuestion().getProCtcTerm().getTerm() + "," + q.getProCtcQuestion().getProCtcQuestionType() + "," + q.getStudyParticipantCrfSchedule().getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant().getDisplayName());
//            }

        }
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}