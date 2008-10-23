package gov.nih.nci.ctcae.web.study;



import gov.nih.nci.ctcae.core.domain.Study;

import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.view.html.ColumnBuilder;
import org.apache.commons.beanutils.BeanUtils;


/**
 * @author Harsh Agarwal
 * @created Oct 23, 2008
 */
public class SelectedStudyCell implements Cell {

    public String getExportDisplay(TableModel model, Column column) {
        return column.getValueAsString();
    }

    public String getHtmlDisplay(TableModel model, Column column) {
        ColumnBuilder inputBuilder = new ColumnBuilder(column);
        inputBuilder.tdStart();

        try {
            Study bean = (Study) model.getCurrentRowBean();
            Integer id = bean.getId();
            inputBuilder.getHtmlBuilder().input("radio")
                    .name("study" + id.intValue())
                    .id("study" + id.intValue())
                    .value(id.toString())
                    .onclick("selectStudy(this.value)")
                    ;
            inputBuilder.getHtmlBuilder().xclose();
            inputBuilder.tdBody(bean.getAssignedIdentifier());

        } catch (Exception e) {
        }
        inputBuilder.tdEnd();

        return inputBuilder.toString().trim();
    }

}