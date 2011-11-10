class UpdateCrfCycleDefinition extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
             addColumn("CRF_CYCLE_DEFINITIONS", 'due_date_unit', 'string', nullable: true);
             addColumn("CRF_CYCLE_DEFINITIONS", 'due_date_amount', 'string', nullable: true);
              }

   void down() {
		     dropColumn("CRF_CYCLE_DEFINITIONS", 'due_date_amount')
		     dropColumn("CRF_CYCLE_DEFINITIONS", 'due_date_unit')
    }

}