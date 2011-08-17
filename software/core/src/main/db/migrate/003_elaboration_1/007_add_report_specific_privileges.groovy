import edu.northwestern.bioinformatics.bering.Migration

class AddRolesAndPriveleges extends edu.northwestern.bioinformatics.bering.Migration {
 void up() {
   insert('PROCTCAE_PRIVILEGES', [id: -303, privilege_name: "PRIVILEGE_PARTICIPANT_REPORTS", display_name: "PRIVILEGE_PARTICIPANT_REPORTS"], primaryKey: false)
   insert('PROCTCAE_PRIVILEGES', [id: -304, privilege_name: "PRIVILEGE_STUDY_REPORTS", display_name: "PRIVILEGE_STUDY_REPORTS"], primaryKey: false)
   insert('ROLE_PRIVILEGES', [id: -1004, role_name: "SITE_CRA", privilege_id: "-304"], primaryKey: false)
   insert('ROLE_PRIVILEGES', [id: -1005, role_name: "SITE_PI", privilege_id: "-304"], primaryKey: false)
   insert('ROLE_PRIVILEGES', [id: -1006, role_name: "SITE_CRA", privilege_id: "-303"], primaryKey: false)
   insert('ROLE_PRIVILEGES', [id: -1007, role_name: "SITE_PI", privilege_id: "-303"], primaryKey: false)

   insert('ROLE_PRIVILEGES', [id: -1008, role_name: "LEAD_CRA", privilege_id: "-304"], primaryKey: false)
   insert('ROLE_PRIVILEGES', [id: -1009, role_name: "PI", privilege_id: "-304"], primaryKey: false)
 }
 void down() {
 }
}
