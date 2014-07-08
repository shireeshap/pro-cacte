class StudyMode extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
      createTable("STUDY_MODES") {t ->
         t.addVersionColumn()
         t.addColumn('mode', 'string', nullable: true)
         t.addColumn('study_id', 'integer', nullable: false)
        }

         execute('ALTER TABLE STUDY_MODES ADD CONSTRAINT fk_study_md FOREIGN KEY (study_id) REFERENCES STUDIES')

    }

    void down() {
      dropTable("STUDY_MODES")
      }
     }