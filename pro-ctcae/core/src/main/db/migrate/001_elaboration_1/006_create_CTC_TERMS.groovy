class CreateCtcTerm extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		createTable("CTC_CATEGORIES") {t ->
			t.addColumn('version_id', 'integer', nullable: false)
			t.addColumn('name', 'string', nullable: false)
		}

		createTable("CTC_TERMS") {t ->
			t.addColumn('category_id', 'integer', nullable: false)
			t.addColumn('term', 'string', nullable: false)
			t.addColumn('select_ae', 'string', nullable: true)
			t.addColumn('ctep_term', 'string', nullable: true)
			t.addColumn('ctep_code', 'string', nullable: true)
		}

		if (databaseMatches('oracle')) {
			execute('ALTER TABLE CTC_TERMS add  other_required integer  DEFAULT \'0\' not null')

		} else {
			addColumn("CTC_TERMS", "other_required", 'boolean', nullable: false)
			execute('ALTER TABLE CTC_TERMS ALTER COLUMN other_required SET DEFAULT false')


		}
		execute('ALTER TABLE CTC_TERMS ADD CONSTRAINT fk_ctc_terms_cat FOREIGN KEY (category_id) REFERENCES CTC_CATEGORIES')
	}

	void down() {
		dropTable("CTC_TERMS")
	}
}