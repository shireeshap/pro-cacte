class AddCtcAEv4Terms extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
            addColumn("PRO_CTC_TERMS", 'core', 'boolean', nullable: true);
    }

    void down() {
        dropTable("notifications")
        dropTable("user_notifications")
    }
}
