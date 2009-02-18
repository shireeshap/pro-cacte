class CreateCrfCalendars extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		createTable("crf_cycles") {t ->
			t.addColumn('crf_id', 'integer', nullable: false)
			t.addColumn('cycle_length', 'integer', nullable: false)
			t.addColumn('cycle_days', 'string', nullable: true)
			t.addColumn('repeat_times', 'integer', nullable: true)
			t.addColumn('cycle_length_unit', 'string', nullable: false)
			}


 	}
 		void down() {
		dropTable("crf_cycles")
	}
}
