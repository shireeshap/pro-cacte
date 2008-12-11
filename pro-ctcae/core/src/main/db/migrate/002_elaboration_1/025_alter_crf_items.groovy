class AlterCrfItems extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		if (databaseMatches("oracle")) {
			execute('ALTER TABLE crf_items modify crf_id integer NULL')
		} else {
			execute('ALTER TABLE crf_items ALTER COLUMN crf_id DROP NOT NULL')
		}
	}

	void down() {

	}
}