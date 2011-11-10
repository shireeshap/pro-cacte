class UpdateStudyParticipantCrf extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
             addColumn("STUDY_PARTICIPANT_CRFS", 'form_submission_mode', 'string', nullable: true);
              }

   void down() {
		     dropColumn("STUDY_PARTICIPANT_CRFS", 'string')
    }

}