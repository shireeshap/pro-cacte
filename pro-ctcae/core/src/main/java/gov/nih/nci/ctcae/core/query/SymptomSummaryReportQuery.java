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
public class SymptomSummaryReportQuery extends AbstractQuery {

    /**
     * The query string.
     */
    private static String queryString = "SELECT count(spci.proCtcValidValue.value) , spci.proCtcValidValue.value from StudyParticipantCrfItem spci group by spci.proCtcValidValue.value  order by spci.proCtcValidValue.value ";

    public SymptomSummaryReportQuery() {
        super(queryString);
        andWhere("spci.studyParticipantCrfSchedule.status =:status");
        setParameter("status", CrfStatus.COMPLETED);
    }

    public void filterBySymptomId(final Integer id) {
        andWhere("spci.proCtcValidValue.proCtcQuestion.proCtcTerm.id = :symptom");
        setParameter("symptom", id);
    }

    public void filterByAttribute(final ProCtcQuestionType attribute) {
        andWhere("spci.proCtcValidValue.proCtcQuestion.proCtcQuestionType = :attribute");
        setParameter("attribute", attribute);
    }

    public void filterByResponse(Set<String> responses) {
        andWhere("spci.proCtcValidValue.value in (:responses)");
        setParameterList("responses", responses);

    }

    public void filterByParticipantGender(String gender) {
        andWhere("spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.gender = :gender");
        setParameter("gender", gender);
    }

    public void filterByScheduleStartDate(Date startDate, Date endDate) {
        andWhere("spci.studyParticipantCrfSchedule.startDate between :startDate and :endDate");
        setParameter("startDate", startDate);
        setParameter("endDate", endDate);
    }

    public void filterByCrf(Integer crfId) {
        andWhere("spci.studyParticipantCrfSchedule.studyParticipantCrf.crf.id=:crfId");
        setParameter("crfId", crfId);
    }

    public void filterByStudySite(Integer id){
        andWhere("spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.studySite.id=:studySiteId");
        setParameter("studySiteId", id);
    }
}