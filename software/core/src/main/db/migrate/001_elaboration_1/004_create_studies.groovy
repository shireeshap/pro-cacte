import edu.northwestern.bioinformatics.bering.Migration

class CreateStudies extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    createTable("STUDIES") {t ->
      t.addVersionColumn()
      t.addColumn("short_title", "string", nullable: false)
      t.addColumn("long_title", "string", nullable: true)
      t.addColumn("description", "string", nullable: true)
      t.addColumn("assigned_identifier", "string", nullable: false)

    }
    execute('CREATE INDEX studies_short_title_idx ON studies (short_title)')
    execute('CREATE INDEX studies_identifier_idx ON studies (assigned_identifier)')

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
    dropTable("STUDIES")
  }
}