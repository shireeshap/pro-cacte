class CreateStudyArms extends  edu.northwestern.bioinformatics.bering.Migration {
 void up() {
   createTable("ARMS") {t ->
        t.addColumn("title", "string", nullable: false)
        t.addColumn("description", "string", nullable: true)
        t.addColumn("study_id", "integer", nullable: false)
        t.addVersionColumn()
     }
     execute('ALTER TABLE ARMS ADD CONSTRAINT fk_sa_study FOREIGN KEY (study_id) REFERENCES  STUDIES')

     createTable("FORM_ARM_SCHEDULES") {t ->
       t.addVersionColumn()
       t.addColumn("form_id", "integer", nullable: false)
       t.addColumn("arm_id", "integer", nullable: false)
       }
       execute('ALTER TABLE FORM_ARM_SCHEDULES ADD CONSTRAINT fk_fas_form FOREIGN KEY (form_id) REFERENCES CRFS')
       execute('ALTER TABLE FORM_ARM_SCHEDULES ADD CONSTRAINT fk_fas_arm FOREIGN KEY (arm_id) REFERENCES ARMS')
     }

     void down() {
        dropTable("FORM_ARM_SCHEDULES")
        dropTable("ARMS")
        }
       }