package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import junit.framework.TestCase;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */
public class ProCtcValidValueTest extends TestCase {
    private ProCtcValidValue proCtcValidValue;

    public void testConstructor() {
        proCtcValidValue = new ProCtcValidValue();
        assertEquals(proCtcValidValue.getValue(SupportedLanguageEnum.ENGLISH), "");
    }

    public void testGetterAndSetter() {
        proCtcValidValue = new ProCtcValidValue();
        proCtcValidValue.setValue("value1", SupportedLanguageEnum.ENGLISH);

        assertEquals("value1", proCtcValidValue.getValue(SupportedLanguageEnum.ENGLISH));
    }

    public void tesToString() {
        proCtcValidValue = new ProCtcValidValue();
        proCtcValidValue.setValue("value1", SupportedLanguageEnum.ENGLISH);

        assertEquals("value1", proCtcValidValue.toString());
    }

    public void testEqualsAndHashCode() {
        ProCtcValidValue anotherProCtcValidValue = null;

        assertEquals(anotherProCtcValidValue, proCtcValidValue);
        proCtcValidValue = new ProCtcValidValue();
        assertFalse(proCtcValidValue.equals(anotherProCtcValidValue));

        anotherProCtcValidValue = new ProCtcValidValue();
        assertEquals(anotherProCtcValidValue, proCtcValidValue);
        assertEquals(anotherProCtcValidValue.hashCode(), proCtcValidValue.hashCode());

        proCtcValidValue.setValue("value1", SupportedLanguageEnum.ENGLISH);
        assertFalse(proCtcValidValue.equals(anotherProCtcValidValue));
        anotherProCtcValidValue.setValue("value1", SupportedLanguageEnum.ENGLISH);
        assertEquals(anotherProCtcValidValue.hashCode(), proCtcValidValue.hashCode());
        assertEquals(anotherProCtcValidValue, proCtcValidValue);

        ProCtcQuestion proCtcQuestion = new ProCtcQuestion();
        proCtcValidValue.setProCtcQuestion(proCtcQuestion);
        assertFalse(proCtcValidValue.equals(anotherProCtcValidValue));
        anotherProCtcValidValue.setProCtcQuestion(proCtcQuestion);
        assertEquals(anotherProCtcValidValue.hashCode(), proCtcValidValue.hashCode());
        assertEquals(anotherProCtcValidValue, proCtcValidValue);

        proCtcValidValue.setId(1);
        anotherProCtcValidValue.setId(2);
        assertEquals("must not consider id", anotherProCtcValidValue.hashCode(), proCtcValidValue.hashCode());
        assertEquals("must not consider id", anotherProCtcValidValue, proCtcValidValue);

    }

}