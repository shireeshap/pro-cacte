import edu.northwestern.bioinformatics.bering.Migration

class UpdateOdcPrivilege extends edu.northwestern.bioinformatics.bering.Migration {
 void up() {

   insert('ROLE_PRIVILEGES', [id: -337, role_name: "ODC", privilege_id: "-304"], primaryKey: false)
   insert('ROLE_PRIVILEGES', [id: -338, role_name: "ODC", privilege_id: "-303"], primaryKey: false)
   insert('ROLE_PRIVILEGES', [id: -339, role_name: "ODC", privilege_id: "-3"], primaryKey: false)
   insert('ROLE_PRIVILEGES', [id: -340, role_name: "ODC", privilege_id: "-62"], primaryKey: false)
   insert('ROLE_PRIVILEGES', [id: -341, role_name: "ODC", privilege_id: "-70"], primaryKey: false)
 }
 void down() {
 }
}
