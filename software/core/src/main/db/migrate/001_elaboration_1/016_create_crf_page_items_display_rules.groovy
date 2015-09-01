class CreateCrfItemDisplayRules extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		createTable("CRF_PAGE_ITEM_DISPLAY_RULES") {t ->
			t.addColumn('crf_item_id', 'integer', nullable: false)
			t.addColumn('pro_ctc_valid_value_id', 'integer', nullable: false)
		}
		execute('ALTER TABLE CRF_PAGE_ITEM_DISPLAY_RULES ADD CONSTRAINT fk_pro_ctc_value_id FOREIGN KEY (pro_ctc_valid_value_id) REFERENCES PRO_CTC_VALID_VALUES')
		execute('ALTER TABLE CRF_PAGE_ITEM_DISPLAY_RULES ADD CONSTRAINT fk_cidr_crf_item FOREIGN KEY (crf_item_id) REFERENCES CRF_PAGE_ITEMS')

	}

	void down() {
		dropTable("CRF_PAGE_ITEM_DISPLAY_RULES")
	}
}