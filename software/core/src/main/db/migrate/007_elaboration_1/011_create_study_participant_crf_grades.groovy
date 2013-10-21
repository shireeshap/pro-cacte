class StudyParticipantCrfGrades extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    createTable("STUDY_PARTICIPANT_CRF_GRADES") {t ->
      t.addVersionColumn()
      t.addColumn('sp_crf_schedule_id', 'integer', nullable: false)
	  t.addColumn('grade_mapping_version_id', 'integer', nullable: false)
	  t.addColumn('pro_ctc_terms_id', 'integer', nullable: true)
	  t.addColumn('meddra_llt_id', 'integer', nullable: true)
	  t.addColumn('grade_evaluation_date', 'date', nullable: true)
      t.addColumn('grade', 'string', nullable: true)
    }
    execute('ALTER TABLE STUDY_PARTICIPANT_CRF_GRADES ADD CONSTRAINT fk_spci_pcvvi FOREIGN KEY (grade_mapping_version_id) REFERENCES proctcae_grade_mapping_versions')
    execute('ALTER TABLE STUDY_PARTICIPANT_CRF_GRADES ADD CONSTRAINT fk_pro_ctc_terms FOREIGN KEY (pro_ctc_terms_id) REFERENCES PRO_CTC_TERMS')
	execute('ALTER TABLE STUDY_PARTICIPANT_CRF_GRADES ADD CONSTRAINT fk_meddra_llt FOREIGN KEY (meddra_llt_id) REFERENCES Meddra_LLT')
    execute('ALTER TABLE STUDY_PARTICIPANT_CRF_GRADES ADD CONSTRAINT fk_spci_spcs FOREIGN KEY (sp_crf_schedule_id) REFERENCES SP_CRF_SCHEDULES')

  }

  void down() {
    dropTable("STUDY_PARTICIPANT_CRF_GRADES")
  }
}
