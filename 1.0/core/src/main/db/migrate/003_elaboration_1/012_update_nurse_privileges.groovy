import edu.northwestern.bioinformatics.bering.Migration

class AddRolesAndPriveleges extends edu.northwestern.bioinformatics.bering.Migration {
 void up() {
  
   insert('ROLE_PRIVILEGES', [id: -1010, role_name: "NURSE", privilege_id: "-304"], primaryKey: false)
   insert('ROLE_PRIVILEGES', [id: -1011, role_name: "TREATING_PHYSICIAN", privilege_id: "-304"], primaryKey: false)
   insert('ROLE_PRIVILEGES', [id: -1012, role_name: "NURSE", privilege_id: "-303"], primaryKey: false)
   insert('ROLE_PRIVILEGES', [id: -1013, role_name: "TREATING_PHYSICIAN", privilege_id: "-303"], primaryKey: false)
 }
 void down() {
 }
}
