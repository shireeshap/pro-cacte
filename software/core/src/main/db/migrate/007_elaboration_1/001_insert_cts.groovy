import java.lang.Exception;

class InsertIntoCategoryTermSet extends edu.northwestern.bioinformatics.bering.Migration {
	void up(){
		execute("insert into category_term_set (ctc_term_id, category_id) values (5058, 402)");
		execute("insert into category_term_set (ctc_term_id, category_id) values (5044, 405)");
	}
	
	void down(){
		execute("delete from category_term_set where ctc_term_id = 5058");
		execute("delete from category_term_set where ctc_term_id = 5044");
	}
}