package gov.nih.nci.ctcae.core;

import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.repository.UserRepository;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

/**
 * Examines the application (via the injected beans) and determines whether any initial setup steps are required.
 * Caches its results so that the setup-or-not filter is faster.
 *
 * @author Vinay Kumar
 * @since Mar 18, 2009
 */
public class SetupStatus implements InitializingBean {
    private Map<InitialSetupElement, SetupChecker> checkers;
    private UserRepository userRepository;

    private boolean[] prepared;
    protected static final Log logger = LogFactory.getLog(SetupStatus.class);

    public SetupStatus() {
        prepared = new boolean[InitialSetupElement.values().length];
        checkers = new LinkedHashMap<InitialSetupElement, SetupChecker>();
        checkers.put(InitialSetupElement.ADMINISTRATOR, new SetupChecker() {
                public boolean isPrepared() {
                	try{
                		return userRepository.getByRole(Role.ADMIN).size() > 0;
                	} catch(Exception e){
                		logger.warn("Error while loading users by role " +e.getMessage());
                		logger.warn("Possible first time start up against blank schema");
                	} 
                	return false;
                }
            });
    }

    public void recheck() {
        for (Map.Entry<InitialSetupElement, SetupChecker> entry : checkers.entrySet()) {
            prepared[entry.getKey().ordinal()] = entry.getValue().isPrepared();
        }
    }

    public void afterPropertiesSet() throws Exception {
        recheck();
    }

    public boolean isSetupNeeded() {
        for (boolean b : prepared) {
            if (!b) return true;
        }
        return false;
    }

    public InitialSetupElement next() {
        recheck();
        for (int i = 0; i < prepared.length; i++) {
            if (!prepared[i]) return InitialSetupElement.values()[i];
        }
        return null;
    }

    ////// PROPERTY ACCESSORS

    public boolean isAdministratorMissing() {
        return !prepared[InitialSetupElement.ADMINISTRATOR.ordinal()];
    }

    ////// CONFIGURATION


    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    ////// INNER CLASSES

    private interface SetupChecker {
        boolean isPrepared();
    }

    public enum InitialSetupElement {
        ADMINISTRATOR
    }
}

