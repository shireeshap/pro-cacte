package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.web.table.cell.AbstractCellTestCase;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.bean.Row;

/**
 * @author Vinay Kumar
 * @crated Nov 11, 2008
 */
public class TextBoxCellTest extends AbstractCellTestCase {

    private TextBoxCell cell;

    private Study study;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        study = new Study();
        study.setId(1);
        cell = new TextBoxCell();
        column = new Column(model);
        row = new Row(model);
        model.addRow(row);

        model.addColumn(column);
        model.setCurrentRowBean(study);

    }

    public void testGetHtmlDisplay() {
        assertEquals("must display radio box", "<td><input type=\"text\"  name=\"participantStudyIdentifier1\"  value=\"\" /></td>", cell.getHtmlDisplay(model, column));
    }
}
