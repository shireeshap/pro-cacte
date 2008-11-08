class CreateInvestigators extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    createTable("INVESTIGATORS") {t ->

      t.addVersionColumn()

      t.addColumn('email_address', 'string', nullable: true)
      t.addColumn('fax_number', 'string', nullable: true)
      t.addColumn('nci_identifier', 'string', nullable: false)
      t.addColumn('phone_number', 'string', nullable: true)
      t.addColumn('first_name', 'string', nullable: false)
      t.addColumn('last_name', 'string', nullable: false)
      t.addColumn('middle_name', 'string', nullable: true)
      t.addColumn('title', 'string', nullable: true)
      t.addColumn('address', 'string', nullable: true)

    }
  }

  void down() {
    dropTable("INVESTIGATORS")
  }
}