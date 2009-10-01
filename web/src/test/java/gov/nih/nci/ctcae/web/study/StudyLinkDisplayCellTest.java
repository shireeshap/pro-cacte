package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.web.table.cell.AbstractCellTestCase;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.bean.Row;

import java.text.ParseException;

/**
 * @author Vinay Kumar
 */
public class StudyLinkDisplayCellTest extends AbstractCellTestCase {

    private StudyLinkDisplayCell cell;
    private Column editLinkColumn;
    private Study study;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        study = new Study();
        study.setAssignedIdentifier("assigned identifier");
        study.setId(2);
        cell = new StudyLinkDisplayCell();

        editLinkColumn = new Column(model);
        editLinkColumn.setProperty("assignedIdentifier");
        editLinkColumn.setCell("gov.nih.nci.ctcae.web.study.StudyLinkDisplayCell");


    }

    public void testEditLinkForStudy() throws ParseException {


        model.addColumn(editLinkColumn);
        row = new Row(model);
        model.addRow(row);
        model.setCurrentRowBean(study);


        String cellValue = cell.getCellValue(model, editLinkColumn);
        assertEquals(String.format("<a href=\"editStudy?studyId=2\">%s</a>", "Edit"), cellValue);


    }


}