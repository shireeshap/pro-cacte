import edu.northwestern.bioinformatics.bering.Migration

class AddRolesAndPriveleges extends edu.northwestern.bioinformatics.bering.Migration {
 void up() {
   insert('PROCTCAE_PRIVILEGES', [id: -302, privilege_name: "PRIVILEGE_CREATE_ADMIN", display_name: "PRIVILEGE_CREATE_ADMIN"], primaryKey: false)
   insert('ROLE_PRIVILEGES', [id: -907, role_name: "ADMIN", privilege_id: "-302"], primaryKey: false)
 }
 void down() {
 }
}
