package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;

import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Nov 18, 2008
 */
public class FinderRepositoryIntegrationTest extends AbstractHibernateIntegrationTestCase {

    private FinderRepository finderRepository;
    private CRF crf;
    private Study study;
    private ProCtcQuestionRepository proCtcQuestionRepository;


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        super.onSetUpInTransaction(); // To change body of overridden methods
        // use File | Settings | File Templates.
        login();

        study = createStudy();

        study = studyRepository.save(study);


        crf = new CRF();
        crf.setTitle("Cancer CRF");
        crf.setDescription("Case Report Form for Cancer Patients");
        crf.setStatus(CrfStatus.DRAFT);
        crf.setCrfVersion("1.0");
        crf.setStudy(study);
        crf = crfRepository.save(crf);


    }

    private Study createStudy() {
        Study study = new Study();
        study.setShortTitle("study short title");
        study.setLongTitle("study long title");
        study.setAssignedIdentifier("assigned identifier");
        return study;
    }

    public void testFindbyId() {
        ProCtcQuestion question = proCtcQuestionRepository.find(new ProCtcQuestionQuery()).iterator().next();
        assertNotNull(question);

        ProCtcQuestion proCtcQuestion = finderRepository.findById(ProCtcQuestion.class, question.getId());
        assertNotNull(proCtcQuestion);
    }


    public void testFind() {
        ProCtcTermQuery query = new ProCtcTermQuery();
        List<? extends Persistable> list = finderRepository.find(query);
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }


    public void setFinderRepository(final FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }

    public void setProCtcQuestionRepository(ProCtcQuestionRepository proCtcQuestionRepository) {
        this.proCtcQuestionRepository = proCtcQuestionRepository;
    }
}
