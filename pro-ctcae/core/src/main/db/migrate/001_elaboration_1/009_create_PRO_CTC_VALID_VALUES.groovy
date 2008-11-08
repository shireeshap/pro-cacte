class CreateProCtcValidValue extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    createTable("PRO_CTC_VALID_VALUES") {t ->
      t.addColumn('value', 'string', nullable: false)
    }
    execute("ALTER TABLE PRO_CTC_VALID_VALUES ADD CONSTRAINT un_valid_value UNIQUE (value)")
    insert('PRO_CTC_VALID_VALUES', [value: 'Low'])
    insert('PRO_CTC_VALID_VALUES', [value: 'High'])
    insert('PRO_CTC_VALID_VALUES', [value: 'Severe'])
    insert('PRO_CTC_VALID_VALUES', [value: 'Very Low'])
    insert('PRO_CTC_VALID_VALUES', [value: 'Yes'])
    insert('PRO_CTC_VALID_VALUES', [value: 'No'])
  }

  void down() {
    dropTable("PRO_CTC_VALID_VALUES")
  }
}