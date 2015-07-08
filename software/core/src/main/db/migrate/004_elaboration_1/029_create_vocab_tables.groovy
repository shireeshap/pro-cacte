class StudyParticipantReportingModeHistory extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
        
      createTable("ctc_terms_vocab") {t ->
         t.addVersionColumn()
         t.addColumn('ctc_terms_id', 'integer', nullable:false)
         t.addColumn('term_english', 'string', nullable:true)
         t.addColumn('term_spanish', 'string', nullable:true)
	  }
	  execute('ALTER TABLE ctc_terms_vocab ADD CONSTRAINT fk_ctc_terms_id FOREIGN KEY (ctc_terms_id) REFERENCES ctc_terms')
	  execute('Insert into ctc_terms_vocab(id, ctc_terms_id, term_english) select ctc_terms.id, ctc_terms.id, ctc_terms.term from ctc_terms')
	  
      createTable("pro_ctc_terms_vocab") {t ->
         t.addVersionColumn()
         t.addColumn('pro_ctc_terms_id', 'integer', nullable:false)
         t.addColumn('term_english', 'string', nullable:true)
         t.addColumn('term_spanish', 'string', nullable:true)
	  }
	  execute('ALTER TABLE pro_ctc_terms_vocab ADD CONSTRAINT fk_pro_ctc_terms_id FOREIGN KEY (pro_ctc_terms_id) REFERENCES pro_ctc_terms')
	  execute('Insert into pro_ctc_terms_vocab(id, pro_ctc_terms_id, term_english) select pro_ctc_terms.id, pro_ctc_terms.id, pro_ctc_terms.term from pro_ctc_terms')

      createTable("pro_ctc_questions_vocab") {t ->
         t.addVersionColumn()
         t.addColumn('pro_ctc_questions_id', 'integer', nullable:false)
         t.addColumn('question_text_english', 'string', nullable:true)
         t.addColumn('question_text_spanish', 'string', nullable:true)
	  }
	  execute('ALTER TABLE pro_ctc_questions_vocab ADD CONSTRAINT fk_pro_ctc_questions_id FOREIGN KEY (pro_ctc_questions_id) REFERENCES pro_ctc_questions')
  	  execute('Insert into pro_ctc_questions_vocab(id, pro_ctc_questions_id, question_text_english) select pro_ctc_questions.id, pro_ctc_questions.id, pro_ctc_questions.question_text from pro_ctc_questions')
	  
      createTable("pro_ctc_valid_values_vocab") {t ->
         t.addVersionColumn()
         t.addColumn('pro_ctc_valid_values_id', 'integer', nullable:false)
         t.addColumn('value_english', 'string', nullable:true)
         t.addColumn('value_spanish', 'string', nullable:true)
	  }
	  execute('ALTER TABLE pro_ctc_valid_values_vocab ADD CONSTRAINT fk_pro_ctc_valid_values_id FOREIGN KEY (pro_ctc_valid_values_id) REFERENCES pro_ctc_valid_values')
  	  execute('Insert into pro_ctc_valid_values_vocab(id, pro_ctc_valid_values_id, value_english) select pro_ctc_valid_values.id, pro_ctc_valid_values.id, pro_ctc_valid_values.value from pro_ctc_valid_values')
    
    
      //Meddra tables
      createTable("meddra_llt_vocab") {t ->
         t.addVersionColumn()
         t.addColumn('meddra_llt_id', 'integer', nullable:false)
         t.addColumn('meddra_term_english', 'string', nullable:true)
         t.addColumn('meddra_term_spanish', 'string', nullable:true)
	  }
	  execute('ALTER TABLE meddra_llt_vocab ADD CONSTRAINT fk_meddra_llt_id FOREIGN KEY (meddra_llt_id) REFERENCES meddra_llt')
  	  execute('Insert into meddra_llt_vocab(id, meddra_llt_id, meddra_term_english) select meddra_llt.id, meddra_llt.id, meddra_llt.meddra_term from meddra_llt')
    
  	  createTable("meddra_questions_vocab") {t ->
         t.addVersionColumn()
         t.addColumn('meddra_questions_id', 'integer', nullable:false)
         t.addColumn('question_text_english', 'string', nullable:true)
         t.addColumn('question_text_spanish', 'string', nullable:true)
	  }
	  execute('ALTER TABLE meddra_questions_vocab ADD CONSTRAINT fk_meddra_questions_id FOREIGN KEY (meddra_questions_id) REFERENCES meddra_questions')
  	  execute('Insert into meddra_questions_vocab(id, meddra_questions_id, question_text_english) select meddra_questions.id, meddra_questions.id, meddra_questions.question_text from meddra_questions')
    
      createTable("meddra_valid_values_vocab") {t ->
         t.addVersionColumn()
         t.addColumn('meddra_valid_values_id', 'integer', nullable:false)
         t.addColumn('value_english', 'string', nullable:true)
         t.addColumn('value_spanish', 'string', nullable:true)
	  }
	  execute('ALTER TABLE meddra_valid_values_vocab ADD CONSTRAINT fk_meddra_valid_values_id FOREIGN KEY (meddra_valid_values_id) REFERENCES meddra_valid_values')
  	  execute('Insert into meddra_valid_values_vocab(id, meddra_valid_values_id, value_english) select meddra_valid_values.id, meddra_valid_values.id, meddra_valid_values.value from meddra_valid_values')
	  
    }
    

    void down() {
      dropTable("sp_crf_sch_sympt_records")
      dropTable("ivrs_call_history")
    }

}