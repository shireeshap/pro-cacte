class InsertDefaultPasswordPolicy extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
        insert('password_policy', [id: 1, ln_allowed_attempts: 5, ln_lockout_duration: 86400,
                   ln_max_age: 7776000, cn_min_age: 3600, cn_history_size: 3,
                   cn_min_length: 7, cn_cb_min_required: 3, cn_cb_is_upper_case_alphabet: '0',
                   cn_cb_is_lower_case_alphabet: '1', cn_cb_is_non_alpha_numeric: '1',
                   cn_cb_is_base_ten_digit: '1', cn_cb_max_substring_length: 3, role: 'ADMIN'],  primaryKey: false)

        addColumn("password_policy", "version", "integer", defaultValue: 0, nullable: false)
    }

    void down() {
        execute("delete from password_policy where id=1");
    }
}
