class UpdateSPCRFScheduleAddedQuestions extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
             addColumn("sp_crf_sch_added_questions", 'response_date', 'timestamp', nullable: true);
             addColumn("sp_crf_sch_added_questions", 'updated_by', 'string', nullable: true);
             addColumn("sp_crf_sch_added_questions", 'response_mode', 'string', nullable: true);
              }

   void down() {
		     dropColumn("sp_crf_sch_added_questions", 'response_date')
		     dropColumn("sp_crf_sch_added_questions", 'updated_by')
             dropColumn("sp_crf_sch_added_questions", 'response_mode')
      }

}