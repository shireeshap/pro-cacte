class AlterCrfs extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {

		execute('ALTER TABLE crfs DROP CONSTRAINT un_crfs_title')
        }

	void down() {

	}
}