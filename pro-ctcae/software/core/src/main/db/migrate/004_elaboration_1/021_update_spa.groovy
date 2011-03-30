class UpdateStudyParticipantAssignment extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
             addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'web_language', 'string', nullable: true);
             addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'ivrs_language', 'string', nullable: true);
              }

   void down() {
		     dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'ivrs_language')
		     dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'web_language')
      }

}