class AddStudyParticipantAssignmentAccessColumn extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
         addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'live_access_timestamp', 'timestamp', nullable: true);
    }

    void down() {
	     dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'live_access_timestamp')
    }

}