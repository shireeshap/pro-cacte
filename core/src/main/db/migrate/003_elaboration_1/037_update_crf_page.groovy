class UpdateCrfPages extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
     addColumn("CRF_PAGES", 'pro_ctc_term_id', 'integer');


  }

  void down() {
    dropColumn("CRF_PAGES", 'pro_ctc_term_id')
  }
  
}