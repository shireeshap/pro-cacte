class UpdateFormSubmissionMode extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
                 dropColumn("STUDY_PARTICIPANT_CRFS", 'form_submission_mode');
                 addColumn("sp_crf_schedules", 'form_submission_mode', 'string', nullable: true);
              }

   void down() {
		     dropColumn("sp_crf_schedules", 'form_submission_mode')
    }

}