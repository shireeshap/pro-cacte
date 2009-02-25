import edu.northwestern.bioinformatics.bering.Migration

class CreateClinicalStaffs extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {

    createTable("CLINICAL_STAFFS") {t ->

      t.addVersionColumn()

      t.addColumn('email_address', 'string', nullable: true)
      t.addColumn('fax_number', 'string', nullable: true)
      t.addColumn('nci_identifier', 'string', nullable: false)
      t.addColumn('phone_number', 'string', nullable: true)
      t.addColumn('first_name', 'string', nullable: false)
      t.addColumn('last_name', 'string', nullable: false)
      t.addColumn('middle_name', 'string', nullable: true)
      t.addColumn('title', 'string', nullable: true)
      t.addColumn('address', 'string', nullable: true)

    }

    createTable("ORGANIZATION_CLINICAL_STAFFS") {t ->
      t.addVersionColumn()

      t.addColumn('clinical_staff_id', 'integer', nullable: false)
      t.addColumn('organization_id', 'integer', nullable: false)
    }

    execute('ALTER TABLE ORGANIZATION_CLINICAL_STAFFS ADD CONSTRAINT fk_si_cli FOREIGN KEY (clinical_staff_id) REFERENCES CLINICAL_STAFFS')
    execute('ALTER TABLE ORGANIZATION_CLINICAL_STAFFS ADD CONSTRAINT fk_si_org FOREIGN KEY (organization_id) REFERENCES ORGANIZATIONS')





    createTable("STUDY_ORGANIZATION_CLINICAL_STAFFS") {t ->

      t.addVersionColumn()

      t.addColumn('organization_clinical_staff_id', 'integer', nullable: false)
      t.addColumn('study_organization_id', 'integer', nullable: false)
      t.addColumn('role_status', 'string', nullable: false)
      t.addColumn('role_name', 'string', nullable: false)
      t.addColumn('status_date', 'date', nullable: false)

    }


    execute('ALTER TABLE STUDY_ORGANIZATION_CLINICAL_STAFFS ADD CONSTRAINT fk_ss_cls_site_clinical_staff FOREIGN KEY (organization_clinical_staff_id) REFERENCES ORGANIZATION_CLINICAL_STAFFS')
    execute('ALTER TABLE STUDY_ORGANIZATION_CLINICAL_STAFFS ADD CONSTRAINT fk_ss_cls_study_site FOREIGN KEY (study_organization_id) REFERENCES STUDY_ORGANIZATIONS')

    createTable("STUDY_PARTICIPANT_CLINICAL_STAFFS") {t ->

      t.addVersionColumn()

      t.addColumn('so_clinical_staff_id', 'integer', nullable: false)
      t.addColumn('sp_assignment_id', 'integer', nullable: false)
      t.addColumn('is_primary', 'boolean', nullable: true)

    }


    execute('ALTER TABLE STUDY_PARTICIPANT_CLINICAL_STAFFS ADD CONSTRAINT fk_sp_cls_so_clinical_staff  FOREIGN KEY (so_clinical_staff_id) REFERENCES STUDY_ORGANIZATION_CLINICAL_STAFFS')
    execute('ALTER TABLE STUDY_PARTICIPANT_CLINICAL_STAFFS ADD CONSTRAINT  fk_sp_cls_site_clinical_staff FOREIGN KEY (sp_assignment_id) REFERENCES STUDY_PARTICIPANT_ASSIGNMENTS')

  }


  void down() {
    dropTable("STUDY_PARTICIPANT_CLINICAL_STAFFS")
    dropTable("STUDY_ORGANIZATION_CLINICAL_STAFFS")

    dropTable("ORGANIZATION_CLINICAL_STAFFS")


  }


}
