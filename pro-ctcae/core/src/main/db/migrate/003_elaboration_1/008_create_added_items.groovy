class CreateAddedItems extends edu.northwestern.bioinformatics.bering.Migration {
	     void up() {
     		createTable("sp_crf_added_questions") {t ->
     			t.addVersionColumn()
     			t.addColumn('sp_crf_id', 'integer', nullable: false)
     			t.addColumn('question_id', 'integer', nullable: false)

     		}
     		execute('ALTER TABLE sp_crf_added_questions ADD CONSTRAINT fk_spcaq_sp_crfs FOREIGN KEY (sp_crf_id) REFERENCES study_participant_crfs')
     		execute('ALTER TABLE sp_crf_added_questions ADD CONSTRAINT fk_spcaq_pc_questions FOREIGN KEY (question_id) REFERENCES pro_ctc_questions')

     		createTable("sp_crf_sch_added_questions") {t ->
     			t.addVersionColumn()
     			t.addColumn('sp_crf_schedule_id', 'integer', nullable: false)
     			t.addColumn('pro_ctc_valid_value_id', 'integer', nullable: true)

     		}
     		execute('ALTER TABLE sp_crf_sch_added_questions ADD CONSTRAINT fk_spcsaq_sp_crf_sch FOREIGN KEY (sp_crf_schedule_id) REFERENCES sp_crf_schedules')
     		execute('ALTER TABLE sp_crf_sch_added_questions ADD CONSTRAINT fk_spcsaq_pcvv FOREIGN KEY (pro_ctc_valid_value_id) REFERENCES pro_ctc_valid_values')

     	}

     	void down() {
     		dropTable("sp_crf_added_questions")
     		dropTable("sp_crf_sch_added_questions")
     	}

}