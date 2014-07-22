class CreateGradeMappingVersion extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		execute("insert into proctcae_grade_mapping_versions (version, name) values ('v4.0', 'PRO-CTCAE-GradeMapping_v1.4_04.11.14.csv')");
 	}
 		void down() {
	}
}