package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CtcCategory;
import gov.nih.nci.ctcae.core.domain.CtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

import java.util.*;

/**
 * @author Vinay Kumar
 * @crated Nov 6, 2008
 */
public class FormDetailsTabTest extends WebTestCase {
    private FormDetailsTab tab;
    private ProCtcTermRepository proCtcTermRepository;
    private FinderRepository finderRepository;
    private CreateFormCommand command;
    private Collection<ProCtcTerm> proCtcTerms;

    private ProCtcTerm proCtcTerm1, proCtcTerm2, proCtcTerm3, proCtcTerm4, proCtcTerm5;

    private CtcCategory ctcCategory1, ctcCategory2, ctcCategory3;
    protected CtcTerm ctcTerm1, ctcTerm2, ctcTerm3, ctcTerm4;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        proCtcTermRepository = registerMockFor(ProCtcTermRepository.class);
        finderRepository = registerMockFor(FinderRepository.class);
        tab = new FormDetailsTab();

        tab.setFinderRepository(finderRepository);
        tab.setProCtcTermRepository(proCtcTermRepository);

        proCtcTerms = new ArrayList<ProCtcTerm>();

        ctcCategory1 = new CtcCategory();
        ctcCategory1.setName("BLOOD/BONE MARROW");

        ctcCategory2 = new CtcCategory();
        ctcCategory2.setName("ALLERGY/IMMUNOLOGY");

        ctcCategory3 = new CtcCategory();
        ctcCategory3.setName("AUDITORY/EAR");

        ctcTerm1 = new CtcTerm();
        ctcTerm1.setCategory(ctcCategory1);

        ctcTerm2 = new CtcTerm();
        ctcTerm2.setCategory(ctcCategory2);

        ctcTerm3 = new CtcTerm();
        ctcTerm3.setCategory(ctcCategory3);

        ctcTerm4 = new CtcTerm();
        ctcTerm4.setCategory(ctcCategory3);

        proCtcTerm1 = new ProCtcTerm();
        proCtcTerm2 = new ProCtcTerm();
        proCtcTerm3 = new ProCtcTerm();
        proCtcTerm4 = new ProCtcTerm();
        proCtcTerm5 = new ProCtcTerm();

        proCtcTerm1.setCtcTerm(ctcTerm3);
        proCtcTerm2.setCtcTerm(ctcTerm2);

        proCtcTerm3.setCtcTerm(ctcTerm1);
        proCtcTerm3.setTerm("Itchy skin ");
        proCtcTerm4.setCtcTerm(ctcTerm1);
        proCtcTerm4.setTerm("Difficulty sleeping (insomnia) ");
        proCtcTerm5.setCtcTerm(ctcTerm1);
        proCtcTerm5.setTerm("Fatigue");

        command = new CreateFormCommand();
        proCtcTerms.add(proCtcTerm1);
        proCtcTerms.add(proCtcTerm2);
        proCtcTerms.add(proCtcTerm3);
        proCtcTerms.add(proCtcTerm4);
        proCtcTerms.add(proCtcTerm5);
    }

    public void testRefereceData() {
        expect(proCtcTermRepository.find(isA(ProCtcTermQuery.class))).andReturn(proCtcTerms);
        replayMocks();
        Map<String, Object> map = tab.referenceData(command);
        verifyMocks();
        assertFalse("must not be null", map.keySet().isEmpty());
        Object object = map.get("ctcCategoryMap");
        assertNotNull("must find ctc category", object);
        assertTrue(object instanceof LinkedHashMap);
        LinkedHashMap<CtcCategory, List<ProCtcTerm>> ctcCategoryMap = (LinkedHashMap<CtcCategory, List<ProCtcTerm>>) object;

        Iterator<CtcCategory> ctcCategoryIterator = ctcCategoryMap.keySet().iterator();
        CtcCategory ctcCategory = ctcCategoryIterator.next();
        assertSame("must sort by name ", ctcCategory2, ctcCategory);
        assertEquals("must keep all terms", Integer.valueOf(1), Integer.valueOf(ctcCategoryMap.get(ctcCategory2).size()));

        ctcCategory = ctcCategoryIterator.next();
        assertSame("must sort by name ", ctcCategory3, ctcCategory);
        assertEquals("must keep all terms", Integer.valueOf(1), Integer.valueOf(ctcCategoryMap.get(ctcCategory3).size()));

        ctcCategory = ctcCategoryIterator.next();
        assertSame("must sort by name ", ctcCategory1, ctcCategory);

        List<ProCtcTerm> ctcTermList = ctcCategoryMap.get(ctcCategory1);

        assertEquals("must keep all terms", Integer.valueOf(3), Integer.valueOf(ctcTermList.size()));
        assertSame("must sort by name ", proCtcTerm4, ctcTermList.get(0));
        assertSame("must sort by name ", proCtcTerm5, ctcTermList.get(1));
        assertSame("must sort by name ", proCtcTerm3, ctcTermList.get(2));


    }
}
