class UpdateSpCrfAddedQuestions extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
            addColumn("SP_CRF_ADDED_QUESTIONS", 'meddra_question_id', 'integer', nullable: true);
    execute('ALTER TABLE SP_CRF_ADDED_QUESTIONS ADD CONSTRAINT fk_sp_crf_medd_ques FOREIGN KEY (meddra_question_id) REFERENCES MEDDRA_QUESTIONS')
    
    }

    void down() {
    		dropColumn("SP_CRF_ADDED_QUESTIONS", 'meddra_question_id')
    }

}