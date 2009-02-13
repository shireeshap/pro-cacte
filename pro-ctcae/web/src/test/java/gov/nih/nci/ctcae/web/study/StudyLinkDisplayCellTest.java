package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.web.table.cell.AbstractCellTestCase;
import org.apache.commons.lang.StringUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.bean.Row;

import java.text.ParseException;

/**
 * @author Vinay Kumar
 */
public class StudyLinkDisplayCellTest extends AbstractCellTestCase {

    private StudyLinkDisplayDetailsCell cell;
    private Column editLinkColumn;
    private Study study;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        study = new Study();
        study.setAssignedIdentifier("assigned identifier");
        study.setId(2);
        cell = new StudyLinkDisplayDetailsCell();

        editLinkColumn = new Column(model);
        editLinkColumn.setProperty("assignedIdentifier");
        editLinkColumn.setCell("gov.nih.nci.ctcae.web.study.StudyLinkDisplayDetailsCell");


    }

    public void testEditLinkForStudy() throws ParseException {


        model.addColumn(editLinkColumn);
        row = new Row(model);
        model.addRow(row);
        model.setCurrentRowBean(study);


        String cellValue = cell.getCellValue(model, editLinkColumn);
        assertEquals("<a href=\"createStudy?studyId=2\">assigned identifier</a>", cellValue);


    }



}