class CreateStudyCRFs extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    createTable("STUDY_CRFS") {t ->
      t.addVersionColumn()
      t.addColumn('study_id', 'integer', nullable: false)
      t.addColumn('crf_id', 'integer', nullable: false)
    }
    execute('ALTER TABLE STUDY_CRFS ADD CONSTRAINT fk_study_crf_study FOREIGN KEY (study_id) REFERENCES STUDIES')
    execute('ALTER TABLE STUDY_CRFS ADD CONSTRAINT fk_study_crf_crf FOREIGN KEY (crf_id) REFERENCES CRFS')
  }

  void down() {
    dropTable("STUDY_CRFS")
  }
}