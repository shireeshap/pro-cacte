
class UpdatePasswordHistory extends edu.northwestern.bioinformatics.bering.Migration {

     void up() {
        execute("insert into password_history (user_id, password, password_creation_timestamp)(select id, password, password_last_set from users where password is not null)");
        
     }
    
      void down() {
         execute("delete from PASSWORD_HISTORY");
      }
}