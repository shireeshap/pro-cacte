package gov.nih.nci.ctcae.core;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import java.util.Collection;
import java.util.Date;

public abstract class AbstractHibernateIntegrationTestCase extends AbstractTransactionalDataSourceSpringContextTests {

    protected FinderRepository finderRepository;
    protected ProCtcTermRepository proCtcTermRepository;

    @Override
    protected String[] getConfigLocations() {
        return new String[]{
                "classpath*:gov/nih/nci/ctcae/core/applicationContext-util.xml",

                "classpath*:gov/nih/nci/ctcae/core/applicationContext-core.xml",
//                "classpath*:gov/nih/nci/ctcae/core/applicationContext-mail.xml",
                "classpath*:gov/nih/nci/ctcae/core/applicationContext-datasource.xml",
                "classpath*:gov/nih/nci/ctcae/core/resourceContext-job.xml",
//                "classpath*:gov/nih/nci/ctcae/core/applicationContext-core-security.xml",
                "classpath*:" + "/*-context-test.xml"};
    }


    protected void login() {
//        user = userRepository.loadUserByUsername("saurabh1@abc.com");
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, "password");
//
//        SecurityContextHolder.getContext().setAuthentication(token);


    }

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        DataAuditInfo auditInfo = new DataAuditInfo("admin", "localhost", new Date(), "127.0.0.0");
        DataAuditInfo.setLocal(auditInfo);

        Collection<ProCtcTerm> proCtcTerms = proCtcTermRepository.find(new ProCtcTermQuery());
        if (proCtcTerms.isEmpty()) {
            //create a new pro ctc term

            ProCtcTerm proCtcTerm = new ProCtcTerm();
            proCtcTerm.setCtcTerm(finderRepository.findById(CtcTerm.class, 3141));
            proCtcTerm.setProCtc(finderRepository.findById(ProCtc.class, 1));
            proCtcTerm.setTerm("Anxiety");

            ProCtcQuestion proCtcQuestion = new ProCtcQuestion();
            proCtcQuestion.setProCtcQuestionType(ProCtcQuestionType.SEVERITY);
            proCtcQuestion.setQuestionText("test question");
            proCtcQuestion.setDisplayOrder(0);
            proCtcTerm.addProCtcQuestion(proCtcQuestion);

            ProCtcValidValue proCtcValidValue1 = new ProCtcValidValue();
            proCtcValidValue1.setValue("value1");
            proCtcQuestion.addValidValue(proCtcValidValue1);

            ProCtcValidValue proCtcValidValue2 = new ProCtcValidValue();
            proCtcValidValue2.setValue("value1");
            proCtcQuestion.addValidValue(proCtcValidValue2);


            proCtcTermRepository.save(proCtcTerm);
            setComplete();
            endTransaction();
            startNewTransaction();
        }

    }

    @Required
    public void setFinderRepository(final FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }

    @Override
    protected void onTearDownInTransaction() throws Exception {
        DataAuditInfo.setLocal(null);

        super.onTearDownInTransaction();


    }

    @Required
    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }
}
