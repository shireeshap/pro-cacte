import edu.northwestern.bioinformatics.bering.Migration

class AddRolesAndPriveleges extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {


    createTable("PRIVILEGES") {t ->


      t.addColumn('privilege_name', 'string', nullable: false)
      t.addColumn('display_name', 'string', nullable: false)

    }



    execute("ALTER TABLE PRIVILEGES ADD CONSTRAINT un_privilege_name UNIQUE (privilege_name)")

    insert('PRIVILEGES', [id: -1, privilege_name: "PRIVILEGE_CREATE_FORM", display_name: "PRIVILEGE_CREATE_FORM"], primaryKey: false)
    insert('PRIVILEGES', [id: -2, privilege_name: "PRIVILEGE_EDIT_FORM", display_name: "PRIVILEGE_EDIT_FORM"], primaryKey: false)
    insert('PRIVILEGES', [id: -3, privilege_name: "PRIVILEGE_MANAGE_FORM", display_name: "PRIVILEGE_MANAGE_FORM"], primaryKey: false)
    insert('PRIVILEGES', [id: -4, privilege_name: "PRIVILEGE_COPY_FORM", display_name: "PRIVILEGE_COPY_FORM"], primaryKey: false)
    insert('PRIVILEGES', [id: -5, privilege_name: "PRIVILEGE_DELETE_FORM", display_name: "PRIVILEGE_DELETE_FORM"], primaryKey: false)
    insert('PRIVILEGES', [id: -6, privilege_name: "PRIVILEGE_VERSION_FORM", display_name: "PRIVILEGE_VERSION_FORM"], primaryKey: false)
    insert('PRIVILEGES', [id: -7, privilege_name: "PRIVILEGE_RELEASE_FORM", display_name: "PRIVILEGE_RELEASE_FORM"], primaryKey: false)

    insert('PRIVILEGES', [id: -11, privilege_name: "PRIVILEGE_CREATE_STUDY", display_name: "PRIVILEGE_CREATE_STUDY"], primaryKey: false)
    insert('PRIVILEGES', [id: -12, privilege_name: "PRIVILEGE_ADD_STUDY_SITE", display_name: "PRIVILEGE_ADD_STUDY_SITE"], primaryKey: false)
    insert('PRIVILEGES', [id: -13, privilege_name: "PRIVILEGE_SEARCH_STUDY", display_name: "PRIVILEGE_SEARCH_STUDY"], primaryKey: false)
    insert('PRIVILEGES', [id: -14, privilege_name: "PRIVILEGE_EDIT_STUDY", display_name: "PRIVILEGE_EDIT_STUDY"], primaryKey: false)

    insert('PRIVILEGES', [id: -15, privilege_name: "PRIVILEGE_CREATE_CLINICAL_STAFF", display_name: "PRIVILEGE_CREATE_CLINICAL_STAFF"], primaryKey: false)
    insert('PRIVILEGES', [id: -16, privilege_name: "PRIVILEGE_EDIT_CLINICAL_STAFF", display_name: "PRIVILEGE_EDIT_CLINICAL_STAFF"], primaryKey: false)
    insert('PRIVILEGES', [id: -17, privilege_name: "PRIVILEGE_SEARCH_CLINICAL_STAFF", display_name: "PRIVILEGE_SEARCH_CLINICAL_STAFF"], primaryKey: false)

    createTable("ROLE_PRIVILEGES") {t ->


      t.addColumn('role_name', 'string', nullable: false)
      t.addColumn('privilege_id', 'integer', nullable: false)

    }

    execute('ALTER TABLE ROLE_PRIVILEGES ADD CONSTRAINT fk_privilege_id FOREIGN KEY (privilege_id) REFERENCES PRIVILEGES')

    insert('ROLE_PRIVILEGES', [id: -1, role_name: "CCA", privilege_id: "-11"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -2, role_name: "CCA", privilege_id: "-12"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -3, role_name: "CCA", privilege_id: "-13"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -4, role_name: "CCA", privilege_id: "-14"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -5, role_name: "LEAD_CRA", privilege_id: "-13"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -6, role_name: "LEAD_CRA", privilege_id: "-14"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -7, role_name: "LEAD_CRA", privilege_id: "-1"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -8, role_name: "LEAD_CRA", privilege_id: "-2"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -9, role_name: "LEAD_CRA", privilege_id: "-3"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -10, role_name: "LEAD_CRA", privilege_id: "-7"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -11, role_name: "LEAD_CRA", privilege_id: "-15"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -12, role_name: "LEAD_CRA", privilege_id: "-17"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -13, role_name: "PI", privilege_id: "-13"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -14, role_name: "PI", privilege_id: "-14"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -15, role_name: "PI", privilege_id: "-1"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -16, role_name: "PI", privilege_id: "-2"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -17, role_name: "PI", privilege_id: "-3"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -18, role_name: "PI", privilege_id: "-7"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -19, role_name: "PI", privilege_id: "-17"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -20, role_name: "ODC", privilege_id: "-17"], primaryKey: false)


  }


  void down() {
    dropTable("ROLE_PRIVILEGES")
    dropTable("PRIVILEGES")


  }


}
