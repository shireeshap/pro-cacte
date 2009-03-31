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
    insert('PRIVILEGES', [id: -3, privilege_name: "PRIVILEGE_SEARCH_FORM", display_name: "PRIVILEGE_SEARCH_FORM"], primaryKey: false)
    insert('PRIVILEGES', [id: -4, privilege_name: "PRIVILEGE_COPY_FORM", display_name: "PRIVILEGE_COPY_FORM"], primaryKey: false)
    insert('PRIVILEGES', [id: -5, privilege_name: "PRIVILEGE_DELETE_FORM", display_name: "PRIVILEGE_DELETE_FORM"], primaryKey: false)
    insert('PRIVILEGES', [id: -6, privilege_name: "PRIVILEGE_VERSION_FORM", display_name: "PRIVILEGE_VERSION_FORM"], primaryKey: false)
    insert('PRIVILEGES', [id: -7, privilege_name: "PRIVILEGE_RELEASE_FORM", display_name: "PRIVILEGE_RELEASE_FORM"], primaryKey: false)
    insert('PRIVILEGES', [id: -8, privilege_name: "PRIVILEGE_ADD_FORM_RULES", display_name: "PRIVILEGE_EDIT_FORM"], primaryKey: false)

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
    insert('PRIVILEGES', [id: -71, privilege_name: "PRIVILEGE_MONITOR_FORM", display_name: "PRIVILEGE_MONITOR_FORM"], primaryKey: false)

    insert('PRIVILEGES', [id: -301, privilege_name: "PRIVILEGE_CREATE_CCA", display_name: "PRIVILEGE_CREATE_CCA"], primaryKey: false)

    insert('PRIVILEGES', [id: -401, privilege_name: "gov.nih.nci.ctcae.core.domain.Organization.GROUP", display_name: "gov.nih.nci.ctcae.core.domain.Organization.GROUP"], primaryKey: false)
    insert('PRIVILEGES', [id: -402, privilege_name: "gov.nih.nci.ctcae.core.domain.ClinicalStaff.GROUP", display_name: "gov.nih.nci.ctcae.core.domain.ClinicalStaff.GROUP"], primaryKey: false)
    insert('PRIVILEGES', [id: -403, privilege_name: "gov.nih.nci.ctcae.core.domain.Study.GROUP", display_name: "gov.nih.nci.ctcae.core.domain.Study.GROUP"], primaryKey: false)
    insert('PRIVILEGES', [id: -404, privilege_name: "gov.nih.nci.ctcae.core.domain.StudyOrganization.GROUP", display_name: "gov.nih.nci.ctcae.core.domain.StudyOrganization.GROUP"], primaryKey: false)



    createTable("ROLE_PRIVILEGES") {t ->


      t.addColumn('role_name', 'string', nullable: false)
      t.addColumn('privilege_id', 'integer', nullable: false)

    }

    execute('ALTER TABLE ROLE_PRIVILEGES ADD CONSTRAINT fk_privilege_id FOREIGN KEY (privilege_id) REFERENCES PRIVILEGES')

    insert('ROLE_PRIVILEGES', [id: -901, role_name: "ADMIN", privilege_id: "-301"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -902, role_name: "ADMIN", privilege_id: "-41"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -903, role_name: "ADMIN", privilege_id: "-42"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -904, role_name: "ADMIN", privilege_id: "-43"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -905, role_name: "ADMIN", privilege_id: "-401"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -906, role_name: "ADMIN", privilege_id: "-402"], primaryKey: false)



    insert('ROLE_PRIVILEGES', [id: -801, role_name: "TREATING_PHYSICIAN", privilege_id: "-62"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -802, role_name: "TREATING_PHYSICIAN", privilege_id: "-63"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -803, role_name: "TREATING_PHYSICIAN", privilege_id: "-65"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -804, role_name: "TREATING_PHYSICIAN", privilege_id: "-67"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -805, role_name: "TREATING_PHYSICIAN", privilege_id: "-3"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -807, role_name: "TREATING_PHYSICIAN", privilege_id: "-71"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -921, role_name: "NURSE", privilege_id: "-62"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -922, role_name: "NURSE", privilege_id: "-63"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -923, role_name: "NURSE", privilege_id: "-65"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -924, role_name: "NURSE", privilege_id: "-67"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -925, role_name: "NURSE", privilege_id: "-3"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -926, role_name: "NURSE", privilege_id: "-71"], primaryKey: false)



    insert('ROLE_PRIVILEGES', [id: -681, role_name: "SITE_CRA", privilege_id: "-61"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -682, role_name: "SITE_CRA", privilege_id: "-62"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -683, role_name: "SITE_CRA", privilege_id: "-63"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -684, role_name: "SITE_CRA", privilege_id: "-65"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -685, role_name: "SITE_CRA", privilege_id: "-66"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -686, role_name: "SITE_CRA", privilege_id: "-67"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -687, role_name: "SITE_CRA", privilege_id: "-68"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -688, role_name: "SITE_CRA", privilege_id: "-69"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -689, role_name: "SITE_CRA", privilege_id: "-70"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -702, role_name: "SITE_CRA", privilege_id: "-71"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -690, role_name: "SITE_CRA", privilege_id: "-43"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -691, role_name: "SITE_CRA", privilege_id: "-41"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -696, role_name: "SITE_CRA", privilege_id: "-23"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -697, role_name: "SITE_CRA", privilege_id: "-26"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -698, role_name: "SITE_CRA", privilege_id: "-27"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -699, role_name: "SITE_CRA", privilege_id: "-3"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -700, role_name: "SITE_CRA", privilege_id: "-8"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -701, role_name: "SITE_CRA", privilege_id: "-2"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -781, role_name: "SITE_PI", privilege_id: "-61"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -782, role_name: "SITE_PI", privilege_id: "-62"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -783, role_name: "SITE_PI", privilege_id: "-63"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -784, role_name: "SITE_PI", privilege_id: "-65"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -785, role_name: "SITE_PI", privilege_id: "-66"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -786, role_name: "SITE_PI", privilege_id: "-67"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -787, role_name: "SITE_PI", privilege_id: "-68"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -788, role_name: "SITE_PI", privilege_id: "-69"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -789, role_name: "SITE_PI", privilege_id: "-70"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -808, role_name: "SITE_PI", privilege_id: "-71"], primaryKey: false)


    insert('ROLE_PRIVILEGES', [id: -790, role_name: "SITE_PI", privilege_id: "-43"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -791, role_name: "SITE_PI", privilege_id: "-41"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -796, role_name: "SITE_PI", privilege_id: "-23"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -797, role_name: "SITE_PI", privilege_id: "-26"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -798, role_name: "SITE_PI", privilege_id: "-27"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -799, role_name: "SITE_PI", privilege_id: "-3"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -800, role_name: "SITE_PI", privilege_id: "-8"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -806, role_name: "SITE_PI", privilege_id: "-2"], primaryKey: false)


    insert('ROLE_PRIVILEGES', [id: -1, role_name: "CCA", privilege_id: "-21"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -2, role_name: "CCA", privilege_id: "-22"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -3, role_name: "CCA", privilege_id: "-23"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -4, role_name: "CCA", privilege_id: "-24"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -5, role_name: "CCA", privilege_id: "-25"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -6, role_name: "CCA", privilege_id: "-27"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -7, role_name: "CCA", privilege_id: "-41"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -8, role_name: "CCA", privilege_id: "-42"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -9, role_name: "CCA", privilege_id: "-43"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -10, role_name: "CCA", privilege_id: "-401"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -11, role_name: "CCA", privilege_id: "-402"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -12, role_name: "CCA", privilege_id: "-403"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -13, role_name: "CCA", privilege_id: "-404"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -121, role_name: "LEAD_CRA", privilege_id: "-1"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -122, role_name: "LEAD_CRA", privilege_id: "-2"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -119, role_name: "LEAD_CRA", privilege_id: "-4"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -118, role_name: "LEAD_CRA", privilege_id: "-5"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -123, role_name: "LEAD_CRA", privilege_id: "-3"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -124, role_name: "LEAD_CRA", privilege_id: "-6"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -125, role_name: "LEAD_CRA", privilege_id: "-7"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -141, role_name: "LEAD_CRA", privilege_id: "-8"], primaryKey: false)


    insert('ROLE_PRIVILEGES', [id: -126, role_name: "LEAD_CRA", privilege_id: "-41"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -127, role_name: "LEAD_CRA", privilege_id: "-43"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -128, role_name: "LEAD_CRA", privilege_id: "-63"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -129, role_name: "LEAD_CRA", privilege_id: "-65"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -130, role_name: "LEAD_CRA", privilege_id: "-67"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -131, role_name: "LEAD_CRA", privilege_id: "-61"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -132, role_name: "LEAD_CRA", privilege_id: "-62"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -133, role_name: "LEAD_CRA", privilege_id: "-68"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -134, role_name: "LEAD_CRA", privilege_id: "-66"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -135, role_name: "LEAD_CRA", privilege_id: "-69"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -136, role_name: "LEAD_CRA", privilege_id: "-70"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -142, role_name: "LEAD_CRA", privilege_id: "-71"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -137, role_name: "LEAD_CRA", privilege_id: "-23"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -138, role_name: "LEAD_CRA", privilege_id: "-24"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -139, role_name: "LEAD_CRA", privilege_id: "-26"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -140, role_name: "LEAD_CRA", privilege_id: "-27"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -221, role_name: "PI", privilege_id: "-1"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -222, role_name: "PI", privilege_id: "-2"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -219, role_name: "PI", privilege_id: "-4"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -218, role_name: "PI", privilege_id: "-5"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -223, role_name: "PI", privilege_id: "-3"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -224, role_name: "PI", privilege_id: "-6"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -225, role_name: "PI", privilege_id: "-7"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -241, role_name: "PI", privilege_id: "-8"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -226, role_name: "PI", privilege_id: "-41"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -227, role_name: "PI", privilege_id: "-43"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -228, role_name: "PI", privilege_id: "-63"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -229, role_name: "PI", privilege_id: "-65"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -230, role_name: "PI", privilege_id: "-67"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -231, role_name: "PI", privilege_id: "-61"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -232, role_name: "PI", privilege_id: "-62"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -233, role_name: "PI", privilege_id: "-68"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -234, role_name: "PI", privilege_id: "-66"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -235, role_name: "PI", privilege_id: "-69"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -236, role_name: "PI", privilege_id: "-70"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -242, role_name: "PI", privilege_id: "-71"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -237, role_name: "PI", privilege_id: "-23"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -238, role_name: "PI", privilege_id: "-24"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -239, role_name: "PI", privilege_id: "-27"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -240, role_name: "PI", privilege_id: "-26"], primaryKey: false)




    insert('ROLE_PRIVILEGES', [id: -331, role_name: "ODC", privilege_id: "-23"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -332, role_name: "ODC", privilege_id: "-27"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -333, role_name: "ODC", privilege_id: "-43"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -334, role_name: "ODC", privilege_id: "-62"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -335, role_name: "ODC", privilege_id: "-71"], primaryKey: false)

    insert('ROLE_PRIVILEGES', [id: -336, role_name: "PARTICIPANT", privilege_id: "-64"], primaryKey: false)



  }


  void down() {
    dropTable("ROLE_PRIVILEGES")
    dropTable("PRIVILEGES")


  }


}
