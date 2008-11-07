class PopulateSites extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
        // Have to break up the inserts so as not to exceed the java max method length
        m0()
    }

    void m0() {

     execute('update PRO_CTC_QUESTIONS set pro_ctc_term_id=3801 where id =1');
     execute('update PRO_CTC_QUESTIONS set pro_ctc_term_id=3141 where id =2');
     execute('update PRO_CTC_QUESTIONS set pro_ctc_term_id=3207 where id =3');
     execute('update PRO_CTC_QUESTIONS set pro_ctc_term_id=3725 where id =4');

        }

    void down() {
        execute("DELETE FROM PRO_CTC_TERMS")
    }
}
