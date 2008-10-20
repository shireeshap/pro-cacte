package gov.nih.nci.ctcae.core;

import org.acegisecurity.providers.dao.DaoAuthenticationProvider;
import org.acegisecurity.providers.dao.cache.EhCacheBasedUserCache;
import org.acegisecurity.providers.dao.salt.SystemWideSaltSource;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;


public abstract class AbstractSecurityIntegrationTestCase extends AbstractJpaIntegrationTestCase {


    protected DaoAuthenticationProvider daoAuthenticationProvider;

    protected SystemWideSaltSource saltSource;

    protected EhCacheBasedUserCache userCache;

    protected static String PASSWORD = "password";

    protected static String USER = "user";

    protected static String VALID_USER;

    protected MockHttpServletRequest request;

    protected MockHttpServletResponse response;

//    protected User userHavingExpiredAccount, userHavingLockedAccount, userHavingExpiredCredentials,
//            userHavingDisabledAccount;
//
//    protected User validUser;


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        request = new MockHttpServletRequest();

        response = new MockHttpServletResponse();

//        VALID_USER = contactInformation.getEmailId();
        // crate valid user;
        // validUser = createUser(VALID_USER, PASSWORD, true, true, true, true, null);

    }


    @Required
    public void setUserCache(final EhCacheBasedUserCache userCache) {
        this.userCache = userCache;
    }

    @Required
    public void setSaltSource(final SystemWideSaltSource saltSource) {
        this.saltSource = saltSource;
    }

    @Required
    public void setDaoAuthenticationProvider(final DaoAuthenticationProvider daoAuthenticationProvider) {
        this.daoAuthenticationProvider = daoAuthenticationProvider;
    }


}