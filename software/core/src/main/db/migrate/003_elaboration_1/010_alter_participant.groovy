class AlterParticipants extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {

            if (databaseMatches('oracle')) {

            	execute('ALTER TABLE participants MODIFY mrn_identifier VARCHAR2(20) NULL')
            } else {
            	execute('ALTER TABLE participants ALTER mrn_identifier DROP NOT NULL')
            }
    }

    void down() {

    }
}