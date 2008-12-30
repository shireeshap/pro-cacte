package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyCrf;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

import java.util.Map;

/**
 * @author Mehul Gulati
 *         Date: Nov 6, 2008
 */
public class CrfAjaxFacadeIntegrationTest extends AbstractWebIntegrationTestCase {

    private CrfAjaxFacade crfAjaxFacade;
    protected Map parameterMap;
    private CRFRepository crfRepository;
    private Study study;
    private CRF crf;
    private StudyRepository studyRepository;


    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();


        crf = Fixture.createCrf("Form1", CrfStatus.DRAFT, "1.1");
        crf = crfRepository.save(crf);

        study = Fixture.createStudy("Short Title", "Long Title", "1");
        StudyCrf studyCrf = new StudyCrf();
        studyCrf.setCrf(crf);
        crf.setStudyCrf(studyCrf);
        study.addStudyCrf(studyCrf);
        study = studyRepository.save(study);

    }

    public void testSearchStudyCrf() {
        String table = crfAjaxFacade.searchCrf(parameterMap, study.getId(), request);
        assertNotNull(table);
        assertTrue("must find a crf with title", table.contains(crf.getTitle()));
        assertTrue("must find a crf with status", table.contains(crf.getStatus().toString()));
    }

    public void setStudyCrfAjaxFacade(CrfAjaxFacade crfAjaxFacade) {
        this.crfAjaxFacade = crfAjaxFacade;
    }

    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }
}
