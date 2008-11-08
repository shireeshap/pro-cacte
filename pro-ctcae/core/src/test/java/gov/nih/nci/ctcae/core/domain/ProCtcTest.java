package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

import java.util.Date;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */
public class ProCtcTest extends TestCase {
    private ProCtc proCtc;

    public void testConstructor() {
//		proCtc = new ProCtc();
//		assertNull(proCtc.getProCtcVersion());
//		assertNull(proCtc.getReleaseDate());
//		assertNull(proCtc.getProCtcTerms());
    }

    public void testGetterAndSetter() {
        proCtc = new ProCtc();
        proCtc.setProCtcVersion("1.0");
        Date releaseDate = new Date();
        proCtc.setReleaseDate(releaseDate);

        assertEquals("1.0", proCtc.getProCtcVersion());
        assertEquals(releaseDate, proCtc.getReleaseDate());
    }

    public void testEqualsAndHashCode() {
        ProCtc anotherProCtc = null;
        assertEquals(anotherProCtc, proCtc);
        proCtc = new ProCtc();
        assertFalse(proCtc.equals(anotherProCtc));
        anotherProCtc = new ProCtc();
        assertEquals(anotherProCtc, proCtc);
        assertEquals(anotherProCtc.hashCode(), proCtc.hashCode());

        proCtc.setProCtcVersion("1.0");
        assertFalse(proCtc.equals(anotherProCtc));

        anotherProCtc.setProCtcVersion("1.0");
        assertEquals(anotherProCtc.hashCode(), proCtc.hashCode());
        assertEquals(anotherProCtc, proCtc);

        Date releaseDate = new Date();
        proCtc.setReleaseDate(releaseDate);
        anotherProCtc.setReleaseDate(releaseDate);

        assertEquals(anotherProCtc.hashCode(), proCtc.hashCode());
        assertEquals(anotherProCtc, proCtc);

    }

}