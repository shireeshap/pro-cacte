class StudyParticipantCrf extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    createTable("STUDY_PARTICIPANT_CRFS") {t ->
      t.addVersionColumn()
      t.addColumn('crf_id', 'integer', nullable: false)
      t.addColumn('study_participant_id', 'integer', nullable: false)
      t.addColumn('start_date', 'date', nullable: true)
    }
    execute('ALTER TABLE STUDY_PARTICIPANT_CRFS ADD CONSTRAINT fk_spc_study_crf FOREIGN KEY (crf_id) REFERENCES CRFS')
    execute('ALTER TABLE STUDY_PARTICIPANT_CRFS ADD CONSTRAINT fk_spc_participant FOREIGN KEY (study_participant_id) REFERENCES STUDY_PARTICIPANT_ASSIGNMENTS')
  }

  void down() {
    dropTable("STUDY_CRF")
  }
}
