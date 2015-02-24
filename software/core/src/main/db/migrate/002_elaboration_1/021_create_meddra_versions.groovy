class CreatePriorTherapyAgents extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {

        createTable("meddra_versions") { t->
            t.setIncludePrimaryKey(false)
            t.addColumn("id", "integer", nullable: false, primaryKey: true)
            t.addColumn("name", "string", nullable: false)
        }

        insert("meddra_versions", [ id: 9, name: "MedDRA v9" ], primaryKey: false)


    }

    void down() {
        dropTable('meddra_versions')

    }
}