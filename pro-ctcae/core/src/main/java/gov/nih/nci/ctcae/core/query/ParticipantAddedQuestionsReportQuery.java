package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.CrfStatus;

import java.util.Set;
import java.util.Date;

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
    private static String queryString = "SELECT spcaq.proCtcQuestion.proCtcTerm.term, count(distinct spcaq.studyParticipantCrf.studyParticipantAssignment.participant.id)  from StudyParticipantCrfAddedQuestion spcaq group by spcaq.proCtcQuestion.proCtcTerm.term order by spcaq.proCtcQuestion.proCtcTerm.term";

    public ParticipantAddedQuestionsReportQuery(String query) {
        super(query);
    }

    public ParticipantAddedQuestionsReportQuery() {
        this(queryString);
    }

    public void filterByCrf(Integer crfId) {
        andWhere("spcaq.studyParticipantCrf.crf.id=:crfId");
        setParameter("crfId", crfId);
    }

    public void filterByStudySite(Integer id) {
        andWhere("spcaq.studyParticipantCrf.studyParticipantAssignment.studySite.id=:studySiteId");
        setParameter("studySiteId", id);
    }

    public void filterBySymptom(String symptom) {
        andWhere("spcaq.proCtcQuestion.proCtcTerm.term=:symptom");
        setParameter("symptom", symptom);
    }
}