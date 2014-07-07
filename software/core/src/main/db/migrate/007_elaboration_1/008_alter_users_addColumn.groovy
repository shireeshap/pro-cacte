import java.lang.Exception;

class AlterUsersAddColumn extends edu.northwestern.bioinformatics.bering.Migration {
	void up(){
		addColumn("users", 'account_lockout_time', 'timestamp', nullable: true);
        
	}
	
	void down(){
	dropColumn("users", 'account_lockout_time', 'timestamp', nullable: true);
	}
}