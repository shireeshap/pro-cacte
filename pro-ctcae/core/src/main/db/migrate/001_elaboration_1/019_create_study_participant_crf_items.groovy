class StudyParticipantCrfItems extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		createTable("STUDY_PARTICIPANT_CRF_ITEMS") {t ->
			t.addVersionColumn()
			t.addColumn('study_participant_crf_id', 'integer', nullable: false)
			t.addColumn('crf_item_id', 'integer', nullable: false)
			t.addColumn('pro_ctc_valid_value_id', 'integer', nullable: true)
		}
		execute('ALTER TABLE STUDY_PARTICIPANT_CRF_ITEMS ADD CONSTRAINT fk_spci_par_crf FOREIGN KEY (study_participant_crf_id) REFERENCES STUDY_PARTICIPANT_CRFS')
		execute('ALTER TABLE STUDY_PARTICIPANT_CRF_ITEMS ADD CONSTRAINT fk_spci_pcvvi FOREIGN KEY (pro_ctc_valid_value_id) REFERENCES pro_ctc_valid_values')
		execute('ALTER TABLE STUDY_PARTICIPANT_CRF_ITEMS ADD CONSTRAINT fk_spc_crf_item FOREIGN KEY (crf_item_id) REFERENCES CRF_PAGE_ITEMS')
	}

	void down() {
		dropTable("STUDY_PARTICIPANT_CRF_ITEMS")
	}
}
