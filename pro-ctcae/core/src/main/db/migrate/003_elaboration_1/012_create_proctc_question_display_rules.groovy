class CreateProctcQuestionDisplayRules extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		createTable("proctc_question_display_rules") {t ->
			t.addColumn('proctc_valid_value_id', 'integer', nullable: false)
			t.addColumn('proctc_question_id', 'string', nullable: false)
		}
		execute('ALTER TABLE proctc_question_display_rules ADD CONSTRAINT fk_proctc_valid_value_id FOREIGN KEY (proctc_valid_value_id) REFERENCES pro_ctc_valid_values')

	}

	void down() {
		dropTable("proctc_question_display_rules")
	}
}