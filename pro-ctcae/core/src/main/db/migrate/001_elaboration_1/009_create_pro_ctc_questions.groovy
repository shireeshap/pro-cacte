class CreateProCtc extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		createTable("PRO_CTC_QUESTIONS") {t ->
			t.addColumn('question_text', 'string', nullable: false)
			t.addColumn('pro_ctc_term_id', 'integer', nullable: false)
			t.addColumn('question_type', 'string', nullable: true)
			t.addColumn('display_order', 'integer', nullable: true)
	}
		execute('ALTER TABLE PRO_CTC_QUESTIONS ADD CONSTRAINT fk_pro_ctc_ques_term FOREIGN KEY (pro_ctc_term_id) REFERENCES PRO_CTC_TERMS')
	}

	void down() {
		dropTable("PRO_CTC_QUESTIONS")
	}
}