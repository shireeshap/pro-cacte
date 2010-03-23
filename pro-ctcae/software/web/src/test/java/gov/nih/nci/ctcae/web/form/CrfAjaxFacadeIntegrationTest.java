package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Map;

/**
 * @author Mehul Gulati
 * Date: Nov 6, 2008
 */
public class CrfAjaxFacadeIntegrationTest extends AbstractWebIntegrationTestCase {

    private CrfAjaxFacade crfAjaxFacade;
    protected Map parameterMap;

    public void testSearchCRF() throws Exception {
        List<CRF> list = crfAjaxFacade.searchCrf(StudyTestHelper.getDefaultStudy().getId());
        assertNotNull(list);
        assertTrue(list.size()>0);
    }

    public void testGetSymptomsForCrf_GetAttributesForSymptom() {
        CRF crf = StudyTestHelper.getDefaultStudy().getCrfs().get(0);
        List<ProCtcTerm> proCtcTerms = crfAjaxFacade.getSymptomsForCrf(crf.getId());
        assertEquals(10, proCtcTerms.size());

        Integer id = proCtcTerms.get(1).getId();
        List<String> attributes = crfAjaxFacade.getAttributesForSymptom(id);
        assertEquals(3, attributes.size());
        assertEquals("Frequency", attributes.get(0));

    }

    @Required
    public void setCRFAjaxFacade(CrfAjaxFacade crfAjaxFacade) {
        this.crfAjaxFacade = crfAjaxFacade;
    }

}
