class UpdateStudyParticipantCRFItems extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
             addColumn("study_participant_crf_items", 'response_date', 'timestamp', nullable: true);
             addColumn("study_participant_crf_items", 'updated_by', 'string', nullable: true);
             addColumn("study_participant_crf_items", 'response_mode', 'string', nullable: true);
              }

   void down() {
		     dropColumn("study_participant_crf_items", 'response_date')
		     dropColumn("study_participant_crf_items", 'updated_by')
             dropColumn("study_participant_crf_items", 'response_mode')
      }

}