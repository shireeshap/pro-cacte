class CreateOrganizations extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		createTable("ORGANIZATIONS") {t ->
			t.addVersionColumn()
			t.addColumn('nci_institute_code', 'string', nullable: false)
			t.addColumn('name', 'string', nullable: false)
		}
		execute('CREATE INDEX organizations_namecode_idx1 ON ORGANIZATIONS (name)')
		execute('CREATE INDEX organizations_namecode_idx2 ON ORGANIZATIONS (nci_institute_code)')

	}
	void down() {
		dropTable("ORGANIZATIONS")
	}
}