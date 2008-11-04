package gov.nih.nci.ctcae.web.table;

import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.core.domain.Study;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.core.TableModelImpl;
import org.extremecomponents.table.context.HttpServletRequestContext;
import org.extremecomponents.table.bean.Table;
import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.limit.TableLimit;
import org.extremecomponents.table.limit.ModelLimitFactory;

import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Vinay Kumar
 * @crated Nov 4, 2008
 */
public class SortRowsCallbackImplTest extends AbstractTableModelTestCase {

    private SortRowsCallbackImpl callback;

    protected TableModel model;
    private HttpServletRequestContext context;
    private List<Study> rows;
    private Object objects;
    private TableLimit limit;
    private String string1;
    private String string2;
    private String string3;

    private Study study1, study2, study3;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        callback = new SortRowsCallbackImpl();
        parameterMap.put("ec_s_shortTitle", "asc");
        context = new HttpServletRequestContext(request, parameterMap);

        model = new TableModelImpl(context);
        Table table = model.getTableInstance();
        model.addTable(table);

        study1 = new Study();
        study2 = new Study();
        study3 = new Study();
        string1 = "String 1";

        study1.setShortTitle(string1);

        string2 = "another String";
        study2.setShortTitle(string2);

        string3 = "third String";
        study3.setShortTitle(string3);

        rows = new ArrayList();
        rows.add(study1);

        rows.add(study2);

        rows.add(study3);

    }

    public void testSortInASCOrder() throws Exception {
        limit = new TableLimit(new ModelLimitFactory(model));
        model.setLimit(limit);
        callback.sortRows(model, rows);
        callback.sortRows(model, rows);
        assertEquals("must  sort rows in asc order", string2, rows.get(0).getShortTitle());
        assertEquals("must  sort rows in asc order", string1, rows.get(1).getShortTitle());
        assertEquals("must  sort rows in asc order", string3, rows.get(2).getShortTitle());


    }

    public void testSortInDescOrder() throws Exception {
        parameterMap.put("ec_s_shortTitle", "desc");

        context = new HttpServletRequestContext(request, parameterMap);
        model = new TableModelImpl(context);
        Table table = model.getTableInstance();
        model.addTable(table);
        limit = new TableLimit(new ModelLimitFactory(model));
        model.setLimit(limit);

        callback.sortRows(model, rows);
        assertEquals("must  sort rows in asc order", string3, rows.get(0).getShortTitle());
        assertEquals("must  sort rows in asc order", string1, rows.get(1).getShortTitle());
        assertEquals("must  sort rows in asc order", string2, rows.get(2).getShortTitle());


    }

    public void testSortIfTableIsNotSortable() throws Exception {
        context = new HttpServletRequestContext(request);

        model = new TableModelImpl(context);
        Table table = model.getTableInstance();
        table.setItems(objects);
        table.setSortable(false);

        model.addTable(table);

        callback.sortRows(model, rows);

        assertEquals("must not sort rows", string1, rows.get(0).getShortTitle());
        assertEquals("must not sort rows", string2, rows.get(1).getShortTitle());
        assertEquals("must not sort rows", string3, rows.get(2).getShortTitle());

    }

}
