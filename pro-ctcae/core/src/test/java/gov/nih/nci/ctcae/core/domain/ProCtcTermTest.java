package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */
public class ProCtcTermTest extends TestCase {
    private ProCtcQuestion proCtcQuestion;

    public void testConstructor() {
        proCtcQuestion = new ProCtcQuestion();
        assertNull(proCtcQuestion.getQuestionText());
    }

    public void testGetterAndSetter() {
        proCtcQuestion = new ProCtcQuestion();
        proCtcQuestion.setQuestionText("How is the pain?");

        assertEquals("How is the pain?", proCtcQuestion.getQuestionText());
    }

    public void testEqualsAndHashCode() {
        ProCtcQuestion anotherProCtcQuestion = null;
        assertEquals(anotherProCtcQuestion, proCtcQuestion);
        proCtcQuestion = new ProCtcQuestion();
        assertFalse(proCtcQuestion.equals(anotherProCtcQuestion));
        anotherProCtcQuestion = new ProCtcQuestion();
        assertEquals(anotherProCtcQuestion, proCtcQuestion);
        assertEquals(anotherProCtcQuestion.hashCode(), proCtcQuestion.hashCode());

        proCtcQuestion.setQuestionText("How is the pain?");
        assertFalse(proCtcQuestion.equals(anotherProCtcQuestion));

        anotherProCtcQuestion.setQuestionText("How is the pain?");
        assertEquals(anotherProCtcQuestion.hashCode(), proCtcQuestion.hashCode());
        assertEquals(anotherProCtcQuestion, proCtcQuestion);

    }

}