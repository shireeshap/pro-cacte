class AddMoreQuestions extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    // Have to break up the inserts so as not to exceed the java max method length
    m0()
  }

  void m0() {

    insert('PRO_CTC_TERMS', [id: 1, question_text: "Describe how fatigued you are feeling RIGHT NOW?", pro_ctc_id: 1, ctc_term_id: 3001], primaryKey: false)
    insert('PRO_CTC_VALID_VALUES', [id: 1, value: "Not at all", pro_ctc_term_id: 1], primaryKey: false)
    insert('PRO_CTC_VALID_VALUES', [id: 2, value: "A Little", pro_ctc_term_id: 1], primaryKey: false)
    insert('PRO_CTC_VALID_VALUES', [id: 3, value: "Moderate", pro_ctc_term_id: 1], primaryKey: false)
    insert('PRO_CTC_VALID_VALUES', [id: 4, value: "Quite a bit", pro_ctc_term_id: 1], primaryKey: false)
    insert('PRO_CTC_VALID_VALUES', [id: 5, value: "Extremely", pro_ctc_term_id: 1], primaryKey: false)
  }

  void down() {
    execute("DELETE FROM PRO_CTC_TERMS")
  }
}
