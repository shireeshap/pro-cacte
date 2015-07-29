class AddStudyParticipantAssignmentAccessColumn extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
         insert('organizations', [id: 108016, nci_institute_code: "RTOG", name: "Radiation Therapy Oncology Group"], primaryKey: false)
    }

    void down() {
	     execute("delete from organizations where id = 108016")
    }

}		