class AlterCrfs extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {

		execute('ALTER TABLE crfs DROP CONSTRAINT un_crfs_title')
        execute('ALTER TABLE crfs ADD CONSTRAINT un_title_ver UNIQUE (title, crf_version)')

        }

	void down() {

	}
}