package gov.nih.nci.ctcae.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easymock.classextension.EasyMock;

import edu.nwu.bioinformatics.commons.testing.CoreTestCase;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CRFPage;
import gov.nih.nci.ctcae.core.domain.CrfPageItem;
import gov.nih.nci.ctcae.core.domain.CtcCategory;
import gov.nih.nci.ctcae.core.domain.CtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;

/**
 * @author Vinay Kumar
 */
public abstract class AbstractTestCase extends CoreTestCase {

    protected ProCtcQuestion proCtcQuestion1, proCtcQuestion2, proCtcQuestion3, proCtcQuestion4, proCtcQuestion5, proCtcQuestion6, proCtcQuestion7, proCtcQuestion8;

    protected Log logger = LogFactory.getLog(getClass());
    protected ProCtcTerm proCtcTerm1, proCtcTerm2, proCtcTerm3;
    protected ArrayList<ProCtcTerm> proCtcTerms = new ArrayList<ProCtcTerm>();

    @Override
    protected void setUp() throws Exception {
        logger.debug("---- Begin test " + getName() + " ----");
        super.setUp();
        CtcCategory ctcCategory = new CtcCategory();
        ctcCategory.setName("ctccategory");
        CtcTerm ctcTerm = new CtcTerm();
        ctcTerm.setTerm("ctc", SupportedLanguageEnum.ENGLISH);

        proCtcTerm1 = new ProCtcTerm();
        proCtcTerm1.setTermEnglish("Fatigue", SupportedLanguageEnum.ENGLISH);
        proCtcTerm1.setCtcTerm(ctcTerm);

        proCtcTerm2 = new ProCtcTerm();
        proCtcTerm2.setTermEnglish("Pain", SupportedLanguageEnum.ENGLISH);
        proCtcTerm2.setCtcTerm(ctcTerm);

        proCtcTerm3 = new ProCtcTerm();
        proCtcTerm3.setTermEnglish("Cough", SupportedLanguageEnum.ENGLISH);
        proCtcTerm3.setCtcTerm(ctcTerm);

        proCtcQuestion1 = new ProCtcQuestion();
        proCtcQuestion1.setQuestionText("first question", SupportedLanguageEnum.ENGLISH);
        proCtcQuestion1.setId(1);

        proCtcQuestion2 = new ProCtcQuestion();
        proCtcQuestion2.setQuestionText("second question", SupportedLanguageEnum.ENGLISH);
        proCtcQuestion2.setId(2);

        proCtcQuestion3 = new ProCtcQuestion();
        proCtcQuestion3.setQuestionText("third question", SupportedLanguageEnum.ENGLISH);
        proCtcQuestion3.setId(3);

        proCtcQuestion4 = new ProCtcQuestion();
        proCtcQuestion4.setQuestionText("fourth question", SupportedLanguageEnum.ENGLISH);
        proCtcQuestion4.setId(4);

        proCtcQuestion5 = new ProCtcQuestion();
        proCtcQuestion5.setQuestionText("fifth question", SupportedLanguageEnum.ENGLISH);
        proCtcQuestion5.setId(5);

        proCtcQuestion6 = new ProCtcQuestion();
        proCtcQuestion6.setQuestionText("6th question", SupportedLanguageEnum.ENGLISH);
        proCtcQuestion6.setId(6);

        proCtcQuestion7 = new ProCtcQuestion();
        proCtcQuestion7.setQuestionText("seventh question", SupportedLanguageEnum.ENGLISH);
        proCtcQuestion7.setId(7);

        proCtcQuestion8 = new ProCtcQuestion();
        proCtcQuestion8.setQuestionText("8th question", SupportedLanguageEnum.ENGLISH);
        proCtcQuestion8.setId(8);

        proCtcTerm1.addProCtcQuestion(proCtcQuestion1);
        proCtcTerm1.addProCtcQuestion(proCtcQuestion2);
        proCtcTerm2.addProCtcQuestion(proCtcQuestion3);
        proCtcTerm2.addProCtcQuestion(proCtcQuestion4);
        proCtcTerm3.addProCtcQuestion(proCtcQuestion5);
        proCtcTerm3.addProCtcQuestion(proCtcQuestion6);
        proCtcTerm3.addProCtcQuestion(proCtcQuestion7);
        proCtcTerm3.addProCtcQuestion(proCtcQuestion8);

        proCtcTerms.add(proCtcTerm1);
        proCtcTerms.add(proCtcTerm2);
        proCtcTerms.add(proCtcTerm3);

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
