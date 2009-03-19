package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.web.table.cell.AbstractCellTestCase;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.bean.Row;

/**
 * @author Mehul Gulati
 *         Date: Nov 5, 2008
 */
public class CRFLinkDisplayDetailsCellTest extends AbstractCellTestCase {

    private CRFLinkDisplayDetailsCell crfLinkDisplayDetailsCell;
    private Column testOptionsColumn;
    private CRF crf = new CRF();


    @Override
    protected void setUp() throws Exception {
        super.setUp();

        crfLinkDisplayDetailsCell = new CRFLinkDisplayDetailsCell();
        testOptionsColumn = new Column(model);
        //  testOptionsColumn.setCell("gov.nih.nci.ctcae.web.form.CRFLinkDisplayDetailsCell");
        //  testOptionsColumn.setTitle("Options");

    }


    public void testScheduleFormStatusLink() {
        crf = new CRF();
        crf.setId(1);

        crf.setStatus(CrfStatus.RELEASED);
        model.addColumn(testOptionsColumn);
        row = new Row(model);
        model.addRow(row);
        model.setCurrentRowBean(crf);


        assertNotNull(crfLinkDisplayDetailsCell.getCellValue(model, testOptionsColumn));
    }

    public void testReleaseFormStatusLink() {
        crf = new CRF();
        crf = new CRF();
        crf.setTitle("title");
        crf.setId(1);

        crf.setStatus(CrfStatus.DRAFT);
        model.addColumn(testOptionsColumn);
        row = new Row(model);
        model.addRow(row);
        model.setCurrentRowBean(crf);


        assertNotNull(crfLinkDisplayDetailsCell.getCellValue(model, testOptionsColumn));
    }


}


