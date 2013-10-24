class CreateProctcQuestionDisplayRules extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		createTable("QUESTION_DISPLAY_RULES") {t ->
			t.addColumn('pro_ctc_valid_value_id', 'integer', nullable: false)
			t.addColumn('pro_ctc_question_id', 'integer', nullable: false)
		}
		execute('ALTER TABLE QUESTION_DISPLAY_RULES ADD CONSTRAINT fk_pro_ctc_valid_val_id FOREIGN KEY (pro_ctc_valid_value_id) REFERENCES PRO_CTC_VALID_VALUES')
        execute('ALTER TABLE QUESTION_DISPLAY_RULES ADD CONSTRAINT fk_pro_ctc_question_id FOREIGN KEY (pro_ctc_question_id) REFERENCES PRO_CTC_QUESTIONS')
        
	}

	void down() {
		dropTable("QUESTION_DISPLAY_RULES")
	}
}