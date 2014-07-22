
class CreatePasswordHistory extends edu.northwestern.bioinformatics.bering.Migration {

     void up() {
         createTable("PASSWORD_HISTORY") {t ->
             t.addColumn("user_id", "integer", nullable: false)
             t.addColumn("password", "string", nullable: false)
             t.addColumn("password_creation_timestamp", "timestamp", nullable: false)
         }
       execute('ALTER TABLE PASSWORD_HISTORY ADD CONSTRAINT fk_ph_users FOREIGN KEY (user_id) REFERENCES users')
     }
    
      void down() {
         dropTable("PASSWORD_HISTORY")
      }
}