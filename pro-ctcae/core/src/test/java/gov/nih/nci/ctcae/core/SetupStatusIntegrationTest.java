package gov.nih.nci.ctcae.core;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Vinay Kumar
 * @crated Mar 18, 2009
 */
public class SetupStatusIntegrationTest extends AbstractHibernateIntegrationTestCase {

    private SetupStatus setupStatus;

    protected static final String[] context = new String[]{
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-util.xml",
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-core.xml",
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-datasource.xml",
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-test.xml",
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-setup.xml"};


    public void testSetupNotRequired() {

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(context);
        setupStatus = (SetupStatus) applicationContext.getBean("setupStatus");

        assertFalse("Initial setup must not be required because there is already one admin user.", setupStatus.isSetupNeeded());

        deleteData();
        assertFalse("Initial setup must be required even if  there is no admin user in database because bean has already been injected. .", setupStatus.isSetupNeeded());


    }

    public void testSetupRequired() {

        deleteData();


        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(context);
        setupStatus = (SetupStatus) applicationContext.getBean("setupStatus");


        assertTrue("Initial setup must be required because  there is no admin user in database. .", setupStatus.isSetupNeeded());


    }

    public void testReCheckSetup() {
        insertAdminUser();

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(context);
        setupStatus = (SetupStatus) applicationContext.getBean("setupStatus");

        assertFalse("Initial setup is not required. .", setupStatus.isSetupNeeded());


        deleteData();

        setupStatus.recheck();
        assertTrue("setup is now required because there is no system admin in database", setupStatus.isSetupNeeded());


    }

}
