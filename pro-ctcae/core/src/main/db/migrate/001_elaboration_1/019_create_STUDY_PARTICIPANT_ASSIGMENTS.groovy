class StudyParticipantAssignments extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
        createTable("STUDY_PARTICIPANT_ASSIGNMENTS") {t ->
            t.addVersionColumn()
            t.addColumn('study_site_id', 'integer', nullable: false)
            t.addColumn('participant_id', 'integer', nullable: false)
        }
        execute('ALTER TABLE STUDY_PARTICIPANT_ASSIGNMENTS ADD CONSTRAINT fk_spa_study_site FOREIGN KEY (study_site_id) REFERENCES STUDY_ORGANIZATIONS')
        execute('ALTER TABLE STUDY_PARTICIPANT_ASSIGNMENTS ADD CONSTRAINT fk_spa_participant FOREIGN KEY (participant_id) REFERENCES PARTICIPANTS')
    }

    void down() {
        dropTable("STUDY_CRF")
    }
}
