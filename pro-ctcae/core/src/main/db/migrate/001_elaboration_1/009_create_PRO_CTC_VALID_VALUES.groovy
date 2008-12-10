class CreateProCtcValidValue extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		createTable("PRO_CTC_VALID_VALUES") {t ->
			t.addColumn('value', 'string', nullable: false)
		}
	}

	void down() {
		dropTable("PRO_CTC_VALID_VALUES")
	}
}