
class AddColumnEq5d extends edu.northwestern.bioinformatics.bering.Migration {

     void up() {
        addColumn("crfs", 'is_eq5d', 'boolean', nullable: true);
	    execute("update crfs set is_eq5d = false");
	    execute("ALTER TABLE crfs MODIFY is_eq5d boolean NOT NULL");
        
     }
    
      void down() {
         
      }
}