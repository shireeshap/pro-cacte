import edu.northwestern.bioinformatics.bering.Migration

class CreateParticipants extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
      createTable("USERS") {t ->

        t.addVersionColumn()

        t.addColumn('password', 'string', nullable: true)
        t.addColumn('user_name', 'string', nullable: true)
        t.addColumn('account_non_expired', 'boolean', nullable: false)
        t.addColumn('account_non_locked', 'boolean', nullable: true)
        t.addColumn('credentials_non_expired', 'boolean', nullable: false)
        t.addColumn('enabled', 'boolean', nullable: false)

      }



      execute("ALTER TABLE USERS ADD CONSTRAINT un_user_name UNIQUE (user_name)")


      createTable("USER_ROLES") {t ->
        t.addVersionColumn()
        t.addColumn('role_name', 'string', nullable: false)
        t.addColumn('user_id', 'integer', nullable: false)

      }

      execute('ALTER TABLE USER_ROLES ADD CONSTRAINT fk_user_uid FOREIGN KEY (user_id) REFERENCES USERS')

  
    createTable("PARTICIPANTS") {t ->
      t.addVersionColumn()

      t.addColumn('birth_date', 'date', nullable: true)
      t.addColumn('gender', 'string', nullable: true)
      t.addColumn('maiden_name', 'string', nullable: true)
      t.addColumn('first_name', 'string', nullable: false)
      t.addColumn('last_name', 'string', nullable: false)
      t.addColumn('middle_name', 'string', nullable: true)
      t.addColumn('mrn_identifier', 'string', nullable: true);
      t.addColumn('user_id', 'integer', nullable: false)
    }

    execute('ALTER TABLE PARTICIPANTS ADD CONSTRAINT fk_user_uid FOREIGN KEY (user_id) REFERENCES USERS')

    execute('CREATE INDEX participants_flname_idx ON participants (first_name)')
    execute('CREATE INDEX participants_flname_idx2 ON participants (last_name)')

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