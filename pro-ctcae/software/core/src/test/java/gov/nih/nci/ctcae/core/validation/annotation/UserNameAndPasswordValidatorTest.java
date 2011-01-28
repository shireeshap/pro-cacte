package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.CombinationPolicy;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.PasswordCreationPolicy;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.PasswordPolicy;
import gov.nih.nci.ctcae.core.query.UserQuery;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.security.passwordpolicy.PasswordPolicyService;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

/**
 * Created by IntelliJ IDEA.
 * User: Reshma Koganti
 * Date: Jan 27, 2011
 * Time: 4:45:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserNameAndPasswordValidatorTest extends AbstractTestCase {
    private UserNameAndPasswordValidator validator;
    private UserRepository userRepository;
    PasswordPolicyService passwordPolicyService;
    User user;
    List<User> users;
    PasswordPolicy passwordPolicy;
    PasswordCreationPolicy passwordCreationPolicy;
    CombinationPolicy combinationPolicy;

    public void UserNameAndPasswordValidatorTest(){

    }

    public void setUp() throws Exception{
        super.setUp();
        validator = new UserNameAndPasswordValidator();
        userRepository = registerMockFor(UserRepository.class);
        validator.setUserRepository(userRepository);
        passwordPolicyService = registerMockFor(PasswordPolicyService.class);
        validator.setPasswordPolicyService(passwordPolicyService);
        combinationPolicy = new CombinationPolicy();
        passwordPolicy = new PasswordPolicy();
        passwordCreationPolicy =new PasswordCreationPolicy();
        user = new User();
        user.setId(1);
        user.setUsername("reshma");
        user.setPassword("Password");
        users = new ArrayList<User>();

    }

    public void testValidateDwrUniqueName(){
        users.add(user);
        expect(userRepository.find(isA(UserQuery.class))).andReturn(users);
        replayMocks();
        assertTrue(validator.validateDwrUniqueName("reshma"));
    }

    public void testValidateDwrUniqueNameFalse(){
        expect(userRepository.find(isA(UserQuery.class))).andReturn(users).anyTimes();
        replayMocks();
        assertFalse(validator.validateDwrUniqueName("eeee"));
    }

    public void testValidatePasswordPolicyDwrParticipantError(){
        combinationPolicy.setBaseTenDigitRequired(false);
        combinationPolicy.setLowerCaseAlphabetRequired(true);
        combinationPolicy.setMaxSubstringLength(3);
        combinationPolicy.setNonAlphaNumericRequired(false);
        combinationPolicy.setUpperCaseAlphabetRequired(false);
        passwordCreationPolicy.setMinPasswordLength(6);
        passwordCreationPolicy.setCombinationPolicy(combinationPolicy);
        passwordPolicy.setPasswordCreationPolicy(passwordCreationPolicy);
        String role="PARTICIPANT";
        expect(passwordPolicyService.getPasswordPolicy(Role.getByCode(role))).andReturn(passwordPolicy).anyTimes();
        replayMocks();
        assertSame("The password should have at least one lower case letter",validator.validatePasswordPolicyDwr("PARTICIPANT","SSSSSS","RESHMA"));
    }

    public void testValidatePasswordPolicyDwrSITE_PIError(){
        combinationPolicy.setBaseTenDigitRequired(true);
        combinationPolicy.setLowerCaseAlphabetRequired(true);
        combinationPolicy.setMaxSubstringLength(3);
        combinationPolicy.setNonAlphaNumericRequired(true);
        combinationPolicy.setUpperCaseAlphabetRequired(true);
        passwordCreationPolicy.setMinPasswordLength(7);
        passwordCreationPolicy.setCombinationPolicy(combinationPolicy);
        passwordPolicy.setPasswordCreationPolicy(passwordCreationPolicy);
        String role="Site PI";
        expect(passwordPolicyService.getPasswordPolicy(Role.getByCode(role))).andReturn(passwordPolicy).anyTimes();
        replayMocks();
        assertNotNull(validator.validatePasswordPolicyDwr("Site PI","SSSSSS","RESHMA"));
    }

}
