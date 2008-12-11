class DropQuestions extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {

		execute('delete from study_crfs where crf_id in(select crfs.id from crfs where crfs.id in (select crf_id from crf_items where pro_ctc_question_id in (-3,-2)))')
		execute('delete from  crf_items where pro_ctc_question_id in (-3,-2)')

		execute('delete from  crfs where crfs.id in (select crf_id from crf_items where pro_ctc_question_id in (-3,-2))')

		execute('delete from pro_ctc_valid_values  where pro_ctc_question_id in (-3,-2)')

		execute('delete from pro_ctc_questions where id in (-3,-2)')

	}

	void down() {

	}
}