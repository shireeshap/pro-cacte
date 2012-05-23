 class AddOrganization extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
        insert('organizations', [id: 108017, nci_institute_code: "MA185", name: "Dana-Farber/Brigham and Women's Cancer Center at South Shore"], primaryKey: false)
        }
        void down() {

	}
}

