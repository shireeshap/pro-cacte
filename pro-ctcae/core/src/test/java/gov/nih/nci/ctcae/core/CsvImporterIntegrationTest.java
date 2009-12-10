package gov.nih.nci.ctcae.core;

import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;

import java.util.Date;

/**
 * @author mehul gulati
 *         Date: Jan 19, 2009
 */
public class CsvImporterIntegrationTest extends TestDataManager {

    @Override
    protected void onSetUpInTransaction() throws Exception {
        try{
        DataAuditInfo auditInfo = new DataAuditInfo("admin", "localhost", new Date(), "127.0.0.0");
        DataAuditInfo.setLocal(auditInfo);
        login(SYSTEM_ADMIN);
        }catch(Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    public void testSaveCsv() throws Exception {
        try{
        saveCsv(true);
        }catch(Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }
}
