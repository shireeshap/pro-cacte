import edu.northwestern.bioinformatics.bering.Migration

class AddRolesAndPriveleges extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    insert('PROCTCAE_PRIVILEGES', [id: -72, privilege_name: "PRIVILEGE_ENTER_PARTICIPANT_RESPONSE", display_name: "PRIVILEGE_ENTER_PARTICIPANT_RESPONSE"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -2014, role_name: "TREATING_PHYSICIAN", privilege_id: "-72"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -2015, role_name: "NURSE", privilege_id: "-72"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -2016, role_name: "SITE_CRA", privilege_id: "-72"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -2017, role_name: "SITE_PI", privilege_id: "-72"], primaryKey: false)
    insert('ROLE_PRIVILEGES', [id: -2018, role_name: "ADMIN", privilege_id: "-72"], primaryKey: false)
  }

  void down() {

  }
}