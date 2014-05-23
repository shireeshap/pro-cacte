class InsertUserCategory extends edu.northwestern.bioinformatics.bering.Migration {
        void up() {
                    execute("insert into ctc_categories (version_id, name, user_defined) values (4, 'Core terms', true)");
                    execute("insert into category_term_set (ctc_term_id, category_id) (select a.id, b.id from ctc_terms a, ctc_categories b, pro_ctc_terms c where b.name='Core terms' and a.id = c.ctc_term_id and c.core = true )");
            }
        void down() {
            }
}