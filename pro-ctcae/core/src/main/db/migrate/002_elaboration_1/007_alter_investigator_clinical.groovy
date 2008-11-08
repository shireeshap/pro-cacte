class AlterInvestigatorClinical extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    dropTable("STUDY_INVESTIGATORS")

    dropTable("SITE_INVESTIGATORS")

    dropTable("INVESTIGATORS")

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

    createTable("SITE_CLINICAL_STAFFS") {t ->
      t.addVersionColumn()

      t.addColumn('status_code', 'string', nullable: true)
      t.addColumn('status_date', 'date', nullable: true)
      t.addColumn('clinical_staff_id', 'integer', nullable: false)
      t.addColumn('organization_id', 'integer', nullable: false)
    }

    execute('ALTER TABLE SITE_CLINICAL_STAFFS ADD CONSTRAINT fk_si_cli FOREIGN KEY (clinical_staff_id) REFERENCES CLINICAL_STAFFS')
    execute('ALTER TABLE SITE_CLINICAL_STAFFS ADD CONSTRAINT fk_si_org FOREIGN KEY (organization_id) REFERENCES ORGANIZATIONS')


    createTable("STUDY_CLINICAL_STAFFS") {t ->
      t.addVersionColumn()

      t.addColumn('status_code', 'string', nullable: true)
      t.addColumn('role_code', 'string', nullable: true)
      t.addColumn('signature_text', 'string', nullable: true)
      t.addColumn('site_clinical_staff_id', 'integer', nullable: false)
      t.addColumn('study_organization_id', 'integer', nullable: false)
    }

    execute('ALTER TABLE STUDY_CLINICAL_STAFFS ADD CONSTRAINT FK_study_cli_site_cli FOREIGN KEY (site_clinical_staff_id) REFERENCES SITE_CLINICAL_STAFFS')
    execute('ALTER TABLE STUDY_CLINICAL_STAFFS ADD CONSTRAINT FK_study_cli_study_org FOREIGN KEY (study_organization_id) REFERENCES STUDY_ORGANIZATIONS')
  }


  void down() {
    execute("alter table STUDY_CLINICAL_STAFFS drop constraint FK_study_cli_study_org")
    execute("alter table STUDY_CLINICAL_STAFFS drop constraint FK_study_cli__site_cli")
    dropTable("STUDY_CLINICAL_STAFFS")

    execute("alter table SITE_CLINICAL_STAFFS drop constraint FK_si_org")
    execute("alter table SITE_CLINICAL_STAFFS drop constraint FK_si_cli")
    dropTable("SITE_CLINICAL_STAFFS")


  }


}
