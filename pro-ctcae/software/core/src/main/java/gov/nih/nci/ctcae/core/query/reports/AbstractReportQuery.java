package gov.nih.nci.ctcae.core.query.reports;

import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.Arm;
import gov.nih.nci.ctcae.core.query.AbstractQuery;

import java.util.Date;
import java.util.Collection;
import java.util.List;

//
/**
 * @author Harsh Agarwal
 * @created June 26, 2009
 */
public abstract class AbstractReportQuery extends AbstractQuery {

    protected AbstractReportQuery(String query) {
        super(query);
        andWhere("spci.studyParticipantCrfSchedule.status =:status");
        setParameter("status", CrfStatus.COMPLETED);
        andWhere("spci.studyParticipantCrfSchedule.baseline =:baseline");
        setParameter("baseline", false);
    }


    public void filterBySymptomId(final Integer id) {
        if (id != null) {
            andWhere("spci.proCtcValidValue.proCtcQuestion.proCtcTerm.id = :symptom");
            setParameter("symptom", id);
        }
    }

    public void filterBySymptom(final String term) {
        andWhere("spci.proCtcValidValue.proCtcQuestion.proCtcTerm.term = :term");
        setParameter("term", term);
    }

    public void filterByAttributes(final Collection<ProCtcQuestionType> attributes) {
        if (attributes != null && attributes.size() > 0) {
            andWhere("spci.proCtcValidValue.proCtcQuestion.proCtcQuestionType in ( :attributesList)");
            setParameterList("attributesList", attributes);
        }
    }

    public void filterBySingleAttribute(final ProCtcQuestionType attribute) {
        if (attribute != null) {
            andWhere("spci.proCtcValidValue.proCtcQuestion.proCtcQuestionType =  :attribute");
            setParameter("attribute", attribute);
        }
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

    public void filterByStudySite(Integer id) {
        if (id != null) {
            andWhere("spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.studySite.id=:studySiteId");
            setParameter("studySiteId", id);
        }
    }

    public void filterByParticipantId(Integer id) {
        andWhere("spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.id=:participantId");
        setParameter("participantId", id);
    }

    public void filterByResponse(Integer response) {
        andWhere("spci.proCtcValidValue.displayOrder=:response");
        setParameter("response", response);
    }

    public void filterByPeriod(String periodType, int periodValue) {
        if ("week".equals(periodType.toLowerCase())) {
            andWhere("spci.studyParticipantCrfSchedule.weekInStudy = :periodValue");
        }
        if ("month".equals(periodType.toLowerCase())) {
            andWhere("spci.studyParticipantCrfSchedule.monthInStudy = :periodValue");
        }
        if ("cycle".equals(periodType.toLowerCase())) {
            andWhere("spci.studyParticipantCrfSchedule.cycleNumber = :periodValue");
        }
        setParameter("periodValue", periodValue);

    }

    public void filterByPeriod(String periodType, List<Integer> periodValues) {
        if ("week".equals(periodType.toLowerCase())) {
            andWhere("spci.studyParticipantCrfSchedule.weekInStudy in (:periodValues)");
        }
        if ("month".equals(periodType.toLowerCase())) {
            andWhere("spci.studyParticipantCrfSchedule.monthInStudy in (:periodValues)");
        }
        if ("cycle".equals(periodType.toLowerCase())) {
            andWhere("spci.studyParticipantCrfSchedule.cycleNumber in (:periodValues)");
        }
        setParameterList("periodValues", periodValues);

    }

    public void filterByArm(Arm arm) {
        if (arm != null) {
            andWhere("spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.arm.id=:armId");
            setParameter("armId", arm.getId());
        }
    }
}