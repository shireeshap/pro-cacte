package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.web.WebTestCase;

/**
 * @author Harsh Agarwal
 * @crated Nov 24, 2008
 */
public class ScheduleCrfTabTest extends WebTestCase {
    private ScheduleCrfTab tab;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        tab = new ScheduleCrfTab();


    }

    public void testConstructor (){
        assertNotNull(tab);
    }

}