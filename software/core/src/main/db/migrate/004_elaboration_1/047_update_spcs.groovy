class UpdateStudyParticipantCRFSchedules extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
             addColumn("sp_crf_schedules", 'verbatim', 'string', nullable: true);
              }

   void down() {
		     dropColumn("sp_crf_schedules", 'verbatim')
      }

}
