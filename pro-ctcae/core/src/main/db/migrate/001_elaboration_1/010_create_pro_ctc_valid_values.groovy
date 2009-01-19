class CreateProCtcValidValue extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		createTable("PRO_CTC_VALID_VALUES") {t ->
			t.addColumn('value', 'string', nullable: false)
			t.addColumn('pro_ctc_question_id', 'integer', nullable: false)
		}
		execute('ALTER TABLE PRO_CTC_VALID_VALUES ADD CONSTRAINT fk_valid_val_ques FOREIGN KEY (pro_ctc_question_id) REFERENCES PRO_CTC_QUESTIONS')
	}

	void down() {
		dropTable("PRO_CTC_VALID_VALUES")
	}
}