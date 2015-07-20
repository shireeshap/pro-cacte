class UpdateCrfPages extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {

        execute("update crf_pages set pro_ctc_term_id = (select pt.id from pro_ctc_terms pt where pt.term=crf_pages.description) where pro_ctc_term_id is null")
        execute("update crf_pages set pro_ctc_term_id = 11 where pro_ctc_term_id is null")
        }
        void down() {

	}
}