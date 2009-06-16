package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.ProCtcQuery;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class CrfItemIntegrationTest extends TestDataManager {


    private ProCtcQuestionRepository proCtcQuestionRepository;
    private CRF crf;
    private CRFPage crfPage;
    private ProCtcQuestion proCtcQuestion;
    private ProCtc proCtc;
    private ProCtcTerm proProCtcTerm;
    private CrfPageItem crfPageItem, invalidCrfPageItem;


    private CrfPageItemDisplayRule crfPageItemDisplayRule, anotherCrfPageItemDisplayRule;
    private Study study;
    protected ProCtcValidValue proCtcValidValue1;
    protected ProCtcValidValue proCtcValidValue2;

    @Override
    protected void onSetUpInTransaction() throws Exception {


        super.onSetUpInTransaction();
        crf = Fixture.createCrf();
        crfPage = new CRFPage();
        proCtc = proCtcRepository.find(new ProCtcQuery()).iterator().next();
        assertNotNull(proCtc);

        proProCtcTerm = proCtcTermRepository.find(new ProCtcTermQuery()).iterator().next();
        assertNotNull(proProCtcTerm);


        Collection<ProCtcQuestion> questions = proCtcQuestionRepository.find(new ProCtcQuestionQuery());
        proCtcQuestion = questions.iterator().next();

        crfPageItem = new CrfPageItem();
        crfPageItem.setCrfPage(crfPage);
        crfPageItem.setProCtcQuestion(proCtcQuestion);
        crfPageItem.setDisplayOrder(1);
        crfPageItem.setCrfItemAllignment(CrfItemAllignment.HORIZONTAL);
        crfPageItem.setInstructions("instructions");
        crfPageItem.setResponseRequired(Boolean.TRUE);

        crfPage.addCrfPageItem(crfPageItem);
        crf.addCrfPage(crfPage);

        Collection<ProCtcValidValue> proCtcValidValues = proProCtcTerm.getProCtcQuestions().iterator().next().getValidValues();
        Iterator<ProCtcValidValue> iterator = proCtcValidValues.iterator();
        proCtcValidValue1 = iterator.next();
        proCtcValidValue2 = iterator.next();


        crfPageItemDisplayRule = Fixture.createCrfPageItemDisplayRules(null, proCtcValidValue1);
        anotherCrfPageItemDisplayRule = Fixture.createCrfPageItemDisplayRules(null, proCtcValidValue2);

        study = new Study();
        study.setShortTitle("study short title");
        study.setLongTitle("study long title");
        study.setAssignedIdentifier("assigned identifier");

    }


    private CRF saveCrfItemWithDisplayRule() {
        study = studyRepository.save(study);
        crf.setStudy(study);

        crfPageItem.addCrfPageItemDisplayRules(crfPageItemDisplayRule);
        crf = crfRepository.save(crf);
        return crf;
    }

    public void testSaveCrfItem() {
        study = studyRepository.save(study);
        crf.setStudy(study);

        crf = crfRepository.save(crf);
        crfPageItem = crfPage.getCrfPageItems().iterator().next();

        assertNotNull(crfPageItem.getId());
        assertEquals(CrfItemAllignment.HORIZONTAL, crfPageItem.getCrfItemAllignment());
        assertEquals("instructions", crfPageItem.getInstructions());
        assertTrue(crfPageItem.getResponseRequired());

    }


    public void testAddCrfPageItemDisplayRuleInCreateCrfItem() {

        crf = saveCrfItemWithDisplayRule();
        crfPageItem = crfPage.getCrfPageItems().iterator().next();

        CrfPageItemDisplayRule savedCrfPageItemDisplayRule = crfPageItem.getCrfPageItemDisplayRules().iterator().next();
        assertNotNull(savedCrfPageItemDisplayRule.getId());
        assertEquals(crfPageItem, savedCrfPageItemDisplayRule.getCrfItem());
        assertEquals(proCtcValidValue1, savedCrfPageItemDisplayRule.getProCtcValidValue());
    }

    public void testAddCrfPageItemDisplayRuleInEditCrfItem() {

        crf = saveCrfItemWithDisplayRule();
        crfPageItem = crfPage.getCrfPageItems().iterator().next();
        assertNotNull(crfPageItem.getId());

        assertTrue("must add another display rules", crfPageItem.addCrfPageItemDisplayRules(anotherCrfPageItemDisplayRule));
        crf = crfRepository.save(crf);
        crfPageItem = crfPage.getCrfPageItems().iterator().next();
        assertFalse("must save crf item display rule", crfPageItem.getCrfPageItemDisplayRules().isEmpty());
        assertEquals(Integer.valueOf(2), Integer.valueOf(crfPageItem.getCrfPageItemDisplayRules().size()));
        for (CrfPageItemDisplayRule savedCrfPageItemDisplayRule : crfPageItem.getCrfPageItemDisplayRules()) {
            assertNotNull(crfPageItemDisplayRule.getId());
            assertEquals(crfPageItem, savedCrfPageItemDisplayRule.getCrfItem());
        }

    }


    public void testDeleteCrfPageItemDisplayRule() {


        crf = saveCrfItemWithDisplayRule();
        crfPageItem = crfPage.getCrfPageItems().iterator().next();

        assertNotNull(crfPageItem.getId());
        Integer id = crfPageItem.getCrfPageItemDisplayRules().iterator().next().getProCtcValidValue().getId();
        assertNotNull(id);

        crfPageItem.removeCrfPageItemDisplayRulesByProCtcValidValueIds(String.valueOf(id));
        crf = crfRepository.save(crf);
        crfPageItem = crfPage.getCrfPageItems().iterator().next();
        assertNotNull(crfPageItem.getId());
        assertTrue("must remove crf item display rule", crfPageItem.getCrfPageItemDisplayRules().isEmpty());


    }


    public void testSavingNullCrfItem() {
        invalidCrfPageItem = new CrfPageItem();
        crfPage.addCrfPageItem(invalidCrfPageItem);
        crf.addCrfPage(crfPage);
        try {
            crfRepository.save(crf);
            fail("Expected CtcAeSystemException because title, status and formVersion are null");
        } catch (CtcAeSystemException e) {
        }
    }


    public void testSavingNullProCtcTermCrfItem() {
        invalidCrfPageItem = new CrfPageItem();
        try {
            invalidCrfPageItem.setCrfPage(crfPage);
            invalidCrfPageItem.setDisplayOrder(1);
            crfPage.addCrfPageItem(invalidCrfPageItem);
            crf.addCrfPage(crfPage);
            crfRepository.save(crf);
            fail("Expected CtcAeSystemException because ProCtcQuestion is null");
        } catch (CtcAeSystemException e) {
        }
    }

    public void testSavingNullDisplayOrderCrfItem() {
        invalidCrfPageItem = new CrfPageItem();
        try {
            invalidCrfPageItem.setCrfPage(crfPage);
            invalidCrfPageItem.setDisplayOrder(null);
            invalidCrfPageItem.setProCtcQuestion(proCtcQuestion);
            crfPage.addCrfPageItem(invalidCrfPageItem);
            crf.addCrfPage(crfPage);

            crfRepository.save(crf);
            fail("Expected CtcAeSystemException because DisplayOrder is null");
        } catch (CtcAeSystemException e) {
        }
    }


    public void setProCtcQuestionRepository(ProCtcQuestionRepository proCtcQuestionRepository) {
        this.proCtcQuestionRepository = proCtcQuestionRepository;
    }


}
