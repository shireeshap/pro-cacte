class UpdateCrf extends edu.northwestern.bioinformatics.bering.Migration {

     void up() {
                    addColumn("CRFS", 'is_hidden', 'boolean', nullable: true);
                    execute("update CRFS set is_hidden = false where is_hidden is null")

                }

     void down() {
                    dropColumn("CRFS", 'is_hidden')
                  }

              }