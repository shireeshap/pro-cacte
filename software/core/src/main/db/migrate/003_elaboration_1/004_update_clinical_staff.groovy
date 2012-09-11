class UpdateClinicalStaff extends edu.northwestern.bioinformatics.bering.Migration {
        void up() {
                if (databaseMatches('oracle')) {
                    execute('ALTER TABLE clinical_staffs MODIFY user_id NULL')
                } else {
                    execute('ALTER TABLE clinical_staffs ALTER COLUMN user_id DROP NOT NULL')
                }
            }
        void down() {
            }
}