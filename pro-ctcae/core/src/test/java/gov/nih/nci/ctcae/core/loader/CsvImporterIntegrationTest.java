package gov.nih.nci.ctcae.core.loader;

import gov.nih.nci.ctcae.core.helper.TestDataManager;

import java.io.IOException;

/**
 * @author mehul gulati
 *         Date: Jan 19, 2009
 */
public class CsvImporterIntegrationTest extends TestDataManager {
    public void testSaveCsv() throws IOException {
        saveCsv();
    }
}
