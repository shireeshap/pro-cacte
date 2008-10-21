package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;
import gov.nih.nci.ctcae.core.domain.CRF;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */
public class CRFTest extends TestCase {
    private CRF crf;

    public void testConstructor() {
        crf = new CRF();
        assertNull(crf.getTitle());
        assertNull(crf.getDescription());
        assertNull(crf.getStatus());
        assertNull(crf.getCrfVersion());
    }

    public void testGetterAndSetter() {
        crf = new CRF();
        crf.setTitle("Cancer CRF");
        crf.setDescription("Case Report Form for Cancer Patients");
        crf.setStatus("DRAFT");
        crf.setCrfVersion("1.0");

        assertEquals("Cancer CRF", crf.getTitle());
        assertEquals("Case Report Form for Cancer Patients", crf.getDescription());
        assertEquals("DRAFT", crf.getStatus());
        assertEquals("1.0", crf.getCrfVersion());
    }

    public void testGetCrfItems() {
        crf = new CRF();
        CrfItem crfItem1 = new CrfItem();
        CrfItem crfItem2 = new CrfItem();
        crf.addCrfItem(crfItem1);
        crf.addCrfItem(crfItem2);
        assertEquals("must return 2 items", 2, crf.getCrfItems().size());
        for (int i = 0; i < crf.getCrfItems().size(); i++) {
            assertNotNull("order number must not be null", crf.getCrfItems().get(i).getDisplayOrder());
            assertEquals("must preserve order no", Integer.valueOf(i), crf.getCrfItems().get(i).getDisplayOrder());

        }
    }

    public void testReorderCrfItems() {
        crf = new CRF();
        CrfItem crfItem1 = new CrfItem();
        CrfItem crfItem2 = new CrfItem();
        crf.addCrfItem(crfItem1);
        crf.addCrfItem(crfItem2);
        assertEquals("must return 2 items", 2, crf.getCrfItems().size());
        for (int i = 0; i < crf.getCrfItems().size(); i++) {
            assertEquals("must preserve order no", Integer.valueOf(i), crf.getCrfItems().get(i).getDisplayOrder());

        }

        crf.getCrfItems().get(0).setDisplayOrder(1);
        crf.getCrfItems().get(1).setDisplayOrder(0);
        for (int i = 0; i < crf.getCrfItems().size(); i++) {
            assertEquals("must preserve order no", Integer.valueOf(i), crf.getCrfItems().get(i).getDisplayOrder());

        }

    }

    public void testEqualsAndHashCode() {
        CRF anotherCrf = null;
        assertEquals(anotherCrf, crf);
        crf = new CRF();
        assertFalse(crf.equals(anotherCrf));
        anotherCrf = new CRF();
        assertEquals(anotherCrf, crf);
        assertEquals(anotherCrf.hashCode(), crf.hashCode());

        crf.setTitle("Cancer CRF");
        assertFalse(crf.equals(anotherCrf));

        anotherCrf.setTitle("Cancer CRF");
        assertEquals(anotherCrf.hashCode(), crf.hashCode());
        assertEquals(anotherCrf, crf);

        crf.setDescription("Case Report Form for Cancer Patients");
        anotherCrf.setDescription("Case Report Form for Cancer Patients");

        crf.setStatus("DRAFT");
        anotherCrf.setStatus("DRAFT");

        crf.setCrfVersion("1.0");
        anotherCrf.setCrfVersion("1.0");

        assertEquals(anotherCrf.hashCode(), crf.hashCode());
        assertEquals(anotherCrf, crf);

    }

}