class InsertCtcTerms extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {

        insert('ctc_terms', [category_id: 411, id: 5079, term: "Headache", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)

        execute("update ctc_terms set ctep_code = '10019211' where term='Headache' and id > 5000")

        }

        void down() {
		execute("DELETE FROM ctc_terms WHERE category_id > 300 AND category_id < 400")
		execute("DELETE FROM ctc_categories WHERE version_id=3")
	}
}