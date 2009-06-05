package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;

import java.text.ParseException;

/**
 * @author Vinay Kumar
 * @crated Mar 13, 2009
 */
public class SecurityTestDataIntegrationTest extends AbstractHibernateIntegrationTestCase {
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
    }

    public void testInsertData() throws ParseException {
        insertData();
    }
}
