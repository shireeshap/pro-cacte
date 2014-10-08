package gov.nih.nci.ctcae.core;

import gov.nih.nci.ctcae.core.helper.TestDataManager;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Vinay Kumar
 * @since Mar 18, 2009
 */
public class SetupStatusIntegrationTest extends TestDataManager {

    private SetupStatus setupStatus;

    public void testCreateTestData() {
        deleteAndCreateTestData();
        assertTrue(isTestDataPresent());
    }

    public void testSetupNotRequired() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(getConfigLocationsA());
        setupStatus = (SetupStatus) applicationContext.getBean("setupStatus");
        assertTrue("Initial setup must not be required because there is already one admin user.", setupStatus.isSetupNeeded());
        assertTrue("Initial setup must be required even if  there is no admin user in database because bean has already been injected. .", setupStatus.isSetupNeeded());
    }

    public void testSetupRequired() {
        deleteAdminUser();
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(getConfigLocationsA());
        setupStatus = (SetupStatus) applicationContext.getBean("setupStatus");
        assertTrue("Initial setup must be required because  there is no admin user in database. .", setupStatus.isSetupNeeded());
        insertAdminUser();
    }

    public void testReCheckSetup() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(getConfigLocationsA());
        setupStatus = (SetupStatus) applicationContext.getBean("setupStatus");
        assertTrue("Initial setup is not required. .", setupStatus.isSetupNeeded());
        deleteAdminUser();
        setupStatus.recheck();
        assertTrue("setup is now required because there is no system admin in database", setupStatus.isSetupNeeded());
        insertAdminUser();
    }

    private String[] getConfigLocationsA() {
        return new String[]{
                "classpath*:gov/nih/nci/ctcae/core/applicationContext-util.xml"
                , "classpath*:gov/nih/nci/ctcae/core/testapplicationContext-util.xml"
                , "classpath*:gov/nih/nci/ctcae/core/applicationContext-core.xml"
                , "classpath*:gov/nih/nci/ctcae/core/applicationContext-datasource.xml"
                , "classpath*:gov/nih/nci/ctcae/core/applicationContext-setup.xml"
        };

    }


}
