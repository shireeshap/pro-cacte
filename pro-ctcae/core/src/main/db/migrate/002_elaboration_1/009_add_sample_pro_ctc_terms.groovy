class PopulateSites extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
        // Have to break up the inserts so as not to exceed the java max method length
        m0()
    }

    void m0() {

     
   insert('PRO_CTC_QUESTIONS', [ id: 5, question_text: "Describe how fatigued you are feeling RIGHT NOW?" ,pro_ctc_term_id:3016], primaryKey: false)
       insert('PRO_CTC_VALID_VALUES', [ id: 21, value: "Not at all",pro_ctc_question_id:5], primaryKey: false)
       insert('PRO_CTC_VALID_VALUES', [ id: 22, value: "A Little",pro_ctc_question_id:5], primaryKey: false)
       insert('PRO_CTC_VALID_VALUES', [ id: 23, value: "Moderate",pro_ctc_question_id:5], primaryKey: false)
       insert('PRO_CTC_VALID_VALUES', [ id: 24, value: "Quite a bit",pro_ctc_question_id:5], primaryKey: false)
       insert('PRO_CTC_VALID_VALUES', [ id: 25, value: "Extremely",pro_ctc_question_id:5], primaryKey: false)

    insert('PRO_CTC_QUESTIONS', [ id: 6, question_text: "Describe the worst constipation you have experienced since your last treatment?",pro_ctc_term_id:3016], primaryKey: false)
    	insert('PRO_CTC_VALID_VALUES', [ id: 26, value: "Not at all",pro_ctc_question_id:6], primaryKey: false)
    	insert('PRO_CTC_VALID_VALUES', [ id: 27, value: "A Little",pro_ctc_question_id:6], primaryKey: false)
    	insert('PRO_CTC_VALID_VALUES', [ id: 28, value: "Moderate",pro_ctc_question_id:6], primaryKey: false)
    	insert('PRO_CTC_VALID_VALUES', [ id: 29, value: "Quite a bit",pro_ctc_question_id:6], primaryKey: false)
    	insert('PRO_CTC_VALID_VALUES', [ id: 30, value: "Extremely",pro_ctc_question_id:6], primaryKey: false)


    }

    void down() {
        execute("DELETE FROM PRO_CTC_TERMS")
    }
}
