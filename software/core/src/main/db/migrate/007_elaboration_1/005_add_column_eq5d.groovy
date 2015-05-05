
class AddColumnEq5d extends edu.northwestern.bioinformatics.bering.Migration {

     void up() {
        addColumn("CRFS", 'is_eq5d', 'boolean', nullable: true);
	    execute("update CRFS set is_eq5d = false where is_eq5d is null");
	    execute("ALTER TABLE CRFS ALTER column is_eq5d SET NOT NULL;");
        
     }
    
      void down() {
         
      }
}