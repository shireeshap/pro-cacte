class CreateMeddraQuestions extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    createTable("MEDDRA_QUESTIONS") {t ->
      t.addColumn('question_text', 'string', nullable: false)
      t.addColumn('meddra_llt_id', 'integer', nullable: false)
      t.addColumn('question_type', 'string', nullable: true)
      t.addColumn('display_order', 'integer', nullable: true)
    }
    execute('ALTER TABLE MEDDRA_QUESTIONS ADD CONSTRAINT fk_meddra_ques_term FOREIGN KEY (meddra_llt_id) REFERENCES meddra_llt')
  }

  void down() {
    dropTable("MEDDRA_QUESTIONS")
  }
}