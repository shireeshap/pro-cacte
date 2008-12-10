class CreateOrganizations extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		createTable("organizations") {t ->
			t.addVersionColumn()

			t.addColumn('nci_institute_code', 'string', nullable: false)
			t.addColumn('name', 'string', nullable: false)

		}
	}

	void down() {
		dropTable("organizations")
	}
}