class CreateStudyOrganizations extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
        createTable('study_organizations') { t ->
        	t.addColumn('organization_id', 'integer', nullable:false)
        	t.addColumn('study_id', 'integer', nullable:false)
        	t.addColumn('type', 'string',nullable:false);

	    	t.addVersionColumn()
        }
    }

    void down() {
        dropTable('study_organizations')
    }
}