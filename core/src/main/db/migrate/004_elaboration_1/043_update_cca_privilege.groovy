import edu.northwestern.bioinformatics.bering.Migration

class UpdateCCAPrivilege extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
		execute("delete from ROLE_PRIVILEGES where role_name='CCA' and privilege_id='-22'")
  }

   void down() {

  }
}