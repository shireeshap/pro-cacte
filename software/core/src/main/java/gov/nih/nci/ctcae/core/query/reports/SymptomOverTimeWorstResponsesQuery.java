package gov.nih.nci.ctcae.core.query.reports;

public class SymptomOverTimeWorstResponsesQuery extends AbstractReportQuery {


    public SymptomOverTimeWorstResponsesQuery(String group) {
        super(getQueryForGroup(group));
        if (group.equals("cycle")) {
            andWhere("spci.studyParticipantCrfSchedule.cycleNumber > :cycleNumber");
            setParameter("cycleNumber", 0);
        }
    }

    private static String getQueryForGroup(String group) {
        StringBuffer query = new StringBuffer();
        String append = "SELECT max(spci.proCtcValidValue.displayOrder), spci.crfPageItem.proCtcQuestion.proCtcQuestionType, spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.id , ";
        String selectGroup = "'Week ' || spci.studyParticipantCrfSchedule.weekInStudy ";
        String middle = "from StudyParticipantCrfItem spci group by spci.crfPageItem.proCtcQuestion.proCtcQuestionType,spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.id, ";
        String groupBy = "spci.studyParticipantCrfSchedule.weekInStudy ";
        String orderBy = "order by spci.crfPageItem.proCtcQuestion.proCtcQuestionType, ";
        String orderBy2 = "spci.studyParticipantCrfSchedule.weekInStudy ";

        if (group.equals("month")) {
            selectGroup = "'Month ' || spci.studyParticipantCrfSchedule.monthInStudy ";
            groupBy = "spci.studyParticipantCrfSchedule.monthInStudy ";
            orderBy2 = "spci.studyParticipantCrfSchedule.monthInStudy ";
        }
        if (group.equals("cycle")) {
            selectGroup = "'Cycle ' || spci.studyParticipantCrfSchedule.cycleNumber ";
            groupBy = "spci.studyParticipantCrfSchedule.cycleNumber ";
            orderBy2 = "spci.studyParticipantCrfSchedule.cycleNumber ";
        }

        query.append(append).append(selectGroup).append(middle).append(groupBy).append(orderBy).append(orderBy2);
        return query.toString();
    }

}