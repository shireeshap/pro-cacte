import java.lang.Exception;

class ExecuteFuzzyStrMatch extends edu.northwestern.bioinformatics.bering.Migration {
	void up(){
		external("../../PostGreSql/fuzzystrmatch.sql")
	}
	
	void down(){		
	}
}