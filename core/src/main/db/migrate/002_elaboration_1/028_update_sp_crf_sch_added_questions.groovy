class UpdateSpCrfSchAddedQuestions extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
            addColumn("SP_CRF_SCH_ADDED_QUESTIONS", 'meddra_valid_value_id', 'integer', nullable: true);
            addColumn("SP_CRF_SCH_ADDED_QUESTIONS", 'meddra_question_id', 'integer', nullable: true);

         execute('ALTER TABLE SP_CRF_SCH_ADDED_QUESTIONS ADD CONSTRAINT fk_sp_crf_sch_medd_value FOREIGN KEY (meddra_valid_value_id) REFERENCES MEDDRA_VALID_VALUES')                
         execute('ALTER TABLE SP_CRF_SCH_ADDED_QUESTIONS ADD CONSTRAINT fk_sp_crf_sch_medd_ques FOREIGN KEY (meddra_question_id) REFERENCES MEDDRA_QUESTIONS')

    }

    void down() {
		    dropColumn("SP_CRF_SCH_ADDED_QUESTIONS", 'meddra_valid_value_id')    
    		dropColumn("SP_CRF_SCH_ADDED_QUESTIONS", 'meddra_question_id')

    }

}