class CreateCrfItemDisplayRules extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		createTable("crf_page_item_display_rules") {t ->
			t.addColumn('crf_item_id', 'integer', nullable: false)
			t.addColumn('pro_ctc_valid_value_id', 'integer', nullable: false)
		}
		execute('ALTER TABLE crf_page_item_display_rules ADD CONSTRAINT fk_pro_ctc_value_id FOREIGN KEY (pro_ctc_valid_value_id) REFERENCES pro_ctc_valid_values')
		execute('ALTER TABLE crf_page_item_display_rules ADD CONSTRAINT fk_cidr_crf_item FOREIGN KEY (crf_item_id) REFERENCES crf_page_items')

	}

	void down() {
		dropTable("crf_page_item_display_rules")
	}
}