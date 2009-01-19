class CreateProCtc extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		createTable("PRO_CTC_TERMS") {t ->
			t.addColumn('term', 'string', nullable: false)
			t.addColumn('ctc_term_id', 'integer', nullable: false)
		}
		execute('ALTER TABLE PRO_CTC_TERMS ADD CONSTRAINT fk_pct_ctc_term FOREIGN KEY (ctc_term_id) REFERENCES CTC_TERMS')
		}

	void down() {
		dropTable("PRO_CTC_TERMS")
	}
}