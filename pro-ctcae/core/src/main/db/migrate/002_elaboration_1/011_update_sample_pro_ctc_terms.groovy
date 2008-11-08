class PopulateSites extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    // Have to break up the inserts so as not to exceed the java max method length
    m0()
  }

  void m0() {

    execute('update PRO_CTC_QUESTIONS set pro_ctc_term_id=3081 where id =1');

  }

  void down() {
    execute("DELETE FROM PRO_CTC_TERMS")
  }
}
