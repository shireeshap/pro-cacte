import edu.northwestern.bioinformatics.bering.Migration

class UpdateLeadCraPrivilege extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
		execute("delete from ROLE_PRIVILEGES where role_name='LEAD_CRA' and privilege_id='-41'")
  }

   void down() {

  }
}