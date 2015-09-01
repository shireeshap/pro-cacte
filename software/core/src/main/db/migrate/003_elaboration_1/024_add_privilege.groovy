import edu.northwestern.bioinformatics.bering.Migration

class AlterPrivilegeUser extends edu.northwestern.bioinformatics.bering.Migration {
        void up() {
            insert('ROLE_PRIVILEGES', [id: -2011, role_name: "LEAD_CRA", privilege_id: "-11"], primaryKey: false)
            }
        void down() {
            }
}
