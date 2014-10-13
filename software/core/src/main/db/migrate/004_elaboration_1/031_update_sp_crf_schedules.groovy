class UpdateStudyParticipantCRFSchedulesFilePath extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
             addColumn("sp_crf_schedules", 'file_path', 'string', nullable: true);
              }

   void down() {
		     dropColumn("sp_crf_schedules", 'file_path')
      }

}
