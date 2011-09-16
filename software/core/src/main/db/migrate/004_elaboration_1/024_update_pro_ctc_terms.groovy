class UpdateProCTCTermFileName extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
             addColumn("pro_ctc_terms", 'file_name', 'string', nullable: true);
              }

   void down() {
		     dropColumn("pro_ctc_terms", 'file_name')
      }

}