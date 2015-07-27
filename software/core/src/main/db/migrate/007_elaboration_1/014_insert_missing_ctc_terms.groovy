import java.lang.Exception;

class InsertMissingCtcTerms extends edu.northwestern.bioinformatics.bering.Migration {
	void up(){

        insert('ctc_terms', [id: 5090, category_id: 405, ctep_term: "", ctep_code: "10005265", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [id: 5091, category_id: 402, ctep_term: "", ctep_code: "10008417", other_required: "1"], primaryKey: false)

        insert('ctc_terms_vocab', [id: 5090, ctc_terms_id: 5090, term_english: "Bloating"])
        insert('ctc_terms_vocab', [id: 5091, ctc_terms_id: 5091, term_english: "Cheilitis"])

        execute("insert into category_term_set (ctc_term_id, category_id) values (5090, 405)");
        execute("insert into category_term_set (ctc_term_id, category_id) values (5091, 402)");
        
	}
	
	void down(){
		execute("delete from category_term_set where ctc_term_id = 5090");
		execute("delete from category_term_set where ctc_term_id = 5091");
		
        execute("delete from ctc_terms where id = 5090");
        execute("delete from ctc_terms where id = 5091");
        
        execute("delete from ctc_terms_vocab where id = 5090");
        execute("delete from ctc_terms_vocab where id = 5091");
	}
}