import java.lang.Exception;

class ResetSeqs extends edu.northwestern.bioinformatics.bering.Migration {
	void up(){
        execute("SELECT setval('public.pro_ctc_questions_vocab_id_seq', 126, true)");
		execute("SELECT setval('public.pro_ctc_terms_vocab_id_seq', 81, true)");
	}
	
	void down(){
	}
}