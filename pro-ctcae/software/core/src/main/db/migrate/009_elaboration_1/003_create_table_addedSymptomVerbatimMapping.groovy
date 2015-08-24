import edu.northwestern.bioinformatics.bering.Migration

class CreateAddedSymptomsVerbatimMapping extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    createTable("added_symptoms_verbatim_mapping") {t ->
      t.addVersionColumn()
      t.addColumn('pro_ctc_term_id', 'integer', nullable: true)
      t.addColumn('meddra_llt_id', 'integer', nullable: true)
      t.addColumn('sp_crf_schedules_id', 'integer', nullable: false)
      t.addColumn('verbatim', 'string', nullable: true)
    }
    execute('ALTER TABLE ADDED_SYMPTOMS_VERBATIM_MAPPING ADD CONSTRAINT fk_addedSymptomVerbatimMapping_proctc_term_id FOREIGN KEY (pro_ctc_term_id) REFERENCES PRO_CTC_TERMS')
    execute('ALTER TABLE ADDED_SYMPTOMS_VERBATIM_MAPPING ADD CONSTRAINT fk_addedSymptomVerbatimMapping_mllt_id FOREIGN KEY (meddra_llt_id) REFERENCES MEDDRA_LLT') 
    execute('ALTER TABLE ADDED_SYMPTOMS_VERBATIM_MAPPING ADD CONSTRAINT fk_addedSymptomVerbatimMapping_schedule_id FOREIGN KEY (sp_crf_schedules_id) REFERENCES SP_CRF_SCHEDULES')
  }
  
  void down() {
    dropTable("added_symptoms_verbatim_mapping")
  }

}