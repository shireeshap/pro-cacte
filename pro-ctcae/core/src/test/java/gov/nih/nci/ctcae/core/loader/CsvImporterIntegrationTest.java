package gov.nih.nci.ctcae.core.loader;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;

import java.io.IOException;

/**
 * @author mehul gulati
 *         Date: Jan 19, 2009
 */
public class CsvImporterIntegrationTest extends AbstractHibernateIntegrationTestCase {


    public void testSaveCsv() throws IOException {
        saveCsv();
        //  assertNotNull(finderRepository.findById(ProCtc.class, 1));
    }


}
