class UpdateStudyMode extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
        execute("update study_modes set mode = 'HOMEWEB' where mode = 'CLINICWEB'")
        }
        void down() {

	}
}