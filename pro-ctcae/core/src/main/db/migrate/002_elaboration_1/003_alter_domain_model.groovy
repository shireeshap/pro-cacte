class AlterDomainModel extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {


		if (databaseMatches('oracle')) {
			execute('drop table PRO_CTC_TERMS CASCADE CONSTRAINTS')
			execute('drop table CTC_TERMS CASCADE CONSTRAINTS')

			execute('drop SEQUENCE seq_PRO_CTC_TERMS_id')
			execute('drop SEQUENCE seq_CTC_TERMS_id')


		} else {
			dropTable("PRO_CTC_TERMS")
			dropTable("CTC_TERMS")

		}


		createTable("PRO_CTC_TERMS") {t ->
			t.addColumn('category_id', 'integer', nullable: false)
			t.addColumn('term', 'string', nullable: false)
			t.addColumn('select_ae', 'string', nullable: true)
			t.addColumn('ctep_term', 'string', nullable: true)
			t.addColumn('ctep_code', 'string', nullable: true)
			t.addColumn('pro_ctc_id', 'integer', nullable: false)
		}
		execute('ALTER TABLE PRO_CTC_TERMS ADD CONSTRAINT fk_pro_ctc_terms_cat FOREIGN KEY (category_id) REFERENCES CTC_CATEGORIES')
		execute('ALTER TABLE PRO_CTC_TERMS ADD CONSTRAINT fk_pro_ctc_terms_ver FOREIGN KEY (pro_ctc_id) REFERENCES PRO_CTC')

		createTable("PRO_CTC_QUESTIONS") {t ->
			t.addColumn('question_text', 'string', nullable: false)
			t.addColumn('pro_ctc_term_id', 'integer', nullable: false)
		}
		execute('ALTER TABLE PRO_CTC_QUESTIONS ADD CONSTRAINT fk_pro_ctc_ques_term FOREIGN KEY (pro_ctc_term_id) REFERENCES PRO_CTC_TERMS')

		execute('delete from pro_ctc_valid_values')
		addColumn('pro_ctc_valid_values', 'pro_ctc_question_id', 'integer', nullable: false)
		execute('ALTER TABLE PRO_CTC_VALID_VALUES ADD CONSTRAINT fk_valid_val_ques FOREIGN KEY (pro_ctc_question_id) REFERENCES PRO_CTC_QUESTIONS')

		execute('delete from crf_items')
		addColumn('crf_items', 'pro_ctc_question_id', 'integer', nullable: false)
		execute('ALTER TABLE CRF_ITEMS ADD CONSTRAINT fk_crf_items_ques FOREIGN KEY (pro_ctc_question_id) REFERENCES PRO_CTC_QUESTIONS')
	}

	void down() {
		dropTable("CTC_TERMS")
	}
}