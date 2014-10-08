
class UpdatePasswordPolicy extends edu.northwestern.bioinformatics.bering.Migration {

     void up() {
        execute("update password_policy set cn_history_size = 24");
	    execute("update password_policy set cn_history_size = 0 where role = 'PARTICIPANT'");
        
     }
    
      void down() {
         
      }
}