class AlterCrfItems extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {

		execute('ALTER TABLE crf_items ALTER COLUMN crf_id DROP NOT NULL')

	}

	void down() {

	}
}