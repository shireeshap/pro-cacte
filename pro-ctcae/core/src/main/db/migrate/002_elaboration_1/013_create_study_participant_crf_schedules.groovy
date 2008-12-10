class CreateStudyParticipantCrfSchedules extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		createTable("SP_CRF_SCHEDULES") {t ->
			t.addColumn('study_participant_crf_id', 'integer', nullable: false)
			t.addColumn('start_date', 'date', nullable: true)
			t.addColumn('due_date', 'date', nullable: true)
			t.addColumn('status', 'string', nullable: true)
		}
		execute('ALTER TABLE SP_CRF_SCHEDULES ADD CONSTRAINT fk_spcs_spc FOREIGN KEY (study_participant_crf_id) REFERENCES STUDY_PARTICIPANT_CRFS')
		execute('DELETE  FROM STUDY_PARTICIPANT_CRF_ITEMS')
		execute('ALTER TABLE STUDY_PARTICIPANT_CRF_ITEMS DROP COLUMN study_participant_crf_id')
		addColumn('STUDY_PARTICIPANT_CRF_ITEMS', 'SP_CRF_SCHEDULE_ID', 'integer', nullable: false)
		execute('ALTER TABLE STUDY_PARTICIPANT_CRF_ITEMS ADD CONSTRAINT fk_spci_spcs FOREIGN KEY (SP_CRF_SCHEDULE_ID) REFERENCES SP_CRF_SCHEDULES')
	}

	void down() {
		dropTable("SP_CRF_SCHEDULES")
	}
}