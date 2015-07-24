class IvrsSchedules extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
      createTable("IVRS_SCHEDULES") {t ->
      t.addVersionColumn()
      t.addColumn('study_participant_id', 'integer', nullable: false)
      t.addColumn('spc_schedule_id', 'integer', nullable: false)
      t.addColumn('pref_call_time', 'timestamp', nullable: false)
      t.addColumn('next_call_time', 'timestamp', nullable: false)
      t.addColumn('call_status', 'string', nullable: false)
      t.addColumn('call_count', 'integer', nullable: false)
      t.addColumn('retry_period', 'integer', nullable: false)
    }
    execute('ALTER TABLE IVRS_SCHEDULES ADD CONSTRAINT fk_ivrs_study_part FOREIGN KEY (study_participant_id) REFERENCES STUDY_PARTICIPANT_ASSIGNMENTS')
    execute('ALTER TABLE IVRS_SCHEDULES ADD CONSTRAINT fk_ivrs_spc_sch FOREIGN KEY (spc_schedule_id) REFERENCES SP_CRF_SCHEDULES')
  }

  void down() {
    dropTable("IVRS_SCHEDULES")
  }
}