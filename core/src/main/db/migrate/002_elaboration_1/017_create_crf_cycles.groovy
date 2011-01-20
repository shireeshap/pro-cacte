class CreateCrfCycles extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		createTable("crf_cycle_definitions") {t ->
			t.addColumn('crf_id', 'integer', nullable: false)
			t.addColumn('cycle_length', 'integer', nullable: false)
			t.addColumn('cycle_length_unit', 'string', nullable: false)
			t.addColumn('cycle_name', 'string', nullable: true)
			t.addColumn('repeat_times', 'string', nullable: false)
			t.addColumn('display_order', 'integer', nullable: false)
			}
        execute('ALTER TABLE CRF_CYCLE_DEFINITIONS ADD CONSTRAINT fk_definition_crf FOREIGN KEY (crf_id) REFERENCES CRFS')

		createTable("crf_cycles") {t ->
			t.addColumn('cycle_definition_id', 'integer', nullable: false)
			t.addColumn('cycle_days', 'string', nullable: true)
			t.addColumn('cycle_order', 'integer', nullable: false)
			}
        execute('ALTER TABLE CRF_CYCLES ADD CONSTRAINT fk_cycle_def FOREIGN KEY (cycle_definition_id) REFERENCES CRF_CYCLE_DEFINITIONS')


 	}
 		void down() {
		dropTable("crf_cycles")
	}
}
