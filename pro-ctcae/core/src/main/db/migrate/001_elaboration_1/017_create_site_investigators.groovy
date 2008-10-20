class CreateSiteInvestigators extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
        createTable("SITE_INVESTIGATORS") {t ->
            t.addVersionColumn()

            t.addColumn('status_code', 'string', nullable: true)
            t.addColumn('status_date', 'date', nullable: true)
            t.addColumn('investigator_id', 'integer', nullable: false)
            t.addColumn('organization_id', 'integer', nullable: false)
         }

         execute('ALTER TABLE SITE_INVESTIGATORS ADD CONSTRAINT fk_si_inv FOREIGN KEY (investigator_id) REFERENCES INVESTIGATORS')
         execute('ALTER TABLE SITE_INVESTIGATORS ADD CONSTRAINT fk_si_org FOREIGN KEY (organization_id) REFERENCES ORGANIZATIONS')
         }

         void down() {
                execute("alter table SITE_INVESTIGATORS drop constraint fk_si_org")
                execute("alter table SITE_INVESTIGATORS drop constraint fk_si_inv")
                dropTable("SITE_INVESTIGATORS")
                }
             }