package gov.nih.nci.ctcae.core.query.reports;

public class SymptomOverTimeAllResponsesQuery extends AbstractReportQuery {


    public SymptomOverTimeAllResponsesQuery(String group) {
        super(getQueryForGroup(group));
    }

    private static String getQueryForGroup(String group) {
        StringBuffer query = new StringBuffer();
        String append = "SELECT avg(spci.proCtcValidValue.displayOrder), spci.crfPageItem.proCtcQuestion.proCtcQuestionType, ";
        String selectGroup = "spci.studyParticipantCrfSchedule.weekInYear ";
        String middle = "from StudyParticipantCrfItem spci group by spci.proCtcValidValue.displayOrder,spci.crfPageItem.proCtcQuestion.proCtcQuestionType, ";
        String groupBy = "spci.studyParticipantCrfSchedule.weekInYear ";
        String orderBy = "order by spci.proCtcValidValue.displayOrder ";

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