class PopulateSites extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
        // Have to break up the inserts so as not to exceed the java max method length
        m0()
    }

    void m0() {

	insert('PRO_CTC_TERMS', [ id: 2, question_text: "Describe the worst constipation you have experienced since your last treatment?",pro_ctc_id:1,ctc_term_id:3001], primaryKey: false)
	insert('PRO_CTC_VALID_VALUES', [ id: 1, value: "Not at all",pro_ctc_term_id:2], primaryKey: false)
	insert('PRO_CTC_VALID_VALUES', [ id: 2, value: "A Little",pro_ctc_term_id:2], primaryKey: false)
	insert('PRO_CTC_VALID_VALUES', [ id: 3, value: "Moderate",pro_ctc_term_id:2], primaryKey: false)
	insert('PRO_CTC_VALID_VALUES', [ id: 4, value: "Quite a bit",pro_ctc_term_id:2], primaryKey: false)
	insert('PRO_CTC_VALID_VALUES', [ id: 5, value: "Extremely",pro_ctc_term_id:2], primaryKey: false)

	insert('PRO_CTC_TERMS', [ id: 3, question_text: "Describe the worst nausea you have felt since your last treatment?" ,pro_ctc_id:1,ctc_term_id:3001], primaryKey: false)
        insert('PRO_CTC_VALID_VALUES', [ id: 1, value: "Not at all",pro_ctc_term_id:3], primaryKey: false)
	insert('PRO_CTC_VALID_VALUES', [ id: 2, value: "A Little",pro_ctc_term_id:3], primaryKey: false)
	insert('PRO_CTC_VALID_VALUES', [ id: 3, value: "Moderate",pro_ctc_term_id:3], primaryKey: false)
	insert('PRO_CTC_VALID_VALUES', [ id: 4, value: "Quite a bit",pro_ctc_term_id:3], primaryKey: false)
	insert('PRO_CTC_VALID_VALUES', [ id: 5, value: "Extremely",pro_ctc_term_id:3], primaryKey: false)

	insert('PRO_CTC_TERMS', [ id: 4, question_text: "How would you rate your pain on average?" ,pro_ctc_id:1,ctc_term_id:3001], primaryKey: false)
	insert('PRO_CTC_VALID_VALUES', [ id: 1, value: "Not at all",pro_ctc_term_id:4], primaryKey: false)
	insert('PRO_CTC_VALID_VALUES', [ id: 2, value: "A Little",pro_ctc_term_id:4], primaryKey: false)
	insert('PRO_CTC_VALID_VALUES', [ id: 3, value: "Moderate",pro_ctc_term_id:4], primaryKey: false)
	insert('PRO_CTC_VALID_VALUES', [ id: 4, value: "Quite a bit",pro_ctc_term_id:4], primaryKey: false)
	insert('PRO_CTC_VALID_VALUES', [ id: 5, value: "Extremely",pro_ctc_term_id:4], primaryKey: false)
    }

    void down() {
        execute("DELETE FROM PRO_CTC_TERMS")
    }
}
