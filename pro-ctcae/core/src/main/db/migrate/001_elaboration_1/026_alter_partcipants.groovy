class AlterParticipants extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
	addColumn('participants','mrn_identifier' , 'varchar' , nullable:false);    }
    void down() {
    }
}
