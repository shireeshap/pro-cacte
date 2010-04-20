class UpdatePasswordPolicy extends edu.northwestern.bioinformatics.bering.Migration {

     void up() {

                    addColumn("password_policy", 'role', 'string', nullable: true);
                }

     void down() {
                    dropColumn("password_policy", 'role')               

                  }

              }