import edu.northwestern.bioinformatics.bering.Migration

class UpdatePiPrivilege extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
  		insert('proctcae_privileges', [id: -32, privilege_name: "PRIVILEGE_ADD_STUDY_SITE_STAFF", display_name: "PRIVILEGE_ADD_STUDY_SITE_STAFF"], primaryKey: false)
  		insert('proctcae_privileges', [id: -31, privilege_name: "PRIVILEGE_ADD_STUDY_SITE_RESEARCH_STAFF", display_name: "PRIVILEGE_ADD_STUDY_SITE_RESEARCH_STAFF"], primaryKey: false)
  
  		insert('ROLE_PRIVILEGES', [id: -144, role_name: "LEAD_CRA", privilege_id: "-32"], primaryKey: false)
  		insert('ROLE_PRIVILEGES', [id: -145, role_name: "LEAD_CRA", privilege_id: "-31"], primaryKey: false)
  		insert('ROLE_PRIVILEGES', [id: -244, role_name: "PI", privilege_id: "-32"], primaryKey: false)
  		insert('ROLE_PRIVILEGES', [id: -245, role_name: "PI", privilege_id: "-31"], primaryKey: false)
  		insert('ROLE_PRIVILEGES', [id: -644, role_name: "SITE_CRA", privilege_id: "-32"], primaryKey: false)
  		insert('ROLE_PRIVILEGES', [id: -645, role_name: "SITE_CRA", privilege_id: "-26"], primaryKey: false)
  		insert('ROLE_PRIVILEGES', [id: -744, role_name: "SITE_PI", privilege_id: "-32"], primaryKey: false)
  		insert('ROLE_PRIVILEGES', [id: -745, role_name: "SITE_PI", privilege_id: "-26"], primaryKey: false)
  		
  }

   void down() {

  }
}