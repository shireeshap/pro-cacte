class CreateCrfItemDisplayRules extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		createTable("crf_item_display_rules") {t ->
			t.addColumn('crf_item_id', 'integer', nullable: false)
			t.addColumn('required_object_class', 'string', nullable: false)
			t.addColumn('required_object_id', 'integer', nullable: false)
		}
		execute('ALTER TABLE crf_item_display_rules ADD CONSTRAINT fk_cidr_crf_item FOREIGN KEY (crf_item_id) REFERENCES crf_items')

	}

	void down() {
		dropTable("crf_item_display_rules")
	}
}