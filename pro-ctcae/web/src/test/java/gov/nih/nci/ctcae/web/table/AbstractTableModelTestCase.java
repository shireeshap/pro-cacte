package gov.nih.nci.ctcae.web.table;


import gov.nih.nci.ctcae.web.WebTestCase;

import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Oct 18, 2008
 */
public abstract class AbstractTableModelTestCase extends WebTestCase {
    protected Map parameterMap;

    protected void validateTable(String table) {
        assertNotNull("table must not be null", table);
        assertTrue("table must contains table id", table.contains("table id=\"ajaxTable"));
        assertTrue("table id and form id must not be same..check the form id", table.contains("buildTable('assembler')"));

    }
}
