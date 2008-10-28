class CreateStudyInvestigators extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		execute('delete from study_participant_assignments')
		addColumn('study_participant_assignments','study_participant_identifier' , 'string' , nullable:false);
        }

	void down() {

	}
}