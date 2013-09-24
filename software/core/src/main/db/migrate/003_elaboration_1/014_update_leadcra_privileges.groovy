import edu.northwestern.bioinformatics.bering.Migration

class UpdateLeadCraPrivileges extends edu.northwestern.bioinformatics.bering.Migration {
 void up() {

   insert('ROLE_PRIVILEGES', [id: -143, role_name: "LEAD_CRA", privilege_id: "-22"], primaryKey: false)
   insert('ROLE_PRIVILEGES', [id: -243, role_name: "PI", privilege_id: "-22"], primaryKey: false)
   insert('ROLE_PRIVILEGES', [id: -1014, role_name: "LEAD_CRA", privilege_id: "-303"], primaryKey: false)
   insert('ROLE_PRIVILEGES', [id: -1015, role_name: "PI", privilege_id: "-303"], primaryKey: false)
 }
 void down() {
 }
}