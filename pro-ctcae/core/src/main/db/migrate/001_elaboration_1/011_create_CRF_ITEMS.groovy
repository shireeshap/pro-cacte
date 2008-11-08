class CreateCrfItem extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    createTable("CRF_ITEMS") {t ->
      t.addColumn('crf_id', 'integer', nullable: false)
      t.addColumn('pro_ctc_term_id', 'integer', nullable: false)
      t.addColumn('display_order', 'integer', nullable: false)
    }
    execute('ALTER TABLE CRF_ITEMS ADD CONSTRAINT fk_crf_items_crf FOREIGN KEY (crf_id) REFERENCES CRFS')
    execute('ALTER TABLE CRF_ITEMS ADD CONSTRAINT fk_crf_items_pro FOREIGN KEY (pro_ctc_term_id) REFERENCES PRO_CTC_TERMS')
    execute("ALTER TABLE CRF_ITEMS ADD CONSTRAINT un_crf_terms UNIQUE (crf_id,pro_ctc_term_id)")
  }

  void down() {
    dropTable("CRF_ITEMS")
  }
}