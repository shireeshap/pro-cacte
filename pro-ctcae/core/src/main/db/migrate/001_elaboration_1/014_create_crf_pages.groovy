class CreateCrfPAges extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		createTable("CRF_PAGES") {t ->
			t.addVersionColumn()
			t.addColumn('crf_id', 'integer', nullable: false)
			t.addColumn('description', 'string', nullable: true)
			t.addColumn('page_number', 'integer', nullable: false)

		}
		execute('ALTER TABLE CRF_PAGES ADD CONSTRAINT fk_crf_pages_crf FOREIGN KEY (crf_id) REFERENCES CRFS')

	}

	void down() {
		dropTable("CRF_PAGES")
	}
}