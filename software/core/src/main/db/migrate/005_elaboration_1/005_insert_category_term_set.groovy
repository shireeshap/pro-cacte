class InsertCategoryTermSet extends edu.northwestern.bioinformatics.bering.Migration {
        void up() {
                    execute('insert into category_term_set (ctc_term_id, category_id) (select b.id, a.id from ctc_categories a, ctc_terms b where a.id = b.category_id)')
            }
        void down() {
            }
}