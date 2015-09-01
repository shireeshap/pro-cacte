import java.lang.Exception;

class UpdateCtcTermMapping extends edu.northwestern.bioinformatics.bering.Migration {
	void up(){
		// Point the proTerms to new ctcTerms
        execute("update pro_ctc_terms set ctc_term_id = 5091 where id = 66");
		execute("update pro_ctc_terms set ctc_term_id = 5090 where id = 7");   
		
		// update currency of meddra_llt corresponding to ctcTerms, to 'Y'
		execute("update meddra_llt set currency = 'Y' where meddra_code = '10005265'");
		execute("update meddra_llt set currency = 'Y' where meddra_code = '10008417'");
	}
	
	void down(){		
	}
}