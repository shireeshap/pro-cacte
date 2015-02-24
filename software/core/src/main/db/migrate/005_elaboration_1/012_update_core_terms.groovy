class UpdateCoreTerms extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    execute("update pro_ctc_terms set core = false from ctc_terms_vocab ctv where ctv.term_english in ('Mucositis oral','Rash maculo-papular') and ctv.ctc_terms_id = ctc_term_id");
  }

  void down() {
  }
}