class AddClinicalStaffAssignmentRoles extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {


        createTable("CS_ASSIGNMENT_ROLES") {t ->
            t.addVersionColumn()

            t.addColumn('role_status', 'string', nullable: false)
            t.addColumn('role', 'string', nullable: false)
            t.addColumn('status_date', 'date', nullable: true)
            t.addColumn('clinical_staff_assignment_id', 'integer', nullable: false)
        }

        execute('ALTER TABLE CS_ASSIGNMENT_ROLES ADD CONSTRAINT fk_site_cli_id FOREIGN KEY (clinical_staff_assignment_id) REFERENCES CLINICAL_STAFF_ASSIGNMENTS')


    }


    void down() {
        dropTable("CS_ASSIGNMENT_ROLES")

    }


}
