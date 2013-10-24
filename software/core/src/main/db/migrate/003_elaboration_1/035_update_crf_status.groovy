import edu.northwestern.bioinformatics.bering.Migration

class DeleteProctcTerm extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
   execute("update crfs set status = 'Final' where status ='Released'")
           execute('delete from password_policy where id =  10');
           insert('password_policy', [id: 10, ln_allowed_attempts: 5, ln_lockout_duration: 86400,
                      ln_max_age: 7776000, cn_min_age: 3600, cn_history_size: 3,
                      cn_min_length: 6, cn_cb_min_required: 3, cn_cb_is_upper_case_alphabet: '0',
                      cn_cb_is_lower_case_alphabet: '1', cn_cb_is_non_alpha_numeric: '0',
                      cn_cb_is_base_ten_digit: '0', cn_cb_max_substring_length: 3, role: 'PARTICIPANT'],  primaryKey: false)

  }

  void down() {

  }
}