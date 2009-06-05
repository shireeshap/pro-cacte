package gov.nih.nci.ctcae.core.loader;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.helper.GenerateTestDataTest;

import java.io.IOException;

/**
 * @author mehul gulati
 *         Date: Jan 19, 2009
 */
public class CsvImporterIntegrationTest extends GenerateTestDataTest {
    public void testSaveCsv() throws IOException {
        saveCsv();
    }
}
