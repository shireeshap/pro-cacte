package gov.nih.nci.ctcae.core;

import edu.nwu.bioinformatics.commons.testing.CoreTestCase;
import gov.nih.nci.ctcae.core.domain.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easymock.classextension.EasyMock;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Vinay Kumar
 */
public abstract class AbstractTestCase extends CoreTestCase {

    protected ProCtcQuestion proCtcQuestion1, proCtcQuestion2, proCtcQuestion3, proCtcQuestion4, proCtcQuestion5, proCtcQuestion6, proCtcQuestion7, proCtcQuestion8;

    protected Log logger = LogFactory.getLog(getClass());
    protected ProCtcTerm proCtcTerm1, proCtcTerm2, proCtcTerm3;

    @Override
    protected void setUp() throws Exception {
        logger.debug("---- Begin test " + getName() + " ----");
        super.setUp();



        proCtcTerm1 = new ProCtcTerm();
        proCtcTerm1.setTerm("Fatigue");

        proCtcTerm2 = new ProCtcTerm();
        proCtcTerm2.setTerm("Pain");

        proCtcTerm3 = new ProCtcTerm();
        proCtcTerm3.setTerm("Cough");

        proCtcQuestion1 = new ProCtcQuestion();
        proCtcQuestion1.setQuestionText("first question");
        proCtcQuestion1.setId(1);

        proCtcQuestion2 = new ProCtcQuestion();
        proCtcQuestion2.setQuestionText("second question");
        proCtcQuestion2.setId(2);

        proCtcQuestion3 = new ProCtcQuestion();
        proCtcQuestion3.setQuestionText("third question");
        proCtcQuestion3.setId(3);

        proCtcQuestion4 = new ProCtcQuestion();
        proCtcQuestion4.setQuestionText("fourth question");
        proCtcQuestion4.setId(4);

        proCtcQuestion5 = new ProCtcQuestion();
        proCtcQuestion5.setQuestionText("fifth question");
        proCtcQuestion5.setId(5);

        proCtcQuestion6 = new ProCtcQuestion();
        proCtcQuestion6.setQuestionText("6th question");
        proCtcQuestion6.setId(6);

        proCtcQuestion7 = new ProCtcQuestion();
        proCtcQuestion7.setQuestionText("seventh question");
        proCtcQuestion7.setId(7);

        proCtcQuestion8 = new ProCtcQuestion();
        proCtcQuestion8.setQuestionText("8th question");
        proCtcQuestion8.setId(8);

        proCtcTerm1.addProCtcQuestion(proCtcQuestion1);
        proCtcTerm1.addProCtcQuestion(proCtcQuestion2);
        proCtcTerm2.addProCtcQuestion(proCtcQuestion3);
        proCtcTerm2.addProCtcQuestion(proCtcQuestion4);
        proCtcTerm3.addProCtcQuestion(proCtcQuestion5);
        proCtcTerm3.addProCtcQuestion(proCtcQuestion6);
        proCtcTerm3.addProCtcQuestion(proCtcQuestion7);
        proCtcTerm3.addProCtcQuestion(proCtcQuestion8);

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        logger.debug("----  End  test " + getName() + " ----");
    }


    public static void assertEqualsAndNotSame(Object expected, Object actual) {
        assertEqualsAndNotSame(null, expected, actual);
    }

    protected Set<Object> mocks = new HashSet<Object>();

    ////// MOCK REGISTRATION AND HANDLING

    public <T> T registerMockFor(Class<T> forClass) {
        return registered(EasyMock.createMock(forClass));
    }


//    public <T extends AbstractRepository<?,?>> T registerRepositoryMockFor(Class<T> forClass) {
//        List<Method> methods = new LinkedList<Method>(Arrays.asList(forClass.getMethods()));
//        for (Iterator<Method> iterator = methods.iterator(); iterator.hasNext();) {
//            Method method = iterator.next();
//            if ("getPersistableClass".equals(method.getName())) {
//                iterator.remove();
//            }
//        }
//        return registerMockFor(forClass, methods.toArray(new Method[methods.size()]));
//    }

    public void replayMocks() {
        for (Object mock : mocks) EasyMock.replay(mock);
    }

    public void verifyMocks() {
        for (Object mock : mocks) EasyMock.verify(mock);
    }

    public void resetMocks() {
        for (Object mock : mocks) EasyMock.reset(mock);
    }

    private <T> T registered(T mock) {
        mocks.add(mock);
        return mock;
    }

    private void validateCrfPageItemDisplayOrder(final CRF crf) {

        for (CRFPage crfPage : crf.getCrfPagesSortedByPageNumber()) {

            for (int i = 0; i < crfPage.getCrfPageItems().size(); i++) {
                assertEquals("must preserve order no", Integer.valueOf(i + CrfPageItem.INITIAL_ORDER), crfPage.getCrfPageItems().get(i).getDisplayOrder());

            }
        }
    }

    public void validateCrfPageAndCrfPageItemOrder(final CRF crf) {
        if (crf != null) {
            validateCrfPageItemDisplayOrder(crf);
            verifyCrfPageNumber(crf);
        }
    }


    private void verifyCrfPageNumber(final CRF crf) {
        for (int i = 0; i < crf.getCrfPagesSortedByPageNumber().size(); i++) {
            CRFPage crfPage = crf.getCrfPagesSortedByPageNumber().get(i);
            assertEquals("must preserve crf page number", Integer.valueOf(i), crfPage.getPageNumber());


        }
    }

}
