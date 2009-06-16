package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.helper.ClinicalStaffTestHelper;
import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.*;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.providers.dao.DaoAuthenticationProvider;
import org.springframework.security.userdetails.UsernameNotFoundException;

/**
 * @author Vinay Kumar
 */

public class UserIntegrationTest extends TestDataManager {

    private User defaultUser;
    protected DaoAuthenticationProvider daoAuthenticationProvider;

    private static final String USER = "user";

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        defaultUser = ClinicalStaffTestHelper.getDefaultClinicalStaff().getUser();
    }

    public void testSaveUser() {
        assertNotNull(defaultUser.getId());
    }

    public void testValidationExceptionForSavingInValidUser() {
        User invalidUser = new User();
        try {
            invalidUser = userRepository.save(invalidUser);
            fail();
        } catch (CtcAeSystemException e) {
            logger.info("expecting this");
        }

        try {
            invalidUser.setUsername("John");
            userRepository.save(invalidUser);
            fail();
        } catch (CtcAeSystemException e) {
        }

    }

    public void testFindById() {

        User existingUser = userRepository.findById(defaultUser.getId());
        assertNotNull(existingUser);
        assertEquals(defaultUser.getUsername(), existingUser.getUsername());
    }


    public void testReceivedBadCredentialsWhenCredentialsNotProvided() {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("user1", null);
        try {
            daoAuthenticationProvider.authenticate(authenticationToken); // null pointer exception
            fail("Expected BadCredenialsException");
        }
        catch (BadCredentialsException expected) {
            assertTrue(true);
        }
    }

    public void testAuthenticateFailsWithInvalidUsernameAndHideUserNotFoundExceptionFalse() {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("INVALID_USER", Fixture.DEFAULT_PASSWORD);

        try {
            daoAuthenticationProvider.authenticate(token);
            fail("Should have thrown UsernameNotFoundException");
        }
        catch (BadCredentialsException expected) {
            assertTrue(true);
        }
    }

    public void testAuthenticateFailsWithMixedCaseUsernameIfDefaultChanged() {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("MaRiSSA", "koala");

        try {
            daoAuthenticationProvider.authenticate(token);
            fail("Should have thrown BadCredentialsException");
        }
        catch (BadCredentialsException expected) {
            assertTrue(true);
        }
    }

    public void testAuthenticatesASecondTime() {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(defaultUser.getUsername(), Fixture.DEFAULT_PASSWORD);

        Authentication result = daoAuthenticationProvider.authenticate(token);

        if (!(result instanceof UsernamePasswordAuthenticationToken)) {
            fail("Should have returned instance of UsernamePasswordAuthenticationToken");
        }

        // Now try to authenticate with the previous result (with its User)
        Authentication result2 = daoAuthenticationProvider.authenticate(result);

        if (!(result2 instanceof UsernamePasswordAuthenticationToken)) {
            fail("Should have returned instance of UsernamePasswordAuthenticationToken");
        }

        assertEquals(result.getCredentials(), result2.getCredentials());
    }


    public void testFindByUserName() throws Exception {
        User userDetails = userRepository.loadUserByUsername(defaultUser.getUsername());
        assertNotNull(userDetails.getId());
        assertEquals(defaultUser.getUsername(), userDetails.getUsername());

        try {
            userRepository.loadUserByUsername("abc");
            fail("Should throw UserNotFoundException");
        }
        catch (UsernameNotFoundException e) {
        }
    }

    public void testAuthenticateSucessfull() {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(defaultUser.getUsername(), Fixture.DEFAULT_PASSWORD);

        token.setDetails("192.168.0.1");

        daoAuthenticationProvider.authenticate(token);

        Authentication result = daoAuthenticationProvider.authenticate(token);
        assertTrue(result instanceof UsernamePasswordAuthenticationToken);

        UsernamePasswordAuthenticationToken castResult = (UsernamePasswordAuthenticationToken) result;
        assertEquals(User.class, castResult.getPrincipal().getClass());
        assertEquals(Fixture.DEFAULT_PASSWORD, castResult.getCredentials());
        assertEquals(defaultUser.getUsername(), castResult.getName());
        // assertEquals("ROLE_ONE", castResult.getAuthorities()[0].getAuthority());
        // assertEquals("ROLE_TWO", castResult.getAuthorities()[1].getAuthority());
        assertEquals("192.168.0.1", castResult.getDetails());

    }

    public void testAuthenticateFailsIfAccountExpired() {

        User userHavingExpiredAccount = Fixture.createUser(USER, Fixture.DEFAULT_PASSWORD, true, false, true, true);
        userRepository.save(userHavingExpiredAccount);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userHavingExpiredAccount
                .getUsername(), Fixture.DEFAULT_PASSWORD);

        try {
            daoAuthenticationProvider.authenticate(token);
            fail("Should have thrown AccountExpiredException");
        }
        catch (AccountExpiredException expected) {
            assertTrue(true);
        }
    }

    public void testAuthenticateFailsIfAccountLocked() {

        User userHavingLockedAccount = Fixture.createUser(USER, Fixture.DEFAULT_PASSWORD, true, true, true, false);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userHavingLockedAccount
                .getUsername(), Fixture.DEFAULT_PASSWORD);
        userRepository.save(userHavingLockedAccount);

        try {
            daoAuthenticationProvider.authenticate(token);
            fail("Should have thrown LockedException");
        }
        catch (LockedException expected) {
            assertTrue(true);
        }
    }

    public void testAuthenticateFailsIfCredentialsExpired() {

        User userHavingExpiredCredentials = Fixture.createUser(USER, Fixture.DEFAULT_PASSWORD, true, true, false, true);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userHavingExpiredCredentials, Fixture.DEFAULT_PASSWORD);
        userRepository.save(userHavingExpiredCredentials);
        try {
            daoAuthenticationProvider.authenticate(token);
            fail("Should have thrown CredentialsExpiredException");
        }
        catch (CredentialsExpiredException expected) {
            assertTrue(true);
        }

        // Check that wrong password causes BadCredentialsException, rather than CredentialsExpiredException
        token = new UsernamePasswordAuthenticationToken(userHavingExpiredCredentials, "wrong_password");

        try {
            daoAuthenticationProvider.authenticate(token);
            fail("Should have thrown BadCredentialsException");
        }
        catch (BadCredentialsException expected) {
            assertTrue(true);
        }
    }

    public void testAuthenticateFailsIfUserDisabled() {

        User userHavingDisabledAccount = Fixture.createUser(USER, Fixture.DEFAULT_PASSWORD, false, true, true, true);
        userRepository.save(userHavingDisabledAccount);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userHavingDisabledAccount,
                Fixture.DEFAULT_PASSWORD);
        try {
            daoAuthenticationProvider.authenticate(token);
            fail("Should have thrown DisabledException");
        }
        catch (DisabledException expected) {
            assertTrue(true);
        }
    }

    public void testAuthenticateFailsWithInvalidPassword() {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(defaultUser.getUsername(),
                "INVALID_PASSWORD");

        try {
            daoAuthenticationProvider.authenticate(token);
            fail("Should have thrown BadCredentialsException");
        }
        catch (BadCredentialsException expected) {
            assertTrue(true);
        }
    }

    public void testAuthenticateFailsWithEmptyUsername() {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(null, Fixture.DEFAULT_PASSWORD);

        try {
            daoAuthenticationProvider.authenticate(token);
            fail("Should have thrown BadCredentialsException");
        }
        catch (BadCredentialsException expected) {
            assertTrue(true);
        }
    }

    @Required
    public void setDaoAuthenticationProvider(DaoAuthenticationProvider daoAuthenticationProvider) {
        this.daoAuthenticationProvider = daoAuthenticationProvider;
    }


}