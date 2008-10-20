class CreateCRF extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
        createTable("CRFS") {t ->
            t.addVersionColumn()
            t.addColumn('title', 'string', nullable: false)
            t.addColumn('description', 'string', nullable: true)
            t.addColumn('status', 'string', nullable: false)
            t.addColumn('crf_version', 'string', nullable: false)
        }
    }

    void down() {
        dropTable("CRFS")
    }
}