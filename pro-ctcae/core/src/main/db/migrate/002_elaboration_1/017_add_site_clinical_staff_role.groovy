class AddSiteClinicalStaffRoles extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {


        createTable("SITE_CLINICAL_STAFF_ROLES") {t ->
            t.addVersionColumn()

            t.addColumn('role_status', 'string', nullable: false)
            t.addColumn('role', 'string', nullable: false)
            t.addColumn('status_date', 'date', nullable: true)
            t.addColumn('site_clinical_staff_id', 'integer', nullable: false)
        }

        execute('ALTER TABLE SITE_CLINICAL_STAFF_ROLES ADD CONSTRAINT fk_site_cli_id FOREIGN KEY (site_clinical_staff_id) REFERENCES SITE_CLINICAL_STAFFS')


    }


    void down() {
        dropTable("SITE_CLINICAL_STAFF_ROLES")

    }


}
