class CreateProtocols extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    createTable("studies") {t ->
      t.addVersionColumn()
      t.addColumn("short_title", "string", nullable: false)
      t.addColumn("long_title", "string", nullable: true)
      t.addColumn("description", "string", nullable: true)
      t.addColumn("assigned_identifier", "string", nullable: false)

    }
	execute('CREATE INDEX studies_short_title_idx ON studies (short_title)')
	execute('CREATE INDEX studies_identifier_idx ON studies (assigned_identifier)')

  }

  void down() {
    dropTable("studies")
  }
}