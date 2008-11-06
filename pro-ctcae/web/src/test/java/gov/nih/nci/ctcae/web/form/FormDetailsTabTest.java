package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CtcCategory;
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

    private ProCtcTerm proCtcTerm1, proCtcTerm2, proCtcTerm3, proCtcTerm4;

    private CtcCategory ctcCategory1, ctcCategory2, ctcCategory3;

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

        proCtcTerm1 = new ProCtcTerm();
        proCtcTerm1.setCategory(ctcCategory1);
        proCtcTerm2 = new ProCtcTerm();
        proCtcTerm2.setCategory(ctcCategory1);
        proCtcTerm3 = new ProCtcTerm();
        proCtcTerm3.setCategory(ctcCategory2);
        proCtcTerm4 = new ProCtcTerm();
        proCtcTerm4.setCategory(ctcCategory3);

        command = new CreateFormCommand();
        proCtcTerms.add(proCtcTerm1);
        proCtcTerms.add(proCtcTerm2);
        proCtcTerms.add(proCtcTerm3);
        proCtcTerms.add(proCtcTerm4);
    }

    public void testRefereceData() {
        expect(proCtcTermRepository.findAndInitializeTerm(isA(ProCtcTermQuery.class))).andReturn(proCtcTerms);
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
        assertEquals("must sort by name ", ctcCategory2, ctcCategory);
        assertEquals("must keep all terms", Integer.valueOf(1), Integer.valueOf(ctcCategoryMap.get(ctcCategory2).size()));

        ctcCategory = ctcCategoryIterator.next();
        assertEquals("must sort by name ", ctcCategory3, ctcCategory);
        assertEquals("must keep all terms", Integer.valueOf(1), Integer.valueOf(ctcCategoryMap.get(ctcCategory3).size()));

        ctcCategory = ctcCategoryIterator.next();
        assertEquals("must sort by name ", ctcCategory1, ctcCategory);
        assertEquals("must keep all terms", Integer.valueOf(2), Integer.valueOf(ctcCategoryMap.get(ctcCategory1).size()));

    }
}
