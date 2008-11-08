class CreateProCtc extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    createTable("PRO_CTC") {t ->
      t.addColumn('pro_ctc_version', 'string', nullable: false)
      t.addColumn('release_date', 'timestamp', nullable: false)
    }
    execute("ALTER TABLE PRO_CTC ADD CONSTRAINT un_pro_ctc_ver UNIQUE (pro_ctc_version)")
    insert("pro_ctc", [pro_ctc_version: '1.0', release_date: '10/15/2008'])
  }

  void down() {
    dropTable("PRO_CTC")
  }
}