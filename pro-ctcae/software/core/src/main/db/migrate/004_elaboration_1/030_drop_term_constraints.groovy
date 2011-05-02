class StudyParticipantReportingModeHistory extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
        
     	execute('ALTER TABLE pro_ctc_terms ALTER COLUMN term DROP NOT NULL')
     	execute('ALTER TABLE ctc_terms ALTER COLUMN term DROP NOT NULL')
    }
    

    void down() {
    }

}