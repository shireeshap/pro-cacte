package gov.nih.nci.ctcae.core;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import gov.nih.nci.ctcae.core.helper.TestDataManager;

/**
 * @author Vinay Kumar
 * @crated Mar 18, 2009
 */
public class SetupStatusIntegrationTest extends TestDataManager {

    private SetupStatus setupStatus;

    public void testSetupNotRequired() {
        if (!isTestDataPresent()) {
            createTestData();
        }
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(getConfigLocations());
        setupStatus = (SetupStatus) applicationContext.getBean("setupStatus");
        assertFalse("Initial setup must not be required because there is already one admin user.", setupStatus.isSetupNeeded());
        assertFalse("Initial setup must be required even if  there is no admin user in database because bean has already been injected. .", setupStatus.isSetupNeeded());
    }

    public void testSetupRequired() {
        if (isTestDataPresent()) {
            deleteTestData();
        }
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(getConfigLocations());
        setupStatus = (SetupStatus) applicationContext.getBean("setupStatus");
        assertTrue("Initial setup must be required because  there is no admin user in database. .", setupStatus.isSetupNeeded());
        createTestData();
    }

    public void testReCheckSetup() {

        if (!isTestDataPresent()) {
            createTestData();
        }

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(getConfigLocations());
        setupStatus = (SetupStatus) applicationContext.getBean("setupStatus");

        assertFalse("Initial setup is not required. .", setupStatus.isSetupNeeded());
        deleteTestData();
        setupStatus.recheck();
        assertTrue("setup is now required because there is no system admin in database", setupStatus.isSetupNeeded());
        createTestData();
    }

}
