package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.StudyCrf;
import gov.nih.nci.ctcae.web.AbstractTableModel;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

/**
 * @author Mehul Gulati
 *         Date: Nov 5, 2008
 */
public class StudyCrfTableModel extends AbstractTableModel {

	public String buildStudyCrfTable(Map parameterMap, Collection<StudyCrf> objects, HttpServletRequest request) {

		try {
			TableModel model = getModel(parameterMap, request, objects);

			addTitle(model);
			addStatus(model);
			addOptions(model);
			return model.assemble().toString();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return "";

	}

	protected Boolean getSortable() {
		return false;
	}

	protected Boolean getFilterable() {
		return false;
	}

	protected Boolean getToolTips() {
		return false;
	}

	protected Boolean getTitle() {
		return false;
	}

	protected Boolean getStatusBar() {
		return false;
	}

	protected boolean getPagination() {
		return false;
	}

	private void addTitle(TableModel model) {
		Column columnTitle = model.getColumnInstance();
		columnTitle.setTitle("Title");
		columnTitle.setProperty("crf.title");
		columnTitle.setAlias("title");
		columnTitle.setSortable(Boolean.TRUE);
		columnTitle.setFilterable(false);
		columnTitle.setCell("gov.nih.nci.ctcae.web.form.EditStudyCrfLinkCell");
		model.addColumn(columnTitle);
	}

	private void addStatus(TableModel model) {
		Column columnStatus = model.getColumnInstance();
		columnStatus.setTitle("Status");
		columnStatus.setProperty("crf.status");
		columnStatus.setAlias("status");
		columnStatus.setSortable(Boolean.TRUE);
		columnStatus.setFilterable(false);
		model.addColumn(columnStatus);
	}

	private void addOptions(TableModel model) {
		Column columnOptions = model.getColumnInstance();
		columnOptions.setTitle("Action");
		columnOptions.setSortable(Boolean.TRUE);
		columnOptions.setFilterable(false);
		columnOptions.setSortable(false);
		columnOptions.setAlias("options");
		columnOptions.setCell("gov.nih.nci.ctcae.web.form.StudyCrfLinkDisplayDetailsCell");
		model.addColumn(columnOptions);
	}


}
