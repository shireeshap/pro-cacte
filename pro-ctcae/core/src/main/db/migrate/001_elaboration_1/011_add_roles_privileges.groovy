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

    insert('PRIVILEGES', [id: -21, privilege_name: "PRIVILEGE_CREATE_STUDY", display_name: "PRIVILEGE_CREATE_STUDY"], primaryKey: false)
    insert('PRIVILEGES', [id: -22, privilege_name: "PRIVILEGE_ADD_STUDY_SITE", display_name: "PRIVILEGE_ADD_STUDY_SITE"], primaryKey: false)
    insert('PRIVILEGES', [id: -23, privilege_name: "PRIVILEGE_SEARCH_STUDY", display_name: "PRIVILEGE_SEARCH_STUDY"], primaryKey: false)
    insert('PRIVILEGES', [id: -24, privilege_name: "PRIVILEGE_EDIT_STUDY", display_name: "PRIVILEGE_EDIT_STUDY"], primaryKey: false)
    insert('PRIVILEGES', [id: -25, privilege_name: "PRIVILEGE_ADD_STUDY_CLINICAL_STAFF", display_name: "PRIVILEGE_EDIT_STUDY"], primaryKey: false)
    insert('PRIVILEGES', [id: -26, privilege_name: "PRIVILEGE_ADD_STUDY_SITE_CLINICAL_STAFF", display_name: "PRIVILEGE_EDIT_STUDY"], primaryKey: false)
    insert('PRIVILEGES', [id: -27, privilege_name: "PRIVILEGE_VIEW_STUDY", display_name: "PRIVILEGE_VIEW_STUDY"], primaryKey: false)

    insert('PRIVILEGES', [id: -41, privilege_name: "PRIVILEGE_CREATE_CLINICAL_STAFF", display_name: "PRIVILEGE_CREATE_CLINICAL_STAFF"], primaryKey: false)
    insert('PRIVILEGES', [id: -42, privilege_name: "PRIVILEGE_EDIT_CLINICAL_STAFF", display_name: "PRIVILEGE_EDIT_CLINICAL_STAFF"], primaryKey: false)
    insert('PRIVILEGES', [id: -43, privilege_name: "PRIVILEGE_SEARCH_CLINICAL_STAFF", display_name: "PRIVILEGE_SEARCH_CLINICAL_STAFF"], primaryKey: false)

    createTable("ROLE_PRIVILEGES") {t ->


      t.addColumn('role_name', 'string', nullable: false)
      t.addColumn('privilege_id', 'integer', nullable: false)

    }

    execute('ALTER TABLE ROLE_PRIVILEGES ADD CONSTRAINT fk_privilege_id FOREIGN KEY (privilege_id) REFERENCES PRIVILEGES')

//    insert('ROLE_PRIVILEGES', [id: -1, role_name: "CCA", privilege_id: "-11"], primaryKey: false)
//    insert('ROLE_PRIVILEGES', [id: -2, role_name: "CCA", privilege_id: "-12"], primaryKey: false)
//    insert('ROLE_PRIVILEGES', [id: -3, role_name: "CCA", privilege_id: "-13"], primaryKey: false)
//    insert('ROLE_PRIVILEGES', [id: -4, role_name: "CCA", privilege_id: "-14"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -21, role_name: "LEAD_CRA", privilege_id: "-1"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -22, role_name: "LEAD_CRA", privilege_id: "-2"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -23, role_name: "LEAD_CRA", privilege_id: "-3"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -24, role_name: "LEAD_CRA", privilege_id: "-7"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -25, role_name: "LEAD_CRA", privilege_id: "-23"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -26, role_name: "LEAD_CRA", privilege_id: "-24"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -27, role_name: "LEAD_CRA", privilege_id: "-26"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -28, role_name: "LEAD_CRA", privilege_id: "-27"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -29, role_name: "LEAD_CRA", privilege_id: "-41"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -30, role_name: "LEAD_CRA", privilege_id: "-43"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -41, role_name: "PI", privilege_id: "-1"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -42, role_name: "PI", privilege_id: "-2"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -43, role_name: "PI", privilege_id: "-3"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -44, role_name: "PI", privilege_id: "-7"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -45, role_name: "PI", privilege_id: "-23"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -46, role_name: "PI", privilege_id: "-24"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -47, role_name: "PI", privilege_id: "-27"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -48, role_name: "PI", privilege_id: "-43"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -61, role_name: "ODC", privilege_id: "-23"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -62, role_name: "ODC", privilege_id: "-27"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -63, role_name: "ODC", privilege_id: "-43"], primaryKey: false)


  }


  void down() {
    dropTable("ROLE_PRIVILEGES")
    dropTable("PRIVILEGES")


  }


}
