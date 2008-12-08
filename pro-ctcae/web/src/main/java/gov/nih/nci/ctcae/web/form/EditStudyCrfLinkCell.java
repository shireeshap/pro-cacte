package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.StudyCrf;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.AbstractCell;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;

/**
 * @author Vinay Kumar
 *         Date: Dec 8, 2008
 */
public class EditStudyCrfLinkCell extends AbstractCell implements Cell {

	public String getExportDisplay(TableModel model, Column column) {
		return column.getValueAsString();
	}


	protected String getCellValue(TableModel model, Column column) {
		StudyCrf bean = (StudyCrf) model.getCurrentRowBean();
		Integer id = bean.getId();

		String cellValue = "";
		String link1 = model.getContext().getContextPath() + "/pages/form/editForm?studyCrfId=" + id.toString();
		if (bean.getCrf().getStatus().equals(CrfStatus.DRAFT)) {
			cellValue = "<a href=\"" + link1 + "\">" + bean.getCrf().getTitle() + "</a>";
		} else {
			cellValue = bean.getCrf().getTitle();

		}
		return cellValue;
	}
}