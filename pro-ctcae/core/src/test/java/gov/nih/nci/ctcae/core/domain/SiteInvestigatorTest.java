package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

import java.util.Date;


/**
 * @author Mehul Gulati
 * Date: Oct 17, 2008
 */

public class SiteInvestigatorTest extends TestCase {

    private SiteInvestigator siteInvestigator;

    public void testGetterAndSetter() {
        siteInvestigator = new SiteInvestigator();
        siteInvestigator.setStatusCode("ab");
        siteInvestigator.setStatusDate(new Date());

        assertEquals("ab", siteInvestigator.getStatusCode());
        assertEquals(new Date(), siteInvestigator.getStatusDate());
    }

    public void testEqualsAndHashCode() {

        SiteInvestigator anothersiteInvestigator = null;
        assertEquals(anothersiteInvestigator, siteInvestigator);

        siteInvestigator = new SiteInvestigator();
        assertFalse(siteInvestigator.equals(anothersiteInvestigator));

        anothersiteInvestigator = new SiteInvestigator();
        assertEquals(anothersiteInvestigator, siteInvestigator);
        assertEquals(anothersiteInvestigator.hashCode(), siteInvestigator.hashCode());

        siteInvestigator.setStatusCode("ab");
        assertFalse(siteInvestigator.equals(anothersiteInvestigator));

        anothersiteInvestigator.setStatusCode("ab");
        assertEquals(anothersiteInvestigator, siteInvestigator);
        assertEquals(anothersiteInvestigator.hashCode(), siteInvestigator.hashCode());

        siteInvestigator.setStatusDate(new Date());
        assertFalse(siteInvestigator.equals(anothersiteInvestigator));

        anothersiteInvestigator.setStatusDate(new Date());
        assertEquals(anothersiteInvestigator, siteInvestigator);
        assertEquals(anothersiteInvestigator.hashCode(), siteInvestigator.hashCode());

    }
}
