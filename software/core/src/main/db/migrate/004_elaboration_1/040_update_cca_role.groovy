import edu.northwestern.bioinformatics.bering.Migration

class AddCCAPriveleges extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
insert('ROLE_PRIVILEGES', [id: -2, role_name: "CCA", privilege_id: "-22"], primaryKey: false)
insert('ROLE_PRIVILEGES', [id: -5, role_name: "CCA", privilege_id: "-25"], primaryKey: false)
  }

   void down() {

  }
}