package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;
import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;

/**
 * @author AmeyS
 * Determines the category to which the given proCtcTerm beglongs to.
 * Eight distinct Categories are represented using this enum.
 * (e.g CATEGORY_1 ptoCtcTerms contains questions beloging to type FREQUENCY, SEVERITY, INTERFERENCE.)
 * 
 * TO DO: Use better nomenclature for these categories, check if there is any existing standard followed in medical world.
 */
public enum ProctcTermTypeBasedCategory implements CodedEnum<String>{
	
	CATEGORY_1("111", "FREQUENCY, SEVERITY, INTERFERENCE"),
	CATEGORY_2("110", "FREQUENCY, SEVERITY"),
	CATEGORY_3("011", "SEVERITY, INTERFERENCE"),
	CATEGORY_4("101", "FREQUENCY, INTERFERENCE"),
	CATEGORY_5("100", "FREQUENCY"),
	CATEGORY_6("010", "SEVERITY"),
	CATEGORY_7("1", "PRESENT/ABSENT"),
	CATEGORY_8("0", "AMOUNT");
	
	private final String categoryCode;
	private final String description;
	
	private ProctcTermTypeBasedCategory(String categoryCode, String description) {
		this.categoryCode = categoryCode;
		this.description = description;
		CodedEnumHelper.register(this);
	}
	
	public static ProctcTermTypeBasedCategory getByCode(String code) {
		return getByClassAndCode(ProctcTermTypeBasedCategory.class, code);
	}
	
	public String getDesciption(){
		return description;
	}
	
	public String getName(){
		return this.getName();
	}
	
	@Override
	public String getCode() {
		return getDisplayName();
	}
	
	@Override
	public String getDisplayName() {
		return categoryCode;
	}
}
