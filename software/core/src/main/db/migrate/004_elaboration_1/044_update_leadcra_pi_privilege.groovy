import edu.northwestern.bioinformatics.bering.Migration

class UpdatePiPrivilege extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
  		insert('ROLE_PRIVILEGES', [id: -126, role_name: "LEAD_CRA", privilege_id: "-41"], primaryKey: false)
  		
		execute("delete from ROLE_PRIVILEGES where role_name='PI' and privilege_id='-26'")
		execute("delete from ROLE_PRIVILEGES where role_name='LEAD_CRA' and privilege_id='-26'")
  }

   void down() {

  }
}