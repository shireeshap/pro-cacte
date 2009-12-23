package gov.nih.nci.ctcae.web.clinicalStaff;

import org.extremecomponents.table.cell.AbstractCell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.bean.Column;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;

/**
 * @author Mehul Gulati
 * Date: Dec 22, 2009
 */
public class ClinicalStaffSiteDisplayCell extends AbstractCell {

    public String getExportDisplay(TableModel model, Column column) {
        return column.getValueAsString();
    }

    protected String getCellValue(TableModel tableModel, Column column) {

        ClinicalStaff bean = (ClinicalStaff) tableModel.getCurrentRowBean();
        String cellValue = "";

            for (OrganizationClinicalStaff site : bean.getOrganizationClinicalStaffs()){
                 cellValue += site.getOrganization().getDisplayName() + "<br>";
            }


        return cellValue;
    }
}
