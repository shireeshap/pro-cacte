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


    insert('PRIVILEGES', [id: -61, privilege_name: "PRIVILEGE_CREATE_PARTICIPANT", display_name: "PRIVILEGE_CREATE_PARTICIPANT"], primaryKey: false)
    insert('PRIVILEGES', [id: -62, privilege_name: "PRIVILEGE_SEARCH_PARTICIPANT", display_name: "PRIVILEGE_SEARCH_PARTICIPANT"], primaryKey: false)
    insert('PRIVILEGES', [id: -68, privilege_name: "PRIVILEGE_PARTICIPANT_DISPLAY_STUDY_SITES", display_name: "PRIVILEGE_PARTICIPANT_DISPLAY_STUDY_SITES"], primaryKey: false)
    insert('PRIVILEGES', [id: -66, privilege_name: "PRIVILEGE_PARTICIPANT_ADD_NOTIFICATION_CLINICAL_STAFF", display_name: "PRIVILEGE_PARTICIPANT_ADD_NOTIFICATION_CLINICAL_STAFF"], primaryKey: false)

    insert('PRIVILEGES', [id: -63, privilege_name: "PRIVILEGE_PARTICIPANT_SCHEDULE_CRF", display_name: "PRIVILEGE_PARTICIPANT_SCHEDULE_CRF"], primaryKey: false)
    insert('PRIVILEGES', [id: -65, privilege_name: "PRIVILEGE_PARTICIPANT_ADD_CRF_SCHEDULE", display_name: "PRIVILEGE_PARTICIPANT_ADD_CRF_SCHEDULE"], primaryKey: false)
    insert('PRIVILEGES', [id: -67, privilege_name: "PRIVILEGE_PARTICIPANT_DISPLAY_CALENDAR", display_name: "PRIVILEGE_PARTICIPANT_DISPLAY_CALENDAR"], primaryKey: false)

    insert('PRIVILEGES', [id: -64, privilege_name: "PRIVILEGE_PARTICIPANT_INBOX", display_name: "PRIVILEGE_PARTICIPANT_INBOX"], primaryKey: false)

    insert('PRIVILEGES', [id: -69, privilege_name: "PRIVILEGE_EDIT_PARTICIPANT", display_name: "PRIVILEGE_EDIT_PARTICIPANT"], primaryKey: false)
    insert('PRIVILEGES', [id: -70, privilege_name: "PRIVILEGE_VIEW_PARTICIPANT", display_name: "PRIVILEGE_VIEW_PARTICIPANT"], primaryKey: false)



    createTable("ROLE_PRIVILEGES") {t ->


      t.addColumn('role_name', 'string', nullable: false)
      t.addColumn('privilege_id', 'integer', nullable: false)

    }

    execute('ALTER TABLE ROLE_PRIVILEGES ADD CONSTRAINT fk_privilege_id FOREIGN KEY (privilege_id) REFERENCES PRIVILEGES')

    insert('ROLE_PRIVILEGES', [id: -101, role_name: "TREATING_PHYSICIAN", privilege_id: "-62"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -102, role_name: "TREATING_PHYSICIAN", privilege_id: "-63"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -103, role_name: "TREATING_PHYSICIAN", privilege_id: "-65"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -104, role_name: "TREATING_PHYSICIAN", privilege_id: "-67"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -121, role_name: "NURSE", privilege_id: "-62"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -122, role_name: "NURSE", privilege_id: "-63"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -123, role_name: "NURSE", privilege_id: "-65"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -124, role_name: "NURSE", privilege_id: "-67"], primaryKey: false)




    insert('ROLE_PRIVILEGES', [id: -71, role_name: "SITE_PI", privilege_id: "-61"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -72, role_name: "SITE_PI", privilege_id: "-62"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -73, role_name: "SITE_PI", privilege_id: "-63"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -74, role_name: "SITE_PI", privilege_id: "-65"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -75, role_name: "SITE_PI", privilege_id: "-66"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -76, role_name: "SITE_PI", privilege_id: "-67"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -77, role_name: "SITE_PI", privilege_id: "-68"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -78, role_name: "SITE_PI", privilege_id: "-69"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -79, role_name: "SITE_PI", privilege_id: "-70"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -80, role_name: "SITE_PI", privilege_id: "-43"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -91, role_name: "SITE_CRA", privilege_id: "-61"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -92, role_name: "SITE_CRA", privilege_id: "-62"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -93, role_name: "SITE_CRA", privilege_id: "-63"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -94, role_name: "SITE_CRA", privilege_id: "-65"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -95, role_name: "SITE_CRA", privilege_id: "-66"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -96, role_name: "SITE_CRA", privilege_id: "-67"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -97, role_name: "SITE_CRA", privilege_id: "-68"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -98, role_name: "SITE_CRA", privilege_id: "-69"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -99, role_name: "SITE_CRA", privilege_id: "-70"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -100, role_name: "SITE_CRA", privilege_id: "-43"], primaryKey: false)


    insert('ROLE_PRIVILEGES', [id: -1, role_name: "CCA", privilege_id: "-21"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -2, role_name: "CCA", privilege_id: "-22"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -3, role_name: "CCA", privilege_id: "-23"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -4, role_name: "CCA", privilege_id: "-24"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -5, role_name: "CCA", privilege_id: "-25"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -6, role_name: "CCA", privilege_id: "-27"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -7, role_name: "CCA", privilege_id: "-41"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -8, role_name: "CCA", privilege_id: "-42"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -9, role_name: "CCA", privilege_id: "-43"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -21, role_name: "LEAD_CRA", privilege_id: "-1"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -22, role_name: "LEAD_CRA", privilege_id: "-2"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -23, role_name: "LEAD_CRA", privilege_id: "-3"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -24, role_name: "LEAD_CRA", privilege_id: "-6"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -25, role_name: "LEAD_CRA", privilege_id: "-7"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -28, role_name: "LEAD_CRA", privilege_id: "-63"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -29, role_name: "LEAD_CRA", privilege_id: "-65"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -30, role_name: "LEAD_CRA", privilege_id: "-67"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -35, role_name: "LEAD_CRA", privilege_id: "-23"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -36, role_name: "LEAD_CRA", privilege_id: "-24"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -37, role_name: "LEAD_CRA", privilege_id: "-26"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -38, role_name: "LEAD_CRA", privilege_id: "-27"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -39, role_name: "LEAD_CRA", privilege_id: "-41"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -40, role_name: "LEAD_CRA", privilege_id: "-43"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -41, role_name: "PI", privilege_id: "-1"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -42, role_name: "PI", privilege_id: "-2"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -43, role_name: "PI", privilege_id: "-3"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -44, role_name: "PI", privilege_id: "-6"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -45, role_name: "PI", privilege_id: "-7"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -55, role_name: "PI", privilege_id: "-23"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -56, role_name: "PI", privilege_id: "-24"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -57, role_name: "PI", privilege_id: "-27"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -58, role_name: "PI", privilege_id: "-43"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -59, role_name: "PI", privilege_id: "-63"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -60, role_name: "PI", privilege_id: "-65"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -61, role_name: "PI", privilege_id: "-67"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -131, role_name: "ODC", privilege_id: "-23"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -132, role_name: "ODC", privilege_id: "-27"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -133, role_name: "ODC", privilege_id: "-43"], primaryKey: false)


  }


  void down() {
    dropTable("ROLE_PRIVILEGES")
    dropTable("PRIVILEGES")


  }


}
