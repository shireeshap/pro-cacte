class UpdateValidValues extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
             addColumn("pro_ctc_valid_values", 'response_code', 'integer', nullable: true);
              }

   void down() {
		     dropColumn("pro_ctc_valid_values", 'response_code')
      }

}