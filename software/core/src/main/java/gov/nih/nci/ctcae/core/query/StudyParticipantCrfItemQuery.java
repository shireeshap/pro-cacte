package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.query.reports.AbstractReportQuery;

public class StudyParticipantCrfItemQuery extends AbstractReportQuery {
    private static String queryString = "SELECT spci " +
            "from StudyParticipantCrfItem spci order by  spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.lastName, spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.firstName, " +
            "spci.studyParticipantCrfSchedule.startDate";

    public StudyParticipantCrfItemQuery() {
        super(queryString);
    }
}