import edu.northwestern.bioinformatics.bering.Migration

class UpdateScraPrivilege extends edu.northwestern.bioinformatics.bering.Migration {
        void up() {
                    execute('DELETE FROM ROLE_PRIVILEGES where id=-701')
                    execute('DELETE FROM ROLE_PRIVILEGES where id=-806')
            }
        void down() {
            }
}
