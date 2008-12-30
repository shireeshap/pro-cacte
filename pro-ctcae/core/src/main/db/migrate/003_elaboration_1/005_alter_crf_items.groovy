class AlterCrfItemstoPageItem extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		execute('delete from study_participant_crf_items')
		execute('delete from crf_item_display_rules')
		execute('delete from crf_items')

		renameTable('crf_items','CRF_PAGE_ITEMS')


		dropColumn("CRF_PAGE_ITEMS","crf_id")
		addColumn("CRF_PAGE_ITEMS","crf_page_id","integer",nullable:false) 
		execute('ALTER TABLE CRF_PAGE_ITEMS ADD CONSTRAINT fk_crf_page_items_ques FOREIGN KEY (pro_ctc_question_id) REFERENCES PRO_CTC_QUESTIONS')
		execute("ALTER TABLE CRF_PAGE_ITEMS ADD CONSTRAINT un_crf_page_terms UNIQUE (crf_page_id,pro_ctc_question_id)")
		execute('ALTER TABLE CRF_PAGE_ITEMS ADD CONSTRAINT fk_crf_page_items_crf FOREIGN KEY (crf_page_id) REFERENCES CRF_PAGES')


	}

	void down() {

	}
}