package gov.nih.nci.ctcae.web.table.cell;

import gov.nih.nci.ctcae.web.WebTestCase;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.bean.Table;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.context.HttpServletRequestContext;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.core.TableModelImpl;

import java.util.Collection;

/**
 * @author
 */
public abstract class AbstractCellTestCase extends WebTestCase {
    protected TableModel model;
    protected Context context;
    protected Row row;
    protected Column column;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = new HttpServletRequestContext(request);

        model = new TableModelImpl(context);

        Table table = model.getTableInstance();
        table.setTableId("ajaxTable");
        table.setForm("searchForm");
        //table.setItems(rides);
        table.setAction(model.getContext().getContextPath() + "/pages/searchRide");
        table.setTitle("");
        table.setOnInvokeAction("buildTable('searchForm')");
        table.setImagePath(model.getContext().getContextPath() + "/images/table/*.gif");
        table.setFilterable(false);
        table.setSortable(true);

        table.setAutoIncludeParameters(Boolean.FALSE);

        model.addTable(table);


        row = model.getRowInstance();
        row.setHighlightRow(Boolean.TRUE);
    }

    protected void assembleTable(Collection items) throws Exception {
        Table table = model.getTableInstance();
        table.setItems(items);
        model.addTable(table);
        row = new Row(model);
        model.addRow(row);
        model.addColumn(column);
        model.assemble();

    }
}
