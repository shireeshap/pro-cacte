import edu.northwestern.bioinformatics.bering.Migration

class DeleteProCtcTerm extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    execute("delete from question_display_rules where pro_ctc_question_id in (select id from pro_ctc_questions where pro_ctc_term_id in (select id from pro_ctc_terms where term = 'Tremors'))")
    execute("delete from pro_ctc_valid_values where pro_ctc_question_id in (select id from pro_ctc_questions where pro_ctc_term_id in (select id from pro_ctc_terms where term = 'Tremors'))")
    execute("delete from pro_ctc_questions where pro_ctc_term_id in (select id from pro_ctc_terms where term = 'Tremors')")
    execute("delete from pro_ctc_terms where term = 'Tremors'")
  }

  void down() {

  }
}