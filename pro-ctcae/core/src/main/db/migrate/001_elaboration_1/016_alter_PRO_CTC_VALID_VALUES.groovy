class DropProCtcTermValidValue extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    execute("delete from PRO_CTC_VALID_VALUES")
    addColumn('pro_ctc_valid_values', 'pro_ctc_term_id', 'integer', nullable: false);
    execute("ALTER TABLE PRO_CTC_VALID_VALUES DROP CONSTRAINT un_valid_value")
    execute('ALTER TABLE PRO_CTC_VALID_VALUES ADD CONSTRAINT fk_valid_val_term FOREIGN KEY (pro_ctc_term_id) REFERENCES PRO_CTC_TERMS')

  }

  void down() {
    dropTable("PRO_CTC_TERM_VALID_VALUES")
  }
}