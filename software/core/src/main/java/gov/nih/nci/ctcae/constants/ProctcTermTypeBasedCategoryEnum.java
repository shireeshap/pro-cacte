package gov.nih.nci.ctcae.constants;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;

/**
 * @author AmeyS, VinayG
 * Determines the category to which the given proCtcTerm belongs.
 * Eight distinct Categories are represented using this enum.
 * (e.g CATEGORY_1 ptoCtcTerms contains questions belonging to type FREQUENCY, SEVERITY, INTERFERENCE.)
 * 
 */
public enum ProctcTermTypeBasedCategoryEnum implements CodedEnum<String>{
	
	CATEGORY_FSI("1", "FREQUENCY, SEVERITY, INTERFERENCE"),
	CATEGORY_FS("2", "FREQUENCY, SEVERITY"),
	CATEGORY_SI("3", "SEVERITY, INTERFERENCE"),
	CATEGORY_FI("4", "FREQUENCY, INTERFERENCE"),
	CATEGORY_F("5", "FREQUENCY"),
	CATEGORY_S("6", "SEVERITY"),
	CATEGORY_PA("7", "PRESENT/ABSENT"),
	CATEGORY_AMT("8", "AMOUNT");
	
	private final String code;
	private final String description;
	
	private ProctcTermTypeBasedCategoryEnum(String code, String description) {
		this.code = code;
		this.description = description;
		CodedEnumHelper.register(this);
	}
	
	public static ProctcTermTypeBasedCategoryEnum getByCode(String code) {
		return getByCode(code);
	}
	
	public String getDesciption(){
		return description;
	}
	
	@Override
	public String getCode() {
		return code;
	}
	
	@Override
	public String getDisplayName() {
		return description;
	}
}
