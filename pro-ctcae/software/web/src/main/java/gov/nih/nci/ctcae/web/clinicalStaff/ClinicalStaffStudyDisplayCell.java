package gov.nih.nci.ctcae.web.clinicalStaff;

import org.extremecomponents.table.cell.AbstractCell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.bean.Column;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Study;

import java.util.Set;
import java.util.HashSet;

/**
 * @author Mehul Gulati
 * Date: Dec 23, 2009
 */
public class ClinicalStaffStudyDisplayCell extends AbstractCell {

    public String getExportDisplay(TableModel model, Column column) {
        return column.getValueAsString();
    }

    protected String getCellValue(TableModel tableModel, Column column) {

        ClinicalStaff bean = (ClinicalStaff) tableModel.getCurrentRowBean();
        String cellValue = "";
        Set studies = new HashSet();
            for (OrganizationClinicalStaff site : bean.getOrganizationClinicalStaffs()){
                for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : site.getStudyOrganizationClinicalStaff()) {
                    studies.add(studyOrganizationClinicalStaff.getStudyOrganization().getStudy());
                }
            }

        for(Object obj : studies){
            cellValue += ((Study)obj).getDisplayName() + "<br>";
        }
        return cellValue;
    }
}