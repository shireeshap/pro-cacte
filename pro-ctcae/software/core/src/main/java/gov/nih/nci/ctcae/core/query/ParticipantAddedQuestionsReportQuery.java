package gov.nih.nci.ctcae.core.query;

import java.util.List;

//
/**
 * The Class ProCtcQuestionQuery.
 *
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ParticipantAddedQuestionsReportQuery extends AbstractQuery {

    /**
     * The query string.
     */
    private static String queryString = "SELECT spcsaq.proCtcQuestion.proCtcTerm.proCtcTermVocab.termEnglish, count(distinct spcsaq.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.id)  " +
    		"from StudyParticipantCrfScheduleAddedQuestion spcsaq group by spcsaq.proCtcQuestion.proCtcTerm.proCtcTermVocab.termEnglish order by spcsaq.proCtcQuestion.proCtcTerm.proCtcTermVocab.termEnglish";

    public ParticipantAddedQuestionsReportQuery(String query) {
        super(query);
    }

    public ParticipantAddedQuestionsReportQuery() {
        this(queryString);
    }

    public void filterByCrf(Integer crfId) {
        andWhere("spcsaq.studyParticipantCrfSchedule.studyParticipantCrf.crf.id=:crfId");
        setParameter("crfId", crfId);
    }
    
    public void filterByCrfs(List<Integer> crfIds) {
        andWhere("spcsaq.studyParticipantCrfSchedule.studyParticipantCrf.crf.id in (:crfs)");
        setParameterList("crfs", crfIds);
    }

    public void filterByStudySite(Integer id) {
        andWhere("spcsaq.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.studySite.id=:studySiteId");
        setParameter("studySiteId", id);
    }

    public void filterBySymptom(String symptom) {
        andWhere("spcsaq.proCtcQuestion.proCtcTerm.proCtcTermVocab.termEnglish=:symptom");
        setParameter("symptom", symptom);
    }
}