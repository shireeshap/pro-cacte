class StudyParticipantReportingModeHistory extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
        
     	execute('ALTER TABLE pro_ctc_terms ALTER COLUMN term DROP NOT NULL')
     	execute('ALTER TABLE ctc_terms ALTER COLUMN term DROP NOT NULL')
     	execute('ALTER TABLE meddra_llt ALTER COLUMN meddra_term DROP NOT NULL')
     	execute('ALTER TABLE pro_ctc_questions ALTER COLUMN question_text DROP NOT NULL')
     	execute('ALTER TABLE meddra_questions ALTER COLUMN question_text DROP NOT NULL')
     	execute('ALTER TABLE meddra_valid_values ALTER COLUMN value DROP NOT NULL')
     	execute('ALTER TABLE pro_ctc_valid_values ALTER COLUMN value DROP NOT NULL')

    }
    

    void down() {
    }

}