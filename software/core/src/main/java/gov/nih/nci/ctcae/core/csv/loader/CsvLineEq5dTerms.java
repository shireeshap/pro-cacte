package gov.nih.nci.ctcae.core.csv.loader;

public class CsvLineEq5dTerms {

	public String categoryName;
	public String termEnglish;
	public String termSpanish;
	public String valueEnglish;
	public String valueSpanish;
	
	public CsvLineEq5dTerms(){
	}

	public CsvLineEq5dTerms(String categoryName, String termEng,
			String termSpanish, String valueEng, String valueSpanish) {
		super();
		this.categoryName = categoryName;
		this.termEnglish = termEng;
		this.termSpanish = termSpanish;
		this.valueEnglish = valueEng;
		this.valueSpanish = valueSpanish;
	}
}
