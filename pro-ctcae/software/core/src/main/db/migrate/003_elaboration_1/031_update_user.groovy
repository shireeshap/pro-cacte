class UpdateUser extends edu.northwestern.bioinformatics.bering.Migration {

     void up() {
                   dropColumn("USERS", 'last_login')
                   dropColumn("USERS", 'num_failed_logins')
                   dropColumn("USERS", 'password_last_set')
                   dropColumn("USERS", 'token_time')
                   dropColumn("USERS", 'token')
                   dropColumn("USERS", 'salt')


                    addColumn("USERS", 'salt', 'string', nullable: true);
                    addColumn("USERS", 'token', 'string', nullable: true);
                    addColumn("USERS", 'token_time', 'timestamp', nullable: true);
                    addColumn("USERS", 'password_last_set', 'timestamp', nullable: true);
                    addColumn("USERS", 'num_failed_logins', 'integer', nullable: true);
                    addColumn("USERS", 'last_login', 'date', nullable: true);
                }

     void down() {
                    dropColumn("USERS", 'last_login')
                    dropColumn("USERS", 'num_failed_logins')
                    dropColumn("USERS", 'password_last_set')
                    dropColumn("USERS", 'token_time')
                    dropColumn("USERS", 'token')
                    dropColumn("USERS", 'salt')

                  }

              }