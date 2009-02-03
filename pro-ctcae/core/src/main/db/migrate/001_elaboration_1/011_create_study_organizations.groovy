class CreateStudyOrganizations extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    createTable('STUDY_ORGANIZATIONS') {t ->
      t.addColumn('organization_id', 'integer', nullable: false)
      t.addColumn('study_id', 'integer', nullable: false)
      t.addColumn('type', 'string', nullable: false);

      t.addVersionColumn()
    }
	execute('ALTER TABLE STUDY_ORGANIZATIONS ADD CONSTRAINT fk_so_study FOREIGN KEY (study_id) REFERENCES STUDIES (id)    ')
	execute('ALTER TABLE STUDY_ORGANIZATIONS ADD CONSTRAINT fk_so_organization FOREIGN KEY (organization_id) REFERENCES ORGANIZATIONS (id)  ')

  }

  void down() {
    dropTable('STUDY_ORGANIZATIONS')
  }
}