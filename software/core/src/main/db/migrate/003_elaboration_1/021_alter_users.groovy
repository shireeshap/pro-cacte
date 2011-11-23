class CreateNotificationRulesTable extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
            addColumn("USERS", 'number_of_attempts', 'integer', nullable: true);
    }

    void down() {
        dropTable("notifications")
        dropTable("user_notifications")
    }
}