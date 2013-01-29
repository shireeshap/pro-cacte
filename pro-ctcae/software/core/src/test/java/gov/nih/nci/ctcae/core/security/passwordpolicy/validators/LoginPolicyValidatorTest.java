package gov.nih.nci.ctcae.core.security.passwordpolicy.validators;

import java.util.Date;

import com.sun.jmx.snmp.Timestamp;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.LoginPolicy;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.security.user.Credential;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;


public class LoginPolicyValidatorTest extends TestDataManager{
	public LoginPolicyValidator loginPolicyValidator;
	LoginPolicy loginPolicy ;
	User user;
	Credential credential;
	
	public LoginPolicyValidatorTest(){
		loginPolicyValidator = new LoginPolicyValidator();
		loginPolicy = new LoginPolicy();
		user = new User();
		user.setId(1);
        user.setUsername("SiteStaff");
        user.setPassword("ProCtcae123");
        credential = new Credential("SiteStaff","ProCtcae123!");
	}

	public void testValidateAllowedFailedLoginAttempts(){
		loginPolicy = createLoginPolicy(3, 180, 86400, 7776000);
        
		user.setFailedLoginAttempts(3);
		credential.setUser(user);
		try{
			loginPolicyValidator.validateAllowedFailedLoginAttempts(loginPolicy, credential);
		}catch (Exception e) {
			assertTrue(e.getMessage().contains("Too many failed login attempts resulted in account lock out for"));
		}
		
		user.setFailedLoginAttempts(1);
		credential.setUser(user);
		assertTrue(loginPolicyValidator.validateAllowedFailedLoginAttempts(loginPolicy, credential));
	}
	
	public void testValidateLockOutDuration(){
        user.setFailedLoginAttempts(-1);
        Date today = new Date();
        today.setMinutes(today.getMinutes()-1);
        user.setLastFailedLoginAttemptTime(today);
		credential.setUser(user);
		
		//Login policy having a Lockout duration for 86400 secs, expect to fail the validation, thereby throwing an exception
		loginPolicy = createLoginPolicy(1, 180, 86400, 7776000);
		try{
			loginPolicyValidator.validateLockOutDuration(loginPolicy, credential);
		}catch (Exception e) {
			assertTrue(e.getMessage().contains("Account is locked out for"));
		}	
		
		//Login policy having a shorter Lockout duration for 50 secs, expect to pass the validation
		loginPolicy = createLoginPolicy(1, 180, 50, 7776000);
		assertTrue(loginPolicyValidator.validateLockOutDuration(loginPolicy, credential));
	}
	
	public void testValidateMaxPasswordAge(){
		
		Date today = new Date();
		Date previousDay = DateUtils.addDaysToDate(today, -1);
        user.setPasswordLastSet(new java.sql.Timestamp(previousDay.getTime()));
		credential.setUser(user);

		// LoginPolicy with maxPasswordAge as 86200, expect fail the validation therby throwing an exception
		loginPolicy = createLoginPolicy(1, 180, 86400, 86200);
		try{
			loginPolicyValidator.validateMaxPasswordAge(loginPolicy, credential);
		}catch (Exception e) {
			assertTrue(e.getMessage().contains("Password is too old."));
		}	
		
		// LoginPolicy with maxPasswordAge as 86600, expect to pass the validation
		loginPolicy = createLoginPolicy(1, 180, 86400, 86600);
		assertTrue(loginPolicyValidator.validateMaxPasswordAge(loginPolicy, credential));
		
	}

	public LoginPolicy createLoginPolicy(int allowedFailedLoginAttempts,
			int allowedLoginTime, int lockOutDuration, int maxPasswordAge) {
		
		loginPolicy.setAllowedFailedLoginAttempts(allowedFailedLoginAttempts);
		loginPolicy.setAllowedLoginTime(allowedLoginTime);
		loginPolicy.setLockOutDuration(lockOutDuration);
		loginPolicy.setMaxPasswordAge(maxPasswordAge);
		return loginPolicy;
	}
}
