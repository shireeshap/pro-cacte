class AddVersionToStudyParticipantCrfSchedule extends edu.northwestern.bioinformatics.bering.Migration {

     void up() {
		 addColumn("sp_crf_schedules", 'version', 'integer', nullable: false, defaultValue: 0);
     }
    
      void down() {
      }
}