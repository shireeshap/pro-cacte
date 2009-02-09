class AddStudyParticipantClinicalStaff extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {

        createTable("STUDY_PARTICIPANT_CLINICAL_STAFFS") {t ->

            t.addVersionColumn()

            t.addColumn('study_site_clinical_staff_id', 'integer', nullable: false)
            t.addColumn('sp_assignment_id', 'integer', nullable: false)

        }


        execute('ALTER TABLE STUDY_PARTICIPANT_CLINICAL_STAFFS ADD CONSTRAINT fk_ss_cls_site_clinical_staff FOREIGN KEY (study_site_clinical_staff_id) REFERENCES STUDY_SITE_CLINICAL_STAFFS')
        execute('ALTER TABLE STUDY_PARTICIPANT_CLINICAL_STAFFS ADD CONSTRAINT fk_ss_cls_study_site FOREIGN KEY (sp_assignment_id) REFERENCES STUDY_PARTICIPANT_ASSIGNMENTS')


    }


    void down() {
        dropTable("STUDY_PARTICIPANT_CLINICAL_STAFFS")



    }


}
