class UpdateStudyParticipantAssignment extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
             addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'language', 'string', nullable: true);
              }

   void down() {
		     dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'language')
      }

}