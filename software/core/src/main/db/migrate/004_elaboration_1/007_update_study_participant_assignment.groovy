class UpdateStudyParticipantAssignment extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
             addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'time', 'integer', nullable: true);
             addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'hour', 'string', nullable: true);
             addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'time_zone', 'string', nullable: true);
              }

   void down() {
		     dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'time')
		     dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'hour')
		     dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'time_zone')

    }

}