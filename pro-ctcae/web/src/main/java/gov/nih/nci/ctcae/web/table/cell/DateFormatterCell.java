package gov.nih.nci.ctcae.web.table.cell;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.AbstractCell;
import org.extremecomponents.table.core.TableModel;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.text.ParseException;
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

        if (beanWrapper.getPropertyValue(column.getProperty()) != null) {
            try {
                cellValue = DateUtils.format((Date) beanWrapper.getPropertyValue(column.getProperty()));
            } catch (ParseException e) {
                log.error(String.format("can not parse  date %s for object %s. Check if this object has %s proeprty",
                        cellValue, model.getCurrentRowBean(), column.getProperty()));
            }
        }
        return cellValue;
    }
}
