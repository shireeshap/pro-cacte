package gov.nih.nci.ctcae.core.query.reports;

import org.apache.commons.lang.StringUtils;

public class SymptomOverTimeAllResponsesDetailsQuery extends AbstractReportQuery {

    protected static String queryString = "SELECT spci from StudyParticipantCrfItem spci order by spci.studyParticipantCrfSchedule.startDate";

    public SymptomOverTimeAllResponsesDetailsQuery(String column, String group) {
        super(queryString); 
        filterByColumn(column,group);
    }

    public SymptomOverTimeAllResponsesDetailsQuery(String queryString, String column, String group) {
        super(queryString);
        filterByColumn(column,group);
    }

    private void filterByColumn(String column, String group) {
        if ("week".equals(group)) {
            andWhere("'" + StringUtils.capitalize(group) + " ' || spci.studyParticipantCrfSchedule.weekInYear = :col");
        }
        if ("month".equals(group)) {
            andWhere("'" + StringUtils.capitalize(group) + " ' || spci.studyParticipantCrfSchedule.monthInYear = :col");
        }
        if ("cycle".equals(group)) {
            andWhere("'" + StringUtils.capitalize(group) + " ' || spci.studyParticipantCrfSchedule.cycleNumber = :col");
        }
        setParameter("col", column);
    }
}