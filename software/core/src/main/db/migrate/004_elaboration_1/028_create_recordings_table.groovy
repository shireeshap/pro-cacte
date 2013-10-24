class StudyParticipantReportingModeHistory extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
    
      createTable("sp_crf_sch_sympt_records") {t ->
         t.addVersionColumn()
         t.addColumn('sp_crf_schedules_id', 'integer', nullable:false)
         t.addColumn('filename', 'string', nullable:false)
         t.addColumn('filepath', 'string', nullable:false)
         t.addColumn('creation_date', 'date', nullable:true)
	  }
	  execute('ALTER TABLE sp_crf_sch_sympt_records ADD CONSTRAINT fk_sp_crf_schedules_id FOREIGN KEY (sp_crf_schedules_id) REFERENCES sp_crf_schedules')

      createTable("ivrs_call_history") {t ->
         t.addVersionColumn()
         t.addColumn('ivrs_schedules_id', 'integer', nullable:false)
         t.addColumn('call_status', 'string', nullable:true)
         t.addColumn('call_time', 'date', nullable:true)
	  }
  	  execute('ALTER TABLE ivrs_call_history ADD CONSTRAINT fk_IVRS_schedules_id FOREIGN KEY (ivrs_schedules_id) REFERENCES ivrs_schedules')
	  
    }
    

    void down() {
      dropTable("sp_crf_sch_sympt_records")
      dropTable("ivrs_call_history")
    }

}