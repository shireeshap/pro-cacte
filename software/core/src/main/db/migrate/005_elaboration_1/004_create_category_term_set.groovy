class CreateCategoryTermSet extends edu.northwestern.bioinformatics.bering.Migration {
 void up() {
   createTable("CATEGORY_TERM_SET") {t ->
     t.addVersionColumn()
     t.addColumn("ctc_term_id", "integer", nullable: false)
     t.addColumn("category_id", "integer", nullable: false)
   }
   execute('ALTER TABLE CATEGORY_TERM_SET ADD CONSTRAINT fk_cts_ctc FOREIGN KEY (ctc_term_id) REFERENCES CTC_TERMS')
   execute('ALTER TABLE CATEGORY_TERM_SET ADD CONSTRAINT fk_cts_cat FOREIGN KEY (category_id) REFERENCES CTC_CATEGORIES')
 }

  void down() {
    dropTable("CATEGORY_TERM_SET")
  }
}