class CreateStudyInvestigators extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
        createTable("STUDY_INVESTIGATORS") {t ->
            t.addVersionColumn()

            t.addColumn('status_code', 'string', nullable: true)
            t.addColumn('role_code', 'string', nullable: true)
            t.addColumn('signature_text', 'string', nullable: true)
            t.addColumn('site_investigator_id', 'integer', nullable: false)
            t.addColumn('study_organization_id', 'integer', nullable: false)
         }

         execute('ALTER TABLE STUDY_INVESTIGATORS ADD CONSTRAINT FK_study_investigator_site_investigators FOREIGN KEY (site_investigator_id) REFERENCES SITE_INVESTIGATORS')
         execute('ALTER TABLE STUDY_INVESTIGATORS ADD CONSTRAINT FK_study_investigator_study_organizations FOREIGN KEY (study_organization_id) REFERENCES STUDY_ORGANIZATIONS')
         }

         void down() {
                execute("alter table STUDY_INVESTIGATORS drop constraint FK_study_investigator_study_organizations")
                execute("alter table STUDY_INVESTIGATORS drop constraint FK_study_investigator_site_investigators")
                dropTable("STUDY_INVESTIGATORS")
                }
             }