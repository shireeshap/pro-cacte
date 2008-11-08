package gov.nih.nci.ctcae.web.table.cell;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.TestBean;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.bean.Row;
import org.springframework.beans.NotReadablePropertyException;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author
 */
public class DateFormatterCellTest extends AbstractCellTestCase {

    private DateFormatterCell DateFormatterCell;
    private Column testBeanDateColumn;
    private TestBean testBean;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testBean = new TestBean();
        Date date = DateUtils.createDate(2008, Calendar.JUNE, 28);
        testBean.setDate(date);

        DateFormatterCell = new DateFormatterCell();
        testBeanDateColumn = new Column(model);
        testBeanDateColumn.setProperty("date");
        testBeanDateColumn.setCell("gov.nih.nci.ctcae.web.table.cell.DateFormatterCell");
        testBeanDateColumn.setTitle("Date");


    }

    public void testDisplayCellForTestBean() throws ParseException {


        model.addColumn(testBeanDateColumn);
        row = new Row(model);
        model.addRow(row);
        model.setCurrentRowBean(testBean);


        String formattedDate = DateFormatterCell.getCellValue(model, testBeanDateColumn);
        assertEquals("06/28/2008", formattedDate);


    }


    public void testDisplayCellIfBeanDoesNotHaveDateProperty() throws ParseException {
        model.addColumn(testBeanDateColumn);
        row = new Row(model);
        model.addRow(row);
        model.setCurrentRowBean("string does not have date bean property");


        try {
            DateFormatterCell.getCellValue(model, testBeanDateColumn);
            fail();
        } catch (NotReadablePropertyException e) {

        }


    }
}
