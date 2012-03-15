class UpdateSPCRFSchedules extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
             addColumn("sp_crf_schedules", 'mark_delete', 'boolean', nullable: true);
             execute('update sp_crf_schedules set mark_delete = false');
              }

   void down() {
		     dropColumn("sp_crf_schedules", 'mark_delete')
      }

}
