class AlterCrfPage extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {

		addColumn("CRF_PAGES", "page_number", "integer", nullable: true)
		execute('update crf_pages set page_number=id');

		if (databaseMatches("oracle")) {
			execute('ALTER TABLE crf_pages modify page_number integer not NULL')
		} else {
			execute('ALTER TABLE crf_pages ALTER COLUMN page_number set NOT NULL')
		}

	}

	void down() {

	}
}