package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.web.table.AbstractTableModelTestCase;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Mehul Gulati
 *         Date: Dec 31, 2008
 */
public class crfTableModelTest extends AbstractTableModelTestCase {

    private CrfTableModel model;
    private CRF crf1, crf2;
    private Collection<CRF> crfs;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        model = new CrfTableModel();

        crf1 = Fixture.createCrf("form1", CrfStatus.DRAFT, "1");
        crf1.setId(10);
        CRF crf = new CRF();
        crf.setId(1);
        crf2 = Fixture.createCrf("form2", CrfStatus.RELEASED, "2");
        crf2.setId(11);
        crfs = new ArrayList<CRF>();
        crfs.add(crf1);
        crfs.add(crf2);

    }

    public void testCreateTable() {
        String table = model.buildCrfTable(parameterMap, crfs, request);
        System.out.println(table);
        validateTable(table);
    }
}
