class UpdateCtcCategory extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
             addColumn("ctc_categories", 'user_defined', 'boolean', nullable: true);
             execute('update ctc_categories set user_defined = false');
              }

   void down() {
		     dropColumn("ctc_categories", 'user_defined')
      }

}
