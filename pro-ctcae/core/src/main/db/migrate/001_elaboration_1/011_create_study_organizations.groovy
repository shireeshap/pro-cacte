class CreateStudyOrganizations extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    createTable('study_organizations') {t ->
      t.addColumn('organization_id', 'integer', nullable: false)
      t.addColumn('study_id', 'integer', nullable: false)
      t.addColumn('type', 'string', nullable: false);

      t.addVersionColumn()
    }
	execute('ALTER TABLE study_organizations ADD CONSTRAINT fk_so_study FOREIGN KEY (study_id) REFERENCES studies (id)    ')
	execute('ALTER TABLE study_organizations ADD CONSTRAINT fk_so_organization FOREIGN KEY (organization_id) REFERENCES organizations (id)  ')

  }

  void down() {
    dropTable('study_organizations')
  }
}