package gov.nih.nci.ctcae.core.security.passwordpolicy.validators;

import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.PasswordPolicy;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.validation.ValidationErrors;

public class CombinationValidatorTest extends TestDataManager{

	public static CombinationValidator combinationValidator;
	public static User user;
	
	public CombinationValidatorTest(){
		user = new User();
		combinationValidator = (CombinationValidator)getApplicationContext().getBean("combinationValidator");
	}
	
	public static PasswordPolicy createPasswordPolicy(boolean isBaseTenDigit,	boolean isLowerCaseAlphabetRequired, int maxSubstringLength,
			boolean isNonAlphaNumericRequired,	boolean isUpperCaseAlphabetRequired, int passwordLength, int passwordHistorySize) {
		combinationPolicy.setBaseTenDigitRequired(isBaseTenDigit);
		combinationPolicy.setLowerCaseAlphabetRequired(isLowerCaseAlphabetRequired);
		combinationPolicy.setMaxSubstringLength(maxSubstringLength);
		combinationPolicy.setNonAlphaNumericRequired(isNonAlphaNumericRequired);
		combinationPolicy.setUpperCaseAlphabetRequired(isUpperCaseAlphabetRequired);
		passwordCreationPolicy.setMinPasswordLength(passwordLength);
		passwordCreationPolicy.setPasswordHistorySize(passwordHistorySize);
		passwordCreationPolicy.setCombinationPolicy(combinationPolicy);
		passwordPolicy.setPasswordCreationPolicy(passwordCreationPolicy);
		return passwordPolicy;
	}
	
	public void testValidateforBaseTenDigitRequired() {
		passwordPolicy = createPasswordPolicy(true, false, 3, false, false, 6, 3);
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
		passwordPolicy = createPasswordPolicy(false, true, 3, false, true, 6, 3);
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
		passwordPolicy = createPasswordPolicy(false, false, 3, true, false, 6, 3);
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
		passwordPolicy = createPasswordPolicy(false, false, 3,  false, false, 6, 3);
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
	
    public void testSavedUsersPasswordHistoryContainsCurrentPassword() {
    	passwordPolicy = createPasswordPolicy(false, false, 3,  false, false, 6, 3);
    	User user = userRepository.findByUserName("Meredith.Olsen@demo.com").get(0);
    	
    	String newEncodedPwd1 = userRepository.getEncodedPassword(user, "password@10");
    	user.setPassword("password@10");
    	userRepository.saveWithoutCheck(user, true);
    	
    	String savedEncodedPwd = user.getPassword();
        assertEquals(savedEncodedPwd, newEncodedPwd1);
        assertTrue(user.isPresentInUserPasswordHistory(savedEncodedPwd, passwordPolicy.getPasswordCreationPolicy()));      
    }

	public void testSavedUsersPasswordHistoryDoesntContainsRandomPassword() {
    	passwordPolicy = createPasswordPolicy(false, false, 3,  false, false, 6, 3);
    	User user = userRepository.findByUserName("Meredith.Olsen@demo.com").get(0);
        
        assertFalse(user.isPresentInUserPasswordHistory("X345", passwordPolicy.getPasswordCreationPolicy()));      
    }
    
    public void testSavedUsersPasswordHistoryContainsLast3Passwords() {
    	passwordPolicy = createPasswordPolicy(false, false, 3,  false, false, 6, 3);
    	User user = userRepository.findByUserName("Meredith.Olsen@demo.com").get(0);
    	
    	String newEncodedPwd1 = userRepository.getEncodedPassword(user, "password@10");
    	user.setPassword("password@10");
    	userRepository.saveWithoutCheck(user, true);
    	
    	String newEncodedPwd2 = userRepository.getEncodedPassword(user, "password@11");
    	user.setPassword("password@11");
    	userRepository.saveWithoutCheck(user, true);
    	
    	String newEncodedPwd3 = userRepository.getEncodedPassword(user, "password@12");
    	user.setPassword("password@12");
    	userRepository.saveWithoutCheck(user, true);
    	
        assertTrue(user.isPresentInUserPasswordHistory(newEncodedPwd3, passwordPolicy.getPasswordCreationPolicy()));
        assertTrue(user.isPresentInUserPasswordHistory(newEncodedPwd2, passwordPolicy.getPasswordCreationPolicy()));
    	assertTrue(user.isPresentInUserPasswordHistory(newEncodedPwd1, passwordPolicy.getPasswordCreationPolicy()));      
    }
	
    public void testSaveUserWithOlderPwdWhichIsNotPresentInHistory() {
    	passwordPolicy = createPasswordPolicy(false, false, 3,  false, false, 6, 2);
    	User user = userRepository.findByUserName("Meredith.Olsen@demo.com").get(0);
    	
    	user.setPassword("password@10");
    	assertTrue(combinationValidator.validate(passwordPolicy, user, new ValidationErrors()));
    	userRepository.saveWithoutCheck(user, true);
    	
    	user.setPassword("password@11");
    	assertTrue(combinationValidator.validate(passwordPolicy, user, new ValidationErrors()));
    	userRepository.saveWithoutCheck(user, true);
    	
    	user.setPassword("password@12");
    	assertTrue(combinationValidator.validate(passwordPolicy, user, new ValidationErrors()));
    	userRepository.saveWithoutCheck(user, true);
    	
    	//retrying password@10 as history size in just 2
    	user.setPassword("password@10");
    	assertTrue(combinationValidator.validate(passwordPolicy, user, new ValidationErrors()));
    	userRepository.saveWithoutCheck(user, true);
    	
    	userRepository.saveWithoutCheck(user, true);
    }
    

}
