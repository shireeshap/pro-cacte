package gov.nih.nci.ctcae.core.query.reports;

public class SymptomOverTimeAllResponsesQuery extends AbstractReportQuery {


    public SymptomOverTimeAllResponsesQuery(String group) {
        super(getQueryForGroup(group));
        if (group.equals("cycle")) {
            andWhere("spci.studyParticipantCrfSchedule.cycleNumber > :cycleNumber");
            setParameter("cycleNumber", 0);
        }
    }


    private static String getQueryForGroup(String group) {
        StringBuffer query = new StringBuffer();
        String append = "SELECT avg(spci.proCtcValidValue.displayOrder), spci.crfPageItem.proCtcQuestion.proCtcQuestionType, ";
        String selectGroup = "'Week ' || spci.studyParticipantCrfSchedule.weekInYear ";
        String middle = "from StudyParticipantCrfItem spci group by spci.crfPageItem.proCtcQuestion.proCtcQuestionType, ";
        String groupBy = "spci.studyParticipantCrfSchedule.weekInYear ";
        String orderBy = "order by spci.crfPageItem.proCtcQuestion.proCtcQuestionType, ";
        String orderBy2 = "spci.studyParticipantCrfSchedule.weekInYear ";

        if (group.equals("month")) {
            selectGroup = "'Month ' || spci.studyParticipantCrfSchedule.monthInYear ";
            groupBy = "spci.studyParticipantCrfSchedule.monthInYear ";
            orderBy2 = "spci.studyParticipantCrfSchedule.monthInYear ";
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