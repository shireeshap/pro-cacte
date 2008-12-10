class UpdateProCtcQuestions extends edu.northwestern.bioinformatics.bering.Migration {

	void up() {


		execute("update PRO_CTC_QUESTIONS set question_text='Severity' where id =-4");

		execute("update PRO_CTC_QUESTIONS set pro_ctc_term_id=3778 where id =-4");
		insert('PRO_CTC_QUESTIONS', [id: -5, question_text: "Interference", pro_ctc_term_id: 3778], primaryKey: false);

		execute("update PRO_CTC_QUESTIONS set question_text='Severity' where id =3");
		insert('PRO_CTC_QUESTIONS', [id: -6, question_text: "Interference", pro_ctc_term_id: 3207], primaryKey: false);

		execute("update PRO_CTC_QUESTIONS set question_text='Severity' where id =2");
		execute("update PRO_CTC_QUESTIONS set pro_ctc_term_id=3778 where id =2");

		insert('PRO_CTC_QUESTIONS', [id: -7, question_text: "Interference", pro_ctc_term_id: 3141], primaryKey: false);

		execute("update PRO_CTC_QUESTIONS set question_text='Severity' where id =-1");
		insert('PRO_CTC_QUESTIONS', [id: -8, question_text: "Interference", pro_ctc_term_id: 3081], primaryKey: false);

		insert('PRO_CTC_QUESTIONS', [id: -9, question_text: "Severity", pro_ctc_term_id: 3147], primaryKey: false);
		insert('PRO_CTC_QUESTIONS', [id: -10, question_text: "Interference", pro_ctc_term_id: 3147], primaryKey: false);

		insert('PRO_CTC_QUESTIONS', [id: -11, question_text: "Severity", pro_ctc_term_id: 3277], primaryKey: false);
		insert('PRO_CTC_QUESTIONS', [id: -12, question_text: "Interference", pro_ctc_term_id: 3277], primaryKey: false);

		insert('PRO_CTC_QUESTIONS', [id: -13, question_text: "Severity", pro_ctc_term_id: 3785], primaryKey: false);
		insert('PRO_CTC_QUESTIONS', [id: -14, question_text: "Interference", pro_ctc_term_id: 3785], primaryKey: false);


	}


	void down() {
		execute("DELETE FROM PRO_CTC_TERMS")
	}
}
