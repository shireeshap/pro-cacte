import edu.northwestern.bioinformatics.bering.Migration

class AddLcraPrivileges extends edu.northwestern.bioinformatics.bering.Migration {
        void up() {
            insert('ROLE_PRIVILEGES', [id: -2019, role_name: "LEAD_CRA", privilege_id: "-72"], primaryKey: false)
            insert('ROLE_PRIVILEGES', [id: -2020, role_name: "PI", privilege_id: "-72"], primaryKey: false)
            }
        void down() {
            }
}
