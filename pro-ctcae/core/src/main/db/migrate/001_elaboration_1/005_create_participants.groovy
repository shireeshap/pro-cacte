import edu.northwestern.bioinformatics.bering.Migration

class CreateParticipants extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    createTable("PARTICIPANTS") {t ->
      t.addVersionColumn()

      t.addColumn('birth_date', 'date', nullable: true)
      t.addColumn('ethnicity', 'string', nullable: true)
      t.addColumn('gender', 'string', nullable: true)
      t.addColumn('maiden_name', 'string', nullable: true)
      t.addColumn('race', 'string', nullable: true)
      t.addColumn('first_name', 'string', nullable: false)
      t.addColumn('last_name', 'string', nullable: false)
      t.addColumn('middle_name', 'string', nullable: true)
      t.addColumn('title', 'string', nullable: true)
      t.addColumn('address', 'string', nullable: true)
      t.addColumn('mrn_identifier', 'string', nullable: false);
    }
    execute('CREATE INDEX participants_flname_idx ON participants (first_name)')
    execute('CREATE INDEX participants_flname_idx2 ON participants (last_name)')

    execute('CREATE INDEX participants_eg_idx1 ON participants (ethnicity)')
    execute('CREATE INDEX participants_eg_idx2 ON participants (gender)')

    createTable("STUDY_PARTICIPANT_ASSIGNMENTS") {t ->
      t.addVersionColumn()
      t.addColumn('study_site_id', 'integer', nullable: false)
      t.addColumn('participant_id', 'integer', nullable: false)
      t.addColumn('study_participant_identifier', 'string', nullable: false);
    }
    execute('ALTER TABLE STUDY_PARTICIPANT_ASSIGNMENTS ADD CONSTRAINT fk_spa_study_site FOREIGN KEY (study_site_id) REFERENCES STUDY_ORGANIZATIONS')
    execute('ALTER TABLE STUDY_PARTICIPANT_ASSIGNMENTS ADD CONSTRAINT fk_spa_participant FOREIGN KEY (participant_id) REFERENCES PARTICIPANTS')

  }

  void down() {
    dropTable("STUDY_PARTICIPANT_ASSIGNMENTS")
    dropTable("PARTICIPANTS")
  }

}