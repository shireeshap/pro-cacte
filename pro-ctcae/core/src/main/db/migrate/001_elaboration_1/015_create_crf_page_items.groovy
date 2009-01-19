class CreateCrfItem extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		createTable("crf_page_items") {t ->
			t.addColumn('display_order', 'integer', nullable: false)
			t.addColumn('response_required', 'boolean', nullable: true)
			t.addColumn('instructions', 'string', nullable: true)
			t.addColumn('allignment', 'string', nullable: true)
			t.addColumn("crf_page_id","integer",nullable:false) 
			t.addColumn("pro_ctc_question_id","integer",nullable:false) 
		}
		execute('ALTER TABLE crf_page_items ADD CONSTRAINT fk_crf_items_ques FOREIGN KEY (pro_ctc_question_id) REFERENCES PRO_CTC_QUESTIONS')
		execute("ALTER TABLE crf_page_items ADD CONSTRAINT un_crf_page_terms UNIQUE (crf_page_id,pro_ctc_question_id)")
		execute('ALTER TABLE crf_page_items ADD CONSTRAINT fk_crf_page_items_crf FOREIGN KEY (crf_page_id) REFERENCES CRF_PAGES')
	}

	void down() {
		dropTable("crf_page_items")
	}
}