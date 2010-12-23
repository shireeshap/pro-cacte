class CreateMeddraValidValue extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		createTable("MEDDRA_VALID_VALUES") {t ->
			t.addColumn('value', 'string', nullable: false)
			t.addColumn('meddra_question_id', 'integer', nullable: false)
			t.addColumn('display_order', 'integer', nullable: true)
		}
		execute('ALTER TABLE MEDDRA_VALID_VALUES ADD CONSTRAINT fk_med_valid_val_ques FOREIGN KEY (meddra_question_id) REFERENCES MEDDRA_QUESTIONS')
	}

	void down() {
		dropTable("MEDDRA_VALID_VALUES")
	}
}