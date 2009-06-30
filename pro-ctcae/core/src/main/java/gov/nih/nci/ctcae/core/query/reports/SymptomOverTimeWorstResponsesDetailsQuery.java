package gov.nih.nci.ctcae.core.query.reports;
public class SymptomOverTimeWorstResponsesDetailsQuery extends SymptomOverTimeAllResponsesDetailsQuery {

    protected static String queryString = "SELECT max(spci.proCtcValidValue.displayOrder), " +
            "spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.id " +
            "from StudyParticipantCrfItem spci group by spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.id ";

    public SymptomOverTimeWorstResponsesDetailsQuery(Integer column, String group) {
        super(queryString,column, group);
    }
}