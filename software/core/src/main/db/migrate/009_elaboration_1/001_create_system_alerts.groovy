import edu.northwestern.bioinformatics.bering.Migration

class CreateSystemAlerts extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    createTable("system_alerts") {t ->
      t.addVersionColumn()
      t.addColumn('start_date', 'date', nullable: true)
      t.addColumn('end_date', 'date', nullable: true)
      t.addColumn('alert_status', 'string', nullable: true)
      t.addColumn('alert_message', 'string', nullable: true)
    }
    
  }

  void down() {
    dropTable("system_alerts")
  }

}