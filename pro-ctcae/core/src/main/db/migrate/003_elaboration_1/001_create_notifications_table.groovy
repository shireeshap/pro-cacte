class CreateotificationsTable extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
        createTable("notifications") { t ->
            t.addColumn("text", "string", nullable: false)
            t.addColumn("date", "timestamp", nullable: false)
            t.addVersionColumn()
        }

        createTable("user_notifications") { t ->
            t.addColumn("notification_id", "integer", nullable: false)
            t.addColumn("user_id", "integer", nullable: false)
            t.addColumn("is_new", "boolean", nullable: false)
            t.addColumn("study_title", "string", nullable: false)
            t.addVersionColumn()
        }


        execute('ALTER TABLE user_notifications ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES USERS')
        execute('ALTER TABLE user_notifications ADD CONSTRAINT fk_notifications FOREIGN KEY (notification_id) REFERENCES NOTIFICATIONS')
    }

    void down() {
        dropTable("notifications")
        dropTable("user_notifications")
    }
}