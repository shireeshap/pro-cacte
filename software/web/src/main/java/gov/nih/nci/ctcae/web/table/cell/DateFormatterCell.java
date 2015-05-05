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

//
/**
 * The Class DateFormatterCell.
 *
 * @author
 */
public class DateFormatterCell extends AbstractCell {

    /**
     * The log.
     */
    protected final Log logger = LogFactory.getLog(getClass());


    /* (non-Javadoc)
      * @see org.extremecomponents.table.cell.AbstractCell#getCellValue(org.extremecomponents.table.core.TableModel, org.extremecomponents.table.bean.Column)
      */
    @Override
    protected String getCellValue(TableModel model, Column column) {

        BeanWrapper beanWrapper = new BeanWrapperImpl(model.getCurrentRowBean());

        String cellValue = "";

        if (beanWrapper.isReadableProperty(column.getProperty())) {
            Object value = beanWrapper.getPropertyValue(column.getProperty());
            if (value != null) {
                cellValue = DateUtils.format((Date) value);
            }
        }
        return cellValue;
    }
}
