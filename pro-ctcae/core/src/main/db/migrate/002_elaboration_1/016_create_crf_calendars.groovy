class CreateCrfCalendars extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		createTable("crf_calendars") {t ->
			t.addColumn('crf_id', 'integer', nullable: false)
			t.addColumn('repeat_every_unit', 'string', nullable: true)
			t.addColumn('repeat_every_amount', 'string', nullable: true)
			t.addColumn('due_date_unit', 'string', nullable: true)
			t.addColumn('due_date_amount', 'string', nullable: true)
			t.addColumn('repeat_until_unit', 'string', nullable: true)
			t.addColumn('repeat_until_amount', 'string', nullable: true)
			}
            execute('ALTER TABLE crf_calendars ADD CONSTRAINT fk_crf FOREIGN KEY (crf_id) REFERENCES CRFS(id)')

 	}
 		void down() {
		dropTable("crf_calendars")
	}
}
