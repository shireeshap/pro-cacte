class StudyParticipantReportingModeHistory extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
      createTable("STUDY_PARTICIPANT_REPORTING_MODE_HISTORY") {t ->
         t.addVersionColumn()
         t.addColumn('mode', 'string', nullable: false)
         t.addColumn('spa_id', 'integer', nullable: false)
         t.addColumn('effective_start_date', 'date', nullable: false)
         t.addColumn('effective_end_date', 'date', nullable:true)
        }
         execute('ALTER TABLE STUDY_PARTICIPANT_REPORTING_MODE_HISTORY ADD CONSTRAINT fk_study_part_rpt_md_hist FOREIGN KEY (spa_id) REFERENCES STUDY_PARTICIPANT_ASSIGNMENTS')

    }

    void down() {
      dropTable("STUDY_PARTICIPANT_REPORTING_MODE_HISTORY")
      }
     }