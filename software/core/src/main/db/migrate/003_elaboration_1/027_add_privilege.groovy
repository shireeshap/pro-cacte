import edu.northwestern.bioinformatics.bering.Migration

class AlterPrivilegeUser extends edu.northwestern.bioinformatics.bering.Migration {
        void up() {
            insert('ROLE_PRIVILEGES', [id: -2012, role_name: "NURSE", privilege_id: "-70"], primaryKey: false)
            insert('ROLE_PRIVILEGES', [id: -2013, role_name: "TREATING_PHYSICIAN", privilege_id: "-70"], primaryKey: false)
            }
        void down() {
            }
}
