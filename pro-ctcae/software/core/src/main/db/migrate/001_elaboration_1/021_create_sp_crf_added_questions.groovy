class CreateAddedItems extends edu.northwestern.bioinformatics.bering.Migration {
	     void up() {
     		createTable("SP_CRF_ADDED_QUESTIONS") {t ->
     			t.addVersionColumn()
     			t.addColumn('sp_crf_id', 'integer', nullable: false)
     			t.addColumn('question_id', 'integer', nullable: true)
    			t.addColumn('page_number', 'integer', nullable: true)
     		}
     		execute('ALTER TABLE SP_CRF_ADDED_QUESTIONS ADD CONSTRAINT fk_spcaq_sp_crfs FOREIGN KEY (sp_crf_id) REFERENCES STUDY_PARTICIPANT_CRFS')
     		execute('ALTER TABLE SP_CRF_ADDED_QUESTIONS ADD CONSTRAINT fk_spcaq_pc_questions FOREIGN KEY (question_id) REFERENCES PRO_CTC_QUESTIONS')

     		createTable("SP_CRF_SCH_ADDED_QUESTIONS") {t ->
     			t.addVersionColumn()
     			t.addColumn('sp_crf_schedule_id', 'integer', nullable: false)
     			t.addColumn('pro_ctc_valid_value_id', 'integer', nullable: true)
 			    t.addColumn('question_id', 'integer', nullable: true)
                t.addColumn('page_number', 'integer', nullable: true)
                t.addColumn('spc_added_question_id', 'integer', nullable: true)
     		}
     		execute('ALTER TABLE SP_CRF_SCH_ADDED_QUESTIONS ADD CONSTRAINT fk_spcsaq_pcvv FOREIGN KEY (pro_ctc_valid_value_id) REFERENCES PRO_CTC_VALID_VALUES')
     		execute('ALTER TABLE SP_CRF_SCH_ADDED_QUESTIONS ADD CONSTRAINT fk_spcsaq_pc_questions FOREIGN KEY (question_id) REFERENCES PRO_CTC_QUESTIONS')

     	}

     	void down() {
     		dropTable("SP_CRF_ADDED_QUESTIONS")
     		dropTable("SP_CRF_SCH_ADDED_QUESTIONS")
     	}

}