class CreateGradeMappingVersion extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		createTable("proctcae_grade_mapping_versions") {t ->
			t.addColumn('version', 'string', nullable:false)
			t.addColumn('name', 'string', nullable: false)
			}
			execute("insert into proctcae_grade_mapping_versions (version, name) values ('v1.0', 'dummy_documentName_placeHolder')");
 	}
 		void down() {
		dropTable("proctcae_grade_mapping_versions")
	}
}