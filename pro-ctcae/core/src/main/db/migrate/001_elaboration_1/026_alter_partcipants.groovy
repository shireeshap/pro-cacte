class AlterParticipants extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
	execute('delete from participants')
	addColumn('participants','mrn_identifier' , 'string' , nullable:false);    }
    void down() {
    }
}
