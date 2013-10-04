package gov.nih.nci.ctcae.core.domain;

/**ProctcaeGradeMappingCreator class
 * @author AmeyS
 * This class is used for creating concrete instance for any of the 8 available ProctcaeGradeMapping category type.
 * (e.g. createCategoryFSI method is responsible for creating ProctcaeGradeMapping instance of category_FSI)
 *
 */
public class ProctcaeGradeMappingCreator {
	
	public static ProctcaeGradeMapping createCategoryFSI(String freqResponse, String sevResponse, String intResponse, ProctcaeGradeMappingVersion proctcaeGradeMappingVersion, ProCtcTerm proCtcTerm){
		Integer frequency = (freqResponse != null ? Integer.parseInt(freqResponse) : 0);
		Integer severity = (sevResponse != null ? Integer.parseInt(sevResponse) : 0);
		Integer interference = (intResponse != null ? Integer.parseInt(intResponse) : 0);
		return new ProctcaeGradeMapping(frequency, severity, interference, null, proctcaeGradeMappingVersion, proCtcTerm);
	}
	
	public static ProctcaeGradeMapping createCategoryFS(String freqResponse, String sevResponse, ProctcaeGradeMappingVersion proctcaeGradeMappingVersion, ProCtcTerm proCtcTerm){
		Integer frequency = (freqResponse != null ? Integer.parseInt(freqResponse) : 0);
		Integer severity = (sevResponse != null ? Integer.parseInt(sevResponse) : 0);
		return new ProctcaeGradeMapping(frequency, severity, null, null, proctcaeGradeMappingVersion, proCtcTerm);
	}
	
	public static ProctcaeGradeMapping createCategorySI(String sevResponse, String intResponse, ProctcaeGradeMappingVersion proctcaeGradeMappingVersion, ProCtcTerm proCtcTerm){
		Integer severity = (sevResponse != null ? Integer.parseInt(sevResponse) : 0);
		Integer interference = (intResponse != null ? Integer.parseInt(intResponse) : 0);
		return new ProctcaeGradeMapping(null, severity, interference, null, proctcaeGradeMappingVersion, proCtcTerm);
	}
	
	public static ProctcaeGradeMapping createCategoryFI(String freqResponse, String intResponse, ProctcaeGradeMappingVersion proctcaeGradeMappingVersion, ProCtcTerm proCtcTerm){
		Integer frequency = (freqResponse != null ? Integer.parseInt(freqResponse) : 0);
		Integer interference = (intResponse != null ? Integer.parseInt(intResponse) : 0);
		return new ProctcaeGradeMapping(frequency, null, interference, null, proctcaeGradeMappingVersion, proCtcTerm);
	}
	
	public static ProctcaeGradeMapping createCategoryF(String freqResponse, ProctcaeGradeMappingVersion proctcaeGradeMappingVersion, ProCtcTerm proCtcTerm){
		Integer frequency = (freqResponse != null ? Integer.parseInt(freqResponse) : 0);
		return new ProctcaeGradeMapping(frequency, null, null, null, proctcaeGradeMappingVersion, proCtcTerm);
	}
	
	public static ProctcaeGradeMapping createCategoryS(String sevResponse, ProctcaeGradeMappingVersion proctcaeGradeMappingVersion, ProCtcTerm proCtcTerm){
		Integer severity = (sevResponse != null ? Integer.parseInt(sevResponse) : 0);
		return new ProctcaeGradeMapping(null, severity, null, null, proctcaeGradeMappingVersion, proCtcTerm);
	}
	
	public static ProctcaeGradeMapping createCategoryPA(String presentAbsentResponse, ProctcaeGradeMappingVersion proctcaeGradeMappingVersion, ProCtcTerm proCtcTerm){
		Integer presentAbsent = (presentAbsentResponse != null ? Integer.parseInt(presentAbsentResponse) : 0);
		return new ProctcaeGradeMapping(presentAbsent, false, null, proctcaeGradeMappingVersion, proCtcTerm);
	}
	
	public static ProctcaeGradeMapping createCategoryAMT(String amountResponse, ProctcaeGradeMappingVersion proctcaeGradeMappingVersion, ProCtcTerm proCtcTerm){
		Integer amount = (amountResponse != null ? Integer.parseInt(amountResponse) : 0);
		return new ProctcaeGradeMapping(amount, true, null, proctcaeGradeMappingVersion, proCtcTerm);
	}
}
