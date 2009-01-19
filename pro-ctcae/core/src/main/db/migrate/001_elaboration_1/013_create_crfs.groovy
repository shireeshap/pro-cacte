class CreateCRF extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    createTable("CRFS") {t ->
	t.addVersionColumn()
	t.addColumn('title', 'string', nullable: false)
	t.addColumn('description', 'string', nullable: true)
	t.addColumn('status', 'string', nullable: false)
	t.addColumn('crf_version', 'string', nullable: false)
	t.addColumn('effective_start_date', 'date', nullable: true);
	t.addColumn('effective_end_date', 'date', nullable: true);
	t.addColumn('next_version_id', 'integer', nullable: true);
	t.addColumn('parent_version_id', 'integer', nullable: true);
	t.addColumn('study_id', 'integer', nullable: false)
	t.addColumn('recall_period', 'string', nullable: false)
    }
	execute("ALTER TABLE CRFS ADD CONSTRAINT un_crfs_title UNIQUE (title, crf_version)")
	execute('ALTER TABLE CRFS ADD CONSTRAINT fk_crfs_crf_id FOREIGN KEY (next_version_id) REFERENCES CRFS')
    execute('ALTER TABLE CRFS ADD CONSTRAINT fk_prev_crfs_crf_id FOREIGN KEY (parent_version_id) REFERENCES CRFS')
	execute('ALTER TABLE CRFS ADD CONSTRAINT fk_crf_study FOREIGN KEY (study_id) REFERENCES STUDIES')
  }

  void down() {
    dropTable("CRFS")
  }
}