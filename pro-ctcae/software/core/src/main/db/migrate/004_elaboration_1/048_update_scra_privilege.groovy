import edu.northwestern.bioinformatics.bering.Migration

class UpadateScraPrivileges extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
insert('ROLE_PRIVILEGES', [id: -703, role_name: "SITE_CRA", privilege_id: "-24"], primaryKey: false)
insert('ROLE_PRIVILEGES', [id: -743, role_name: "SITE_PI", privilege_id: "-24"], primaryKey: false)
  }

   void down() {

  }
}