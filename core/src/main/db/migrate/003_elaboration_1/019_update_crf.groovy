class UpdateCrf extends edu.northwestern.bioinformatics.bering.Migration {

     void up() {
                    addColumn("CRFS", 'is_baseline', 'boolean', nullable: true);
                }

     void down() {
                    dropColumn("CRFS", 'is_baseline')
                  }

              }