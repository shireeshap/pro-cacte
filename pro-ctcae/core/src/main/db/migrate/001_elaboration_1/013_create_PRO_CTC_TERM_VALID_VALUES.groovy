class CreateProCtcTermValidValue extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    createTable("PRO_CTC_TERM_VALID_VALUES") {t ->
      t.setIncludePrimaryKey(false)
      t.addColumn('pro_ctc_term_id', 'integer', nullable: false)
      t.addColumn('valid_value_id', 'integer', nullable: false)
    }
    execute('ALTER TABLE PRO_CTC_TERM_VALID_VALUES ADD CONSTRAINT fk_pro_ctc_term FOREIGN KEY (pro_ctc_term_id) REFERENCES PRO_CTC_TERMS')
    execute('ALTER TABLE PRO_CTC_TERM_VALID_VALUES ADD CONSTRAINT fk_pro_ctc_val FOREIGN KEY (valid_value_id) REFERENCES PRO_CTC_VALID_VALUES')
    execute("ALTER TABLE PRO_CTC_TERM_VALID_VALUES ADD CONSTRAINT un_term_value UNIQUE (pro_ctc_term_id,valid_value_id )")
  }

  void down() {
    dropTable("PRO_CTC_TERM_VALID_VALUES")
  }
}