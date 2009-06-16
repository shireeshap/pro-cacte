package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

import java.util.Map;

/**
 * @author Mehul Gulati
 *         Date: Nov 6, 2008
 */
public class CrfAjaxFacadeIntegrationTest extends AbstractWebIntegrationTestCase {

    private CrfAjaxFacade crfAjaxFacade;
    protected Map parameterMap;
    private CRF crf;


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();


        crf = Fixture.createCrf("Form1", CrfStatus.DRAFT, "1.1");

        crf.setStudy(defaultStudy);

        crf = crfRepository.save(crf);

    }

    public void testSearchCRF() {
        String table = crfAjaxFacade.searchCrf(parameterMap, defaultStudy.getId(), request);
        assertNotNull(table);
        assertTrue("must find a crf with title", table.contains(crf.getTitle()));
        assertTrue("must find a crf with status", table.contains(crf.getStatus().toString()));
    }

    public void setCRFAjaxFacade(CrfAjaxFacade crfAjaxFacade) {
        this.crfAjaxFacade = crfAjaxFacade;
    }

}
