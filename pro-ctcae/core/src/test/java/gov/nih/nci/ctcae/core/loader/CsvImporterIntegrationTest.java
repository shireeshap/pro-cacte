package gov.nih.nci.ctcae.core.loader;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.csv.loader.CsvImporter;
import gov.nih.nci.ctcae.core.domain.ProCtc;
import gov.nih.nci.ctcae.core.repository.GenericRepository;

import java.io.IOException;

/**
 * @author mehul gulati
 *         Date: Jan 19, 2009
 */
public class CsvImporterIntegrationTest extends AbstractHibernateIntegrationTestCase {

    private CsvImporter csvImporter;
    private GenericRepository genericRepository;

    @Override
    protected void onSetUpInTransaction() throws Exception {

        super.onSetUpInTransaction();
    }

    private void saveCsv() throws IOException {

        //ProCtc pro = finderRepository.findById(ProCtc.class, 1);
        //genericRepository.delete(pro);

        setComplete();
        endTransaction();
        startNewTransaction();

        csvImporter = new CsvImporter();
        csvImporter.setFinderRepository(finderRepository);
        ProCtc proctc = csvImporter.readCsv();
        //assertEquals(65,proctc.getProCtcTerms().size());
        genericRepository.save(proctc);
        setComplete();
        endTransaction();
        startNewTransaction();
    }

    public void testSaveCsv() throws IOException {
        saveCsv();
        //  assertNotNull(finderRepository.findById(ProCtc.class, 1));
    }


    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

}
