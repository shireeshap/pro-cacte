class AddVerbatimColumn extends edu.northwestern.bioinformatics.bering.Migration {

     void up() {
		 addColumn("study_participant_crf_grades", 'proctcae_verbatim', 'string', nullable: true);
		 addColumn("proctcae_grade_mapping", 'proctcae_verbatim', 'string', nullable: true);
     }
    
      void down() {
      }
}