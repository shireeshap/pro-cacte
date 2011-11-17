import edu.northwestern.bioinformatics.bering.Migration

class IndexForeignKeys extends edu.northwestern.bioinformatics.bering.Migration {
        void up() {
                if (databaseMatches('postgres')) {
                execute('CREATE INDEX "arms_study_id_idx" ON arms(study_id)')                
                execute('CREATE INDEX "cs_user_id_idx" ON clinical_staffs(user_id)')
                execute('CREATE INDEX "cc_fas_id_idx" ON crf_calendars(form_arm_schedules_id)')
                execute('CREATE INDEX "ccd_arm_sch_id_idx" ON crf_cycle_definitions(form_arm_schedules_id)')
                execute('CREATE INDEX "cy_def_id_idx" ON crf_cycles(cycle_definition_id)')
                execute('CREATE INDEX "cnr_nr_id_idx" ON crf_notification_rules(notification_rule_id)')
                execute('CREATE INDEX "cnr_crf_id_idx" ON crf_notification_rules(crf_id)')
                execute('CREATE INDEX "cpidr_crf_id_idx" ON crf_page_item_display_rules(crf_item_id)')
                execute('CREATE INDEX "cpidr_pvv_id_idx" ON crf_page_item_display_rules(pro_ctc_valid_value_id)')
                execute('CREATE INDEX "cpi_pcq_id_idx" ON crf_page_items(pro_ctc_question_id)')
                execute('CREATE INDEX "cpi_cp_id_idx" ON crf_page_items(crf_page_id)')
                execute('CREATE INDEX "crf_study_id_idx" ON crfs(study_id)')
                execute('CREATE INDEX "crf_nv_id_idx" ON crfs(next_version_id)')
                execute('CREATE INDEX "crf_pv_id_idx" ON crfs(parent_version_id)')
                execute('CREATE INDEX "ctc_cat_id_idx" ON ctc_terms(category_id)')
                execute('CREATE INDEX "fas_arm_idx" ON form_arm_schedules(arm_id)')
                execute('CREATE INDEX "fas_form_idx" ON form_arm_schedules(form_id)')
                execute('CREATE INDEX "nrc_nr_idx" ON notification_rule_conditions(notification_rule_id)')
                execute('CREATE INDEX "nrr_nr_idx" ON notification_rule_roles(notification_rule_id)')
                execute('CREATE INDEX "nrs_nr_idx" ON notification_rule_symptoms(notification_rule_id)')
                execute('CREATE INDEX "nrs_pr_term_idx" ON notification_rule_symptoms(pro_ctc_term_id)')
                execute('CREATE INDEX "ocs_cs_idx" ON organization_clinical_staffs(clinical_staff_id)')
                execute('CREATE INDEX "part_user_idx" ON participants(user_id)')
                execute('CREATE INDEX "pq_pct_idx" ON pro_ctc_questions(pro_ctc_term_id)')
                execute('CREATE INDEX "pt_ct_idx" ON pro_ctc_terms(ctc_term_id)')
                execute('CREATE INDEX "pt_pro_idx" ON pro_ctc_terms(pro_ctc_id)')
                execute('CREATE INDEX "pro_vv_idx" ON pro_ctc_valid_values(pro_ctc_question_id)')
                execute('CREATE INDEX "qdr_pq_idx" ON question_display_rules(pro_ctc_question_id)')
                execute('CREATE INDEX "qdr_pvv_idx" ON question_display_rules(pro_ctc_valid_value_id)')
                execute('CREATE INDEX "rp_priv_idx" ON role_privileges(privilege_id)')
                execute('CREATE INDEX "scn_crf_idx" ON site_crf_notification_rules(crf_id)')
                execute('CREATE INDEX "scn_nr_idx" ON site_crf_notification_rules(notification_rule_id)')
                execute('CREATE INDEX "scn_ss_idx" ON site_crf_notification_rules(study_site_id)')
                execute('CREATE INDEX "spcs_spc_idx" ON sp_crf_schedules(study_participant_crf_id)')
                execute('CREATE INDEX "sm_st_idx" ON study_modes(study_id)')
                execute('CREATE INDEX "socs_ocs_idx" ON study_org_clinical_staffs(organization_clinical_staff_id)')
                execute('CREATE INDEX "so_study_idx" ON study_organizations(study_id)')
                execute('CREATE INDEX "stu_par_cs_spa_idx" ON study_par_clinical_staffs(sp_assignment_id)')
                execute('CREATE INDEX "stu_par_cs_socs_idx" ON study_par_clinical_staffs(so_clinical_staff_id)')
                execute('CREATE INDEX "spa_arm_idx" ON study_participant_assignments(arm_id)')
                execute('CREATE INDEX "spa_part_idx" ON study_participant_assignments(participant_id)')
                execute('CREATE INDEX "spa_ss_idx" ON study_participant_assignments(participant_id)')
                execute('CREATE INDEX "spci_ci_idx" ON study_participant_crf_items(crf_item_id)')
                execute('CREATE INDEX "spci_pvv_idx" ON study_participant_crf_items(pro_ctc_valid_value_id)')
                execute('CREATE INDEX "spc_sp_idx" ON study_participant_crfs(study_participant_id)')
                execute('CREATE INDEX "spc_crf_idx" ON study_participant_crfs(crf_id)')
                execute('CREATE INDEX "spm_spa_idx" ON study_participant_modes(spa_id)')
                execute('CREATE INDEX "un_noti_idx" ON user_notifications(notification_id)')
                execute('CREATE INDEX "un_part_idx" ON user_notifications(participant_id)')
                execute('CREATE INDEX "un_spc_idx" ON user_notifications(spc_schedule_id)')
                execute('CREATE INDEX "un_study_idx" ON user_notifications(study_id)')
                execute('CREATE INDEX "un_user_idx" ON user_notifications(user_id)')
                execute('CREATE INDEX "ur_user_idx" ON user_roles(user_id)')

              }
            }
        void down() {
            }
}
