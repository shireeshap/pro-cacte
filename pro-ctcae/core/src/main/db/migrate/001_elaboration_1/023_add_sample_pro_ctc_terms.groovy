class AddMoreQuestions extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		// Have to break up the inserts so as not to exceed the java max method length
		m0()
	}

	void m0() {

		insert('PRO_CTC_TERMS', [id: 1, question_text: "Describe how fatigued you are feeling RIGHT NOW?", pro_ctc_id: 1, ctc_term_id: 3001], primaryKey: false)
	}

	void down() {
		execute("DELETE FROM PRO_CTC_TERMS")
	}
}
