import edu.northwestern.bioinformatics.bering.Migration

class UpdatePrivilegeNurse extends edu.northwestern.bioinformatics.bering.Migration {
        void up() {
                if (databaseMatches('oracle')) {
                    execute('DELETE FROM ROLE_PRIVILEGES where id=-1010')
                    execute('DELETE FROM ROLE_PRIVILEGES where id=-1011')
                } else {
                    execute('DELETE FROM ROLE_PRIVILEGES where id=-1010')
                    execute('DELETE FROM ROLE_PRIVILEGES where id=-1011')
                }
            }
        void down() {
            }
}
