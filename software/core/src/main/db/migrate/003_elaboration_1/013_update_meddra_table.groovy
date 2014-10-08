class UpdateMeddraTable extends edu.northwestern.bioinformatics.bering.Migration {

     void up() {
                    addColumn("meddra_llt", 'participant_added', 'boolean', nullable: true);
                }
                
     void down() {
                    dropColumn("meddra_llt", 'participant_added')
                  }

              }