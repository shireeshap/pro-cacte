class UpdateStudyParticipantAssignment extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
             addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'study_start_date', 'date', nullable: true);
              }

   void down() {
		     dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'study_start_date')    
    }

}