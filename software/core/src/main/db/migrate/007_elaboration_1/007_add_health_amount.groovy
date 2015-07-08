
class AddHealthAmount extends edu.northwestern.bioinformatics.bering.Migration {

     void up() {
         addColumn("sp_crf_schedules", 'health_amount', 'integer', nullable: true);
     }
    
      void down() {
      }
}