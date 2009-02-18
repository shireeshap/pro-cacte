class CreateCtc extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		createTable("CTC") {t ->
			t.addColumn('name', 'string', nullable: true)
			}
            insert('ctc', [id: 2, name: "CTC v2.0"], primaryKey: false)
            insert('ctc', [id: 3, name: "CTCAE v3.0"], primaryKey: false)


 	}
 		void down() {
		dropTable("CTC")
	}
}
