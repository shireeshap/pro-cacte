class CreateStudyParticipantCrfSchedules extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    createTable("SP_CRF_SCHEDULES") {t ->
      t.addColumn('study_participant_crf_id', 'integer', nullable: false)
      t.addColumn('start_date', 'date', nullable: true)
      t.addColumn('due_date', 'date', nullable: true)
      t.addColumn('status', 'string', nullable: true)
    }
    execute('ALTER TABLE SP_CRF_SCHEDULES ADD CONSTRAINT fk_spcs_spc FOREIGN KEY (study_participant_crf_id) REFERENCES STUDY_PARTICIPANT_CRFS')

  }

  void down() {
    dropTable("SP_CRF_SCHEDULES")
  }
}





