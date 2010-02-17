package gov.nih.nci.ctcae.core;

import gov.nih.nci.ctcae.core.csv.loader.ProCtcTermsImporterV4;
import gov.nih.nci.ctcae.core.repository.CtcTermRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcRepository;
import gov.nih.nci.ctcae.core.domain.ProCtc;
import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;

import java.io.IOException;
import java.util.Date;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * @author mehul gulati
 *         Date: Jan 19, 2009
 */
public class ProCtcTermsImporterV4Test extends AbstractTransactionalDataSourceSpringContextTests {
    CtcTermRepository ctcTermRepository;
    ProCtcRepository proCtcRepository;

    public void testLoadProCtcTerms() {
        try {
            DataAuditInfo auditInfo = new DataAuditInfo("admin", "localhost", new Date(), "127.0.0.0");
            DataAuditInfo.setLocal(auditInfo);

            ProCtcTermsImporterV4 importerV4 = new ProCtcTermsImporterV4();
            importerV4.setCtcTermRepository(ctcTermRepository);
            ProCtc proCtc = importerV4.loadProCtcTerms(true);
            proCtcRepository.save(proCtc);
            setComplete();
            endTransaction();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static final String[] context = new String[]{
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-util.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-core.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-datasource.xml"
    };

    //    @Override
    protected String[] getConfigLocations() {
        return context;
    }

    public void setCtcTermRepository(CtcTermRepository ctcTermRepository) {
        this.ctcTermRepository = ctcTermRepository;
    }

    public void setProCtcRepository(ProCtcRepository proCtcRepository) {
        this.proCtcRepository = proCtcRepository;
    }
}