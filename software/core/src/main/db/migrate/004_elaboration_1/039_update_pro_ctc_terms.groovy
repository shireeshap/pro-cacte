class UpdateProCtcTerms extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
             addColumn("PRO_CTC_TERMS", 'currency', 'string', nullable: true);
              }

   void down() {
		     dropColumn("PRO_CTC_TERMS", 'currency')

    }

}