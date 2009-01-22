class CreateProctcQuestionDisplayRules extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    createTable("question_display_rules") {t ->
      t.addColumn('pro_ctc_valid_value_id', 'integer', nullable: false)
      t.addColumn('pro_ctc_question_id', 'integer', nullable: false)
    }
    execute('ALTER TABLE question_display_rules ADD CONSTRAINT fk_pro_ctc_valid_val_id FOREIGN KEY (pro_ctc_valid_value_id) REFERENCES pro_ctc_valid_values')

  }

  void down() {
    dropTable("question_display_rules")
  }
}