class CreateProCtcGradeMapping extends edu.northwestern.bioinformatics.bering.Migration {

     void up() {
         createTable("PROCTCAE_GRADE_MAPPING") {t ->
             t.addColumn("proctcae_grade_mapping_version_id", "integer", nullable: false)
             t.addColumn("pro_ctc_terms_id", "integer", nullable: false)
             t.addColumn("frequency", "integer", nullable: true)
             t.addColumn("severity", "integer", nullable: true)
             t.addColumn("interference", "integer", nullable: true)
             t.addColumn("amount", "integer", nullable: true)
             t.addColumn("present_absent", "integer", nullable: true)
             t.addColumn("pro_ctc_grade", "string", nullable: false)
         }
         execute('ALTER TABLE PROCTCAE_GRADE_MAPPING ADD CONSTRAINT fk_pctcgm_pctcterms FOREIGN KEY (pro_ctc_terms_id) REFERENCES pro_ctc_terms')
         execute('ALTER TABLE PROCTCAE_GRADE_MAPPING ADD CONSTRAINT fk_pctcgm_gmversions FOREIGN KEY (proctcae_grade_mapping_version_id) REFERENCES proctcae_grade_mapping_versions')
       
     }
    
      void down() {
         dropTable("PRO_CTC_GRADE_MAPPING")
      }
}