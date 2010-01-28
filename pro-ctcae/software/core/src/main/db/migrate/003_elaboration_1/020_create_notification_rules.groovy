class CreateNotificationRulesTable extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
        createTable("NOTIFICATION_RULES") { t ->
            t.addColumn("title", "string", nullable: false)
        }

        createTable("SITE_CRF_NOTIFICATION_RULES") { t ->
            t.addColumn("display_order", "integer", nullable: false)
            t.addColumn("study_site_id", "integer", nullable: false)
            t.addColumn("crf_id", "integer", nullable: false)
            t.addColumn("notification_rule_id", "integer", nullable: false)
        }
        execute('ALTER TABLE SITE_CRF_NOTIFICATION_RULES ADD CONSTRAINT fk_snr_nr FOREIGN KEY (notification_rule_id) REFERENCES NOTIFICATION_RULES')
        execute('ALTER TABLE SITE_CRF_NOTIFICATION_RULES ADD CONSTRAINT fk_snr_ss FOREIGN KEY (study_site_id) REFERENCES STUDY_ORGANIZATIONS')
        execute('ALTER TABLE SITE_CRF_NOTIFICATION_RULES ADD CONSTRAINT fk_snr_crf FOREIGN KEY (crf_id) REFERENCES CRFS')

        createTable("CRF_NOTIFICATION_RULES") { t ->
            t.addColumn("display_order", "integer", nullable: false)
            t.addColumn("crf_id", "integer", nullable: false)
            t.addColumn("notification_rule_id", "integer", nullable: false)
        }
        execute('ALTER TABLE CRF_NOTIFICATION_RULES ADD CONSTRAINT fk_cnr_nr FOREIGN KEY (notification_rule_id) REFERENCES NOTIFICATION_RULES')
        execute('ALTER TABLE CRF_NOTIFICATION_RULES ADD CONSTRAINT fk_snr_crf FOREIGN KEY (crf_id) REFERENCES CRFS')

        createTable("NOTIFICATION_RULE_CONDITIONS") { t ->
            t.addColumn("notification_rule_id", "integer", nullable: false)
            t.addColumn("operator", "string", nullable: false)
            t.addColumn("question_type", "string", nullable: false)
            t.addColumn("threshold", "integer", nullable: false)
        }
        execute('ALTER TABLE NOTIFICATION_RULE_CONDITIONS ADD CONSTRAINT fk_nrc_nr FOREIGN KEY (notification_rule_id) REFERENCES NOTIFICATION_RULES')

        createTable("NOTIFICATION_RULE_SYMPTOMS") { t ->
            t.addColumn("notification_rule_id", "integer", nullable: false)
            t.addColumn("pro_ctc_term_id", "integer", nullable: false)
        }
        execute('ALTER TABLE NOTIFICATION_RULE_SYMPTOMS ADD CONSTRAINT fk_nrs_nr FOREIGN KEY (notification_rule_id) REFERENCES NOTIFICATION_RULES')
        execute('ALTER TABLE NOTIFICATION_RULE_SYMPTOMS ADD CONSTRAINT fk_snr_pctc FOREIGN KEY (pro_ctc_term_id) REFERENCES PRO_CTC_TERMS')
        createTable("NOTIFICATION_RULE_ROLES") { t ->
                    t.addColumn("notification_rule_id", "integer", nullable: false)
                    t.addColumn("role", "string", nullable: false)
                }
                execute('ALTER TABLE NOTIFICATION_RULE_ROLES ADD CONSTRAINT fk_nrr_nr FOREIGN KEY (notification_rule_id) REFERENCES NOTIFICATION_RULES')
        

    }

    void down() {
        dropTable("notifications")
        dropTable("user_notifications")
    }
}