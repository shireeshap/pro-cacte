class UpdateParticipant extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {

             addColumn("PARTICIPANTS", 'user_number', 'integer', nullable: true);
             addColumn("PARTICIPANTS", 'pin_number', 'integer', nullable: true);

              }

   void down() {
		     dropColumn("PARTICIPANTS", 'user_number')
		     dropColumn("PARTICIPANTS", 'pin_number')
		   }
}