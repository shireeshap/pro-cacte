package gov.nih.nci.ctcae.core.security.passwordpolicy.validators;

import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.CombinationPolicy;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.PasswordCreationPolicy;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.PasswordPolicy;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.validation.ValidationErrors;

public class CombinationValidatorTest extends TestDataManager{

	public static CombinationPolicy combinationPolicy;
	public static PasswordCreationPolicy passwordCreationPolicy;
	public static PasswordPolicy passwordPolicy;
	public static CombinationValidator combinationValidator;
	public static User user;
	
	public CombinationValidatorTest(){
		combinationPolicy = new CombinationPolicy();
		passwordCreationPolicy = new PasswordCreationPolicy();
		passwordPolicy = new PasswordPolicy();
		combinationValidator = new CombinationValidator();
		user = new User();
	}
	
	public void testValidateforBaseTenDigitRequired() {
		passwordPolicy = createPasswordPolicy(true, false, 3, false, false, 6);
		user = new User();
		user.setId(1);
		user.setUsername("SiteStaff");
		user.setPassword("ProCtcae");
		assertFalse(combinationValidator.validate(passwordPolicy, user, new ValidationErrors()));
		
		user.setPassword("ProCtcae1");
		assertTrue(combinationValidator.validate(passwordPolicy, user, new ValidationErrors()));
		
		user.setPassword("ProCtcae1234");
		assertTrue(combinationValidator.validate(passwordPolicy, user, new ValidationErrors()));
	}

	public void testValidateforLowerCaseAlphabetRequired() {
		passwordPolicy = createPasswordPolicy(false, true, 3, false, true, 6);
		user = new User();
		user.setId(1);
		user.setUsername("SiteStaff");
		
		user.setPassword("proctcae");
		assertFalse(combinationValidator.validate(passwordPolicy, user, new ValidationErrors()));
		
		user.setPassword("proctcae12");
		assertFalse(combinationValidator.validate(passwordPolicy, user, new ValidationErrors()));
		
		user.setPassword("Proctcae");
		assertTrue(combinationValidator.validate(passwordPolicy, user, new ValidationErrors()));
		
	}
	
	public void testValidateforNonAlphaNumericRequired() {
		passwordPolicy = createPasswordPolicy(false, false, 3, true, false, 6);
		user = new User();
		user.setId(1);
		user.setUsername("SiteStaff");
		
		user.setPassword("ProCtcae");
		assertFalse(combinationValidator.validate(passwordPolicy, user, new ValidationErrors()));
		
		user.setPassword("ProCtcae$");
		assertTrue(combinationValidator.validate(passwordPolicy, user, new ValidationErrors()));
		
		user.setPassword("proctcae^");
		assertTrue(combinationValidator.validate(passwordPolicy, user, new ValidationErrors()));
	}
	
	public void testMaxSubstringLength() {
		passwordPolicy = createPasswordPolicy(false, false, 3,  false, false, 6);
		user = new User();
		user.setId(1);
		user.setUsername("SiteStaff");
		
		user.setPassword("SiCtcae");
		assertTrue(combinationValidator.validate(passwordPolicy, user, new ValidationErrors()));
		
		user.setPassword("SiteS");
		assertFalse(combinationValidator.validate(passwordPolicy, user, new ValidationErrors()));
		
		user.setPassword("Site");
		assertFalse(combinationValidator.validate(passwordPolicy, user, new ValidationErrors()));
	}
	
	public PasswordPolicy createPasswordPolicy(boolean isBaseTenDigit,	boolean isLowerCaseAlphabetRequired, int maxSubstringLength,
			boolean isNonAlphaNumericRequired,	boolean isUpperCaseAlphabetRequired, int passwordLength) {
		
		combinationPolicy.setBaseTenDigitRequired(isBaseTenDigit);
		combinationPolicy.setLowerCaseAlphabetRequired(isLowerCaseAlphabetRequired);
		combinationPolicy.setMaxSubstringLength(maxSubstringLength);
		combinationPolicy.setNonAlphaNumericRequired(isNonAlphaNumericRequired);
		combinationPolicy.setUpperCaseAlphabetRequired(isUpperCaseAlphabetRequired);
		passwordCreationPolicy.setMinPasswordLength(passwordLength);
		passwordCreationPolicy.setCombinationPolicy(combinationPolicy);
		passwordPolicy.setPasswordCreationPolicy(passwordCreationPolicy);
		return passwordPolicy;
	}
}
