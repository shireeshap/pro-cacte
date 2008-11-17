class CreateStudyDiseases extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {

       createTable("STUDY_DISEASES") {t ->

          t.addVersionColumn()

          t.addColumn('lead_disease', 'string', nullable: true)
          t.addColumn('term_id', 'integer', nullable: false)
          t.addColumn('study_id', 'integer', nullable: false)

 }
 execute('ALTER TABLE STUDY_DISEASES ADD CONSTRAINT fk_sty_dis_term FOREIGN KEY (term_id) REFERENCES DISEASE_TERMS')
 execute('ALTER TABLE STUDY_DISEASES ADD CONSTRAINT fk_sty_dis_study FOREIGN KEY (study_id) REFERENCES STUDIES')

 }

  void down() {

       execute("alter table STUDY_DISEASES drop constraint FK_sty_dis_study")
       execute("alter table STUDY_DISEASES drop constraint FK_sty_dis_term")

       dropTable("STUDY_DISEASES")
        }

        }