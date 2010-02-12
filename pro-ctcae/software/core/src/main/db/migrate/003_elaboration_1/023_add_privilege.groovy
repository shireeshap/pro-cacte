import edu.northwestern.bioinformatics.bering.Migration

class AlterPrivilegeUser extends edu.northwestern.bioinformatics.bering.Migration {
        void up() {
            insert('PROCTCAE_PRIVILEGES', [id: -11, privilege_name: "PRIVILEGE_VIEW_CLINICAL_STAFF", display_name: "PRIVILEGE_VIEW_CLINICAL_STAFF"], primaryKey: false)
            insert('ROLE_PRIVILEGES', [id: -2010, role_name: "ODC", privilege_id: "-11"], primaryKey: false)
            }
        void down() {
            }
}
