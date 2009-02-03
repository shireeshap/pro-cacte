class StudyParticipantCrfItems extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    createTable("STUDY_PARTICIPANT_CRF_ITEMS") {t ->
      t.addVersionColumn()
      t.addColumn('SP_CRF_SCHEDULE_ID', 'integer', nullable: false)
      t.addColumn('crf_item_id', 'integer', nullable: false)
      t.addColumn('pro_ctc_valid_value_id', 'integer', nullable: true)
    }
    execute('ALTER TABLE STUDY_PARTICIPANT_CRF_ITEMS ADD CONSTRAINT fk_spci_pcvvi FOREIGN KEY (pro_ctc_valid_value_id) REFERENCES PRO_CTC_VALID_VALUES')
    execute('ALTER TABLE STUDY_PARTICIPANT_CRF_ITEMS ADD CONSTRAINT fk_spc_crf_item FOREIGN KEY (crf_item_id) REFERENCES CRF_PAGE_ITEMS')

    execute('ALTER TABLE STUDY_PARTICIPANT_CRF_ITEMS ADD CONSTRAINT fk_spci_spcs FOREIGN KEY (SP_CRF_SCHEDULE_ID) REFERENCES SP_CRF_SCHEDULES')

  }

  void down() {
    dropTable("STUDY_PARTICIPANT_CRF_ITEMS")
  }
}
