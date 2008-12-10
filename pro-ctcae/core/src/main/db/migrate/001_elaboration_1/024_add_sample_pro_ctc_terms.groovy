class AddSampleProCtcTerms extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		// Have to break up the inserts so as not to exceed the java max method length
		m0()
	}

	void m0() {

		insert('PRO_CTC_TERMS', [id: 2, question_text: "Describe the worst constipation you have experienced since your last treatment?", pro_ctc_id: 1, ctc_term_id: 3001], primaryKey: false)

		insert('PRO_CTC_TERMS', [id: 3, question_text: "Describe the worst nausea you have felt since your last treatment?", pro_ctc_id: 1, ctc_term_id: 3001], primaryKey: false)

		insert('PRO_CTC_TERMS', [id: 4, question_text: "How would you rate your pain on average?", pro_ctc_id: 1, ctc_term_id: 3001], primaryKey: false)
	}

	void down() {
		execute("DELETE FROM PRO_CTC_TERMS")
	}
}
