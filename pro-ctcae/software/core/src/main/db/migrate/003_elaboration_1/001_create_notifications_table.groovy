class CreateNotificationsTable extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
        createTable("notifications") { t ->
            t.addVersionColumn()
            t.addColumn("notification_text", "string", nullable: false)
            t.addColumn("notification_date", "timestamp", nullable: false)
        }

        createTable("user_notifications") { t ->
            t.addColumn("notification_id", "integer", nullable: false)
            t.addColumn("user_id", "integer", nullable: false)
            t.addColumn("study_id", "integer", nullable: false)
            t.addColumn("participant_id", "integer", nullable: true)
            t.addColumn("is_new", "boolean", nullable: false)
            t.addColumn("mark_delete", "boolean", nullable: false)
            t.addColumn("uuid", "string", nullable: true)
            t.addColumn("spc_schedule_id", "integer", nullable: true)
            
            t.addVersionColumn()
        }


        execute('ALTER TABLE user_notifications ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES USERS')
        execute('ALTER TABLE user_notifications ADD CONSTRAINT fk_notifications FOREIGN KEY (notification_id) REFERENCES NOTIFICATIONS')
        execute('ALTER TABLE user_notifications ADD CONSTRAINT fk_participant FOREIGN KEY (participant_id) REFERENCES PARTICIPANTS')
        execute('ALTER TABLE user_notifications ADD CONSTRAINT fk_study FOREIGN KEY (study_id) REFERENCES STUDIES')
        execute('ALTER TABLE user_notifications ADD CONSTRAINT fk_spc_schedule FOREIGN KEY (spc_schedule_id) REFERENCES SP_CRF_SCHEDULES')
    }

    void down() {
        dropTable("notifications")
        dropTable("user_notifications")
    }
}