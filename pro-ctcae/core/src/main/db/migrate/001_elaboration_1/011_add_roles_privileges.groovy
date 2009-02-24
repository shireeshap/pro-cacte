import edu.northwestern.bioinformatics.bering.Migration

class AddRolesAndPriveleges extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {


    createTable("PRIVILEGES") {t ->

      t.addVersionColumn()

      t.addColumn('privilege_name', 'string', nullable: false)
      t.addColumn('display_name', 'string', nullable: false)

    }

    execute("ALTER TABLE PRIVILEGES ADD CONSTRAINT un_privilege_name UNIQUE (privilege_name)")

    createTable("ROLE_PRIVILEGES") {t ->

      t.addVersionColumn()

      t.addColumn('role_name', 'string', nullable: false)
      t.addColumn('privilege_id', 'integer', nullable: false)

    }

    execute('ALTER TABLE ROLE_PRIVILEGES ADD CONSTRAINT fk_privilege_id FOREIGN KEY (privilege_id) REFERENCES PRIVILEGES')


  }


  void down() {
    dropTable("ROLE_PRIVILEGES")
    dropTable("PRIVILEGES")


  }


}
