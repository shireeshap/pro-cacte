class UpdateStudy extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {

             addColumn("STUDIES", 'call_back_hour', 'integer', nullable: true);
             addColumn("STUDIES", 'call_back_frequency', 'integer', nullable: true);
             
              }

   void down() {
		     dropColumn("STUDIES", 'call_back_frequency')
		     dropColumn("STUDIES", 'call_back_hour')
		   }
}