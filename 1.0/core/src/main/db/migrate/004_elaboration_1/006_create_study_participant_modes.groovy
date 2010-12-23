class StudyParticipantMode extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
      createTable("STUDY_PARTICIPANT_MODES") {t ->
         t.addVersionColumn()
         t.addColumn('mode', 'string', nullable: true)
         t.addColumn('spa_id', 'integer', nullable: false)
         t.addColumn('is_email', 'boolean', nullable:true)
         t.addColumn('is_call', 'boolean', nullable:true)
         t.addColumn('is_text', 'boolean', nullable:true)
        }
         execute('ALTER TABLE STUDY_PARTICIPANT_MODES ADD CONSTRAINT fk_study_part_md FOREIGN KEY (spa_id) REFERENCES STUDY_PARTICIPANT_ASSIGNMENTS')

    }

    void down() {
      dropTable("STUDY_PARTICIPANT_MODES")
      }
     }