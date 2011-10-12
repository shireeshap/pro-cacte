class StudyParticipantReportingModeHistory extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
        dropColumn("ctc_terms", 'term');
        dropColumn("pro_ctc_terms", 'term');
        dropColumn("pro_ctc_questions", 'question_text');
        dropColumn("pro_ctc_valid_values", 'value');
        dropColumn("meddra_llt", 'meddra_term');
        dropColumn("meddra_questions", 'question_text');
        dropColumn("meddra_valid_values", 'value');
    }
    

    void down() {
    	addColumn("ctc_terms", 'term', 'text', nullable: false);
    	addColumn("pro_ctc_terms", 'term', 'text', nullable: false);
    	addColumn("pro_ctc_questions", 'question_text', 'text', nullable: false);
    	addColumn("pro_ctc_valid_values", 'value', 'text', nullable: false);
    	addColumn("meddra_llt", 'meddra_term', 'text', nullable: false);
    	addColumn("meddra_questions", 'question_text', 'text', nullable: false);
    	addColumn("meddra_valid_values", 'value', 'text', nullable: false);
    }

}