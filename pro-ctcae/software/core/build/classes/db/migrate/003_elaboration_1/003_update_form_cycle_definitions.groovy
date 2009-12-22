class UpdateFormCycleDDefinitions extends edu.northwestern.bioinformatics.bering.Migration {

     void up() {
           addColumn("CRF_CYCLE_DEFINITIONS", 'form_arm_schedules_id', 'integer', nullable: false);
           dropColumn("CRF_CYCLE_DEFINITIONS", 'crf_id');
           dropColumn("CRF_CALENDARS", 'crf_id');
           addColumn("CRF_CALENDARS", 'form_arm_schedules_id', 'integer', nullable: false);
           addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'arm_id', 'integer', nullable: false);

          
        execute('ALTER TABLE CRF_CYCLE_DEFINITIONS ADD CONSTRAINT fk_ccd_fas FOREIGN KEY (form_arm_schedules_id) REFERENCES FORM_ARM_SCHEDULES')
        execute('ALTER TABLE CRF_CALENDARS ADD CONSTRAINT fk_cc_fas FOREIGN KEY (form_arm_schedules_id) REFERENCES FORM_ARM_SCHEDULES')
        execute('ALTER TABLE STUDY_PARTICIPANT_ASSIGNMENTS ADD CONSTRAINT fk_spa_arm FOREIGN KEY (arm_id) REFERENCES ARMS')

           }

           void down() {
              dropColumn("CRF_CYCLE_DEFINITIONS", 'form_arm_schedules_id')
              }

          }    