class CreateProtocols extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
        createTable("studies") { t ->
            t.addVersionColumn()
            t.addColumn("short_title", "string", nullable:false)
            t.addColumn("long_title", "string", nullable:true)
            t.addColumn("description", "string", nullable:true)
            t.addColumn("assigned_identifier", "string", nullable:false)

            }
    }

    void down() {
        dropTable("studies")
    }
}