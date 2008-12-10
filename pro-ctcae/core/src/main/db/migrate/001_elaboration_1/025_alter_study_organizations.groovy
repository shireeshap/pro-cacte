class AlterStudyOrganizations extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		execute('ALTER TABLE study_organizations ADD CONSTRAINT fk_so_study FOREIGN KEY (study_id) REFERENCES studies (id)    ')
		execute('ALTER TABLE study_organizations ADD CONSTRAINT fk_so_organization FOREIGN KEY (organization_id) REFERENCES organizations (id)  ')
	}

	void down() {
	}
}
