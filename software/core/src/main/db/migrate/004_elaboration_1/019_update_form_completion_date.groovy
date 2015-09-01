class UpdateFormCompletionDate extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
                 addColumn("sp_crf_schedules", 'form_completion_date', 'date', nullable: true);
              }

   void down() {
		     dropColumn("sp_crf_schedules", 'form_completion_date')
    }
}