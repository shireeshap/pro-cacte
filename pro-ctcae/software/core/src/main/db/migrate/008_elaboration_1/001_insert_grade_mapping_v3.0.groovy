class CreateGradeMappingVersion extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		execute("insert into proctcae_grade_mapping_versions (version, name) values ('v3.0', 'PRO-CTCAE-GradeMapping_v1.3_03.11.14.csv')");
 	}
 		void down() {
	}
}