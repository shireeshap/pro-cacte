import edu.northwestern.bioinformatics.bering.Migration

class AddRolesAndPriveleges extends edu.northwestern.bioinformatics.bering.Migration {
        void up() {
                if (databaseMatches('oracle')) {
                    execute('ALTER TABLE clinical_staffs MODIFY nci_identifier NULL')
                } else {
                    execute('ALTER TABLE clinical_staffs ALTER COLUMN nci_identifier DROP NOT NULL')
                }
            }
        void down() {
            }
}
