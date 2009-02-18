import edu.northwestern.bioinformatics.bering.Migration

class AddRolesAndPriveleges extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {

    createTable("ROLES") {t ->

      t.addVersionColumn()

      t.addColumn('role_name', 'string', nullable: false)
      t.addColumn('role_type', 'string', nullable: false)

    }


    execute("ALTER TABLE ROLES ADD CONSTRAINT un_role_name UNIQUE (role_name)")

    insert('ROLES', [id: -1, role_name: "Coordinating Center Administrator", role_type: "SITE_LEVEL"], primaryKey: false)
    insert('ROLES', [id: -2, role_name: "Overall Data Coordinator", role_type: "STUDY_LEVEL"], primaryKey: false)
    insert('ROLES', [id: -3, role_name: "Principal Investigator/Study Chair", role_type: "STUDY_LEVEL"], primaryKey: false)
    insert('ROLES', [id: -4, role_name: "Lead CRA", role_type: "SITE_LEVEL"], primaryKey: false)
    insert('ROLES', [id: -5, role_name: "Site PI", role_type: "STUDY_SITE_LEVEL"], primaryKey: false)
    insert('ROLES', [id: -6, role_name: "Site CRA", role_type: "STUDY_SITE_LEVEL"], primaryKey: false)
    insert('ROLES', [id: -7, role_name: "Site Investigator/Treating Physician", role_type: "STUDY_SITE_LEVEL"], primaryKey: false)
    insert('ROLES', [id: -8, role_name: "Research Nurse", role_type: "STUDY_SITE_LEVEL"], primaryKey: false)
    insert('ROLES', [id: -9, role_name: "Participant", role_type: "SITE_LEVEL"], primaryKey: false)


    createTable("PRIVILEGES") {t ->

      t.addVersionColumn()

      t.addColumn('privilege_name', 'string', nullable: false)
      t.addColumn('display_name', 'string', nullable: false)

    }

    execute("ALTER TABLE PRIVILEGES ADD CONSTRAINT un_privilege_name UNIQUE (privilege_name)")

    createTable("ROLE_PRIVILEGES") {t ->

      t.addVersionColumn()

      t.addColumn('role_id', 'integer', nullable: false)
      t.addColumn('privilege_id', 'integer', nullable: false)

    }

    execute('ALTER TABLE ROLE_PRIVILEGES ADD CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES ROLES')
    execute('ALTER TABLE ROLE_PRIVILEGES ADD CONSTRAINT fk_privilege_id FOREIGN KEY (privilege_id) REFERENCES PRIVILEGES')


  }


  void down() {
    dropTable("ROLE_PRIVILEGES")
    dropTable("PRIVILEGES")
    dropTable("ROLES")


  }


}
