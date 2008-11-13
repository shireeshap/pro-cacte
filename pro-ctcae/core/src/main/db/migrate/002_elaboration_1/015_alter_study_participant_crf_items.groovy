class AddUniqueCrfTitleConstraints extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    execute('delete from study_participant_crf_items')
    dropColumn('study_participant_crf_items','selected_response')
    addColumn('study_participant_crf_items','pro_ctc_valid_value_id','integer',nullable:true)
    execute('ALTER TABLE STUDY_PARTICIPANT_CRF_ITEMS ADD CONSTRAINT fk_spci_pcvvi FOREIGN KEY (pro_ctc_valid_value_id) REFERENCES pro_ctc_valid_values')
  }

  void down() {
  }
}

