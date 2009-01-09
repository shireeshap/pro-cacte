class AlterCrfItemsDisplayRules extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		execute("delete from crf_item_display_rules")
		renameTable('crf_item_display_rules', 'crf_page_item_display_rules')

		dropColumn('crf_page_item_display_rules', 'required_object_class')
		dropColumn('crf_page_item_display_rules', 'required_object_id')

		addColumn("crf_page_item_display_rules", "pro_ctc_valid_value_id", "integer", nullable: false)
		execute('ALTER TABLE crf_page_item_display_rules ADD CONSTRAINT fk_pro_ctc_value_id FOREIGN KEY (pro_ctc_valid_value_id) REFERENCES pro_ctc_valid_values')


	}

	void down() {

	}
}