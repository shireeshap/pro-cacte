class CreateStudyPartcipantCrfScheduleNotifications extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
      createTable("sp_crf_schedule_notif") {t ->
         t.addVersionColumn()
         t.addColumn('spc_schedule_id', 'integer', nullable: false)
         t.addColumn('status', 'string', nullable: false)
         t.addColumn('is_mail_sent', 'boolean', defaultValue: false)
         t.addColumn('creation_date', 'timestamp', nullable: false)
         t.addColumn('completion_date', 'timestamp', nullable:true)
        }

         execute('ALTER TABLE sp_crf_schedule_notif ADD CONSTRAINT fk_spc_sch FOREIGN KEY (spc_schedule_id) REFERENCES SP_CRF_SCHEDULES')

    }

    void down() {
      dropTable("sp_crf_schedule_notif")
      }


}