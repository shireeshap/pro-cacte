class CreateProCtcTerm extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    createTable("PRO_CTC_TERMS") {t ->
      t.addColumn('question_text', 'string', nullable: false)
      t.addColumn('pro_ctc_id', 'integer', nullable: false)
      t.addColumn('ctc_term_id', 'integer', nullable: false)
    }
    execute('ALTER TABLE PRO_CTC_TERMS ADD CONSTRAINT fk_pro_ctc_terms_ver FOREIGN KEY (pro_ctc_id) REFERENCES PRO_CTC')
    execute('ALTER TABLE PRO_CTC_TERMS ADD CONSTRAINT fk_pro_ctc_terms_ctc FOREIGN KEY (ctc_term_id) REFERENCES CTC_TERMS')
  }

  void down() {
    dropTable("PRO_CTC_TERMS")
  }
}