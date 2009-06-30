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
        String selectGroup = "'Week ' || spci.studyParticipantCrfSchedule.weekInYear ";
        String middle = "from StudyParticipantCrfItem spci group by spci.proCtcValidValue.displayOrder,spci.crfPageItem.proCtcQuestion.proCtcQuestionType,spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.id, ";
        String groupBy = "spci.studyParticipantCrfSchedule.weekInYear ";
        String orderBy = "order by spci.studyParticipantCrfSchedule.weekInYear, ";
        String orderBy2 = "spci.crfPageItem.proCtcQuestion.proCtcQuestionType ";

        if (group.equals("month")) {
            selectGroup = "'Month ' || spci.studyParticipantCrfSchedule.monthInYear ";
            groupBy = "spci.studyParticipantCrfSchedule.monthInYear ";
            orderBy = "order by spci.studyParticipantCrfSchedule.monthInYear, ";
        }
        if (group.equals("cycle")) {
            selectGroup = "'Cycle ' || spci.studyParticipantCrfSchedule.cycleNumber ";
            groupBy = "spci.studyParticipantCrfSchedule.cycleNumber ";
            orderBy = "order by spci.studyParticipantCrfSchedule.cycleNumber, ";
        }

        query.append(append).append(selectGroup).append(middle).append(groupBy).append(orderBy).append(orderBy2);
        return query.toString();
    }

}