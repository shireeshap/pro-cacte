package gov.nih.nci.ctcae.web.table.cell;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.AbstractCell;
import org.extremecomponents.table.core.TableModel;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Date;

/**
 * @author
 */
public class DateFormatterCell extends AbstractCell {
	protected final Log log = LogFactory.getLog(getClass());


	@Override
	protected String getCellValue(TableModel model, Column column) {

		BeanWrapper beanWrapper = new BeanWrapperImpl(model.getCurrentRowBean());

		String cellValue = "";

		if (beanWrapper.isReadableProperty(column.getProperty())) {
			cellValue = DateUtils.format((Date) beanWrapper.getPropertyValue(column.getProperty()));
		}
		return cellValue;
	}
}
