
class AddColumnEq5d extends edu.northwestern.bioinformatics.bering.Migration {

     void up() {
        addColumn("crfs", 'is_eq5d', 'boolean', nullable: false);
	    execute("update crfs set is_eq5d = false");
        
     }
    
      void down() {
         
      }
}