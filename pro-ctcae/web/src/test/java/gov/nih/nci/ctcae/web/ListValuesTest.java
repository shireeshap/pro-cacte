package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.domain.*;
import junit.framework.TestCase;

import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Dec 11, 2008
 */
public class ListValuesTest extends TestCase {

    private ListValues listValues;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        listValues = new ListValues();

    }

    public void testParticipantSearchType() {
        List<ListValues> participantSearchType = listValues.getParticipantSearchType();
        assertEquals("must be 3 search type", Integer.valueOf(3), Integer.valueOf(participantSearchType.size()));
        ListValues values = participantSearchType.get(0);
    }

    public void testGetterAndSetter() {
        listValues.setCode("code");
        listValues.setDesc("desc");

        assertEquals("code", listValues.getCode());
        assertEquals("desc", listValues.getDesc());
    }

    public void testgetSymptomsForCRF() {
        ProCtcTerm proCtcTerm1 = new ProCtcTerm();
        proCtcTerm1.setTerm("A");
        ProCtcTerm proCtcTerm2 = new ProCtcTerm();
        proCtcTerm2.setTerm("B");

        ProCtcQuestion proCtcQuestion1 = new ProCtcQuestion();
        proCtcQuestion1.setProCtcTerm(proCtcTerm1);
        ProCtcQuestion proCtcQuestion2 = new ProCtcQuestion();
        proCtcQuestion2.setProCtcTerm(proCtcTerm1);
        ProCtcQuestion proCtcQuestion3 = new ProCtcQuestion();
        proCtcQuestion3.setProCtcTerm(proCtcTerm2);

        CrfPageItem crfPageItem1 = new CrfPageItem();
        crfPageItem1.setProCtcQuestion(proCtcQuestion1);
        CrfPageItem crfPageItem2 = new CrfPageItem();
        crfPageItem2.setProCtcQuestion(proCtcQuestion2);
        CrfPageItem crfPageItem3 = new CrfPageItem();
        crfPageItem3.setProCtcQuestion(proCtcQuestion3);

        CRFPage crfPage1 = new CRFPage();
        crfPage1.addCrfPageItem(crfPageItem1);

        CRFPage crfPage2 = new CRFPage();
        crfPage2.addCrfPageItem(crfPageItem2);

        CRFPage crfPage3 = new CRFPage();
        crfPage3.addCrfPageItem(crfPageItem3);

        CRF crf = Fixture.createCrf();

        crf.addCrfPage(crfPage1);
        crf.addCrfPage(crfPage2);
        crf.addCrfPage(crfPage3);

        List lv = ListValues.getSymptomsForCRF(crf);

        assertEquals(3, lv.size());


    }
}
