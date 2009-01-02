package gov.nih.nci.ctcae.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.bean.Table;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.context.HttpServletRequestContext;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.core.TableModelImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class AbstractTableModel {
	protected Log log = LogFactory.getLog(getClass());

	protected TableModel getModel(Map parameterMap, HttpServletRequest request, Collection objects) {
		Context context = null;
		if (parameterMap == null) {
			context = new HttpServletRequestContext(request);
		} else {
			context = new HttpServletRequestContext(request, parameterMap);
		}

		TableModel model = new TableModelImpl(context);
		addTable(model, objects);

		return model;
	}

	private void addTable(TableModel model, Collection objects) {
		Table table = model.getTableInstance();
		table.setTableId("ajaxTable");
		table.setForm("assembler");
		table.setItems(objects);
		table.setAction(model.getContext().getContextPath() + "/assembler.run");
		table.setTitle("");
		table.setShowPagination(getPagination());
		table.setShowStatusBar(getStatusBar());
		table.setShowTitle(getTitle());
		table.setShowTooltips(getToolTips());
		table.setOnInvokeAction("buildTable('assembler')");
		table.setImagePath(model.getContext().getContextPath() + "/images/table/*.gif");
		table.setFilterable(getFilterable());
		table.setSortable(getSortable());
        table.setSortRowsCallback("gov.nih.nci.ctcae.web.table.SortRowsCallbackImpl");

		table.setAutoIncludeParameters(false);
		model.addTable(table);

		Row row = model.getRowInstance();
		row.setHighlightRow(Boolean.TRUE);
		updateRow(row);
        model.addRow(row);
	}

	protected void updateRow(Row row) {

	}

	protected Boolean getSortable() {
		return true;
	}

	protected Boolean getFilterable() {
		return true;
	}

	protected Boolean getToolTips() {
		return true;
	}

	protected Boolean getTitle() {
		return true;
	}

	protected Boolean getStatusBar() {
		return true;
	}

	protected boolean getPagination() {
		return true;
	}
}
