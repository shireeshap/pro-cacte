package gov.nih.nci.ctcae.core.query.reports;

public class SymptomOverTimeWorstResponsesQuery extends AbstractReportQuery {


    public SymptomOverTimeWorstResponsesQuery(String group) {
        super(getQueryForGroup(group));
    }

    private static String getQueryForGroup(String group) {
        StringBuffer query = new StringBuffer();
        String append = "SELECT max(spci.proCtcValidValue.displayOrder), spci.crfPageItem.proCtcQuestion.proCtcQuestionType, spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.id , ";
        String selectGroup = "spci.studyParticipantCrfSchedule.weekInYear ";
        String middle = "from StudyParticipantCrfItem spci group by spci.proCtcValidValue.displayOrder,spci.crfPageItem.proCtcQuestion.proCtcQuestionType,spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.id, ";
        String groupBy = "spci.studyParticipantCrfSchedule.weekInYear ";
        String orderBy = "order by max(spci.proCtcValidValue.displayOrder) ";

        if (group.equals("month")) {
            selectGroup = "spci.studyParticipantCrfSchedule.monthInYear ";
            groupBy = "spci.studyParticipantCrfSchedule.monthInYear ";
        }
        if (group.equals("cycle")) {
            selectGroup = "spci.studyParticipantCrfSchedule.cycleNumber ";
            groupBy = "spci.studyParticipantCrfSchedule.cycleNumber ";
        }

        query.append(append).append(selectGroup).append(middle).append(groupBy).append(orderBy);
        return query.toString();
    }

}