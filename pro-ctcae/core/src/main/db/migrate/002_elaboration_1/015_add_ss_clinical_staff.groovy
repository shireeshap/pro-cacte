class AddStudySiteClinicalStaff extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {

        createTable("STUDY_SITE_CLINICAL_STAFFS") {t ->

            t.addVersionColumn()

            t.addColumn('site_clinical_staff_id', 'integer', nullable: false)
            t.addColumn('study_site_id', 'integer', nullable: false)

        }


        execute('ALTER TABLE STUDY_SITE_CLINICAL_STAFFS ADD CONSTRAINT fk_ss_cls_site_clinical_staff FOREIGN KEY (site_clinical_staff_id) REFERENCES SITE_CLINICAL_STAFFS')
        execute('ALTER TABLE STUDY_SITE_CLINICAL_STAFFS ADD CONSTRAINT fk_ss_cls_study_site FOREIGN KEY (study_site_id) REFERENCES STUDY_ORGANIZATIONS')


    }


    void down() {
        dropTable("STUDY_SITE_CLINICAL_STAFFS")



    }


}
