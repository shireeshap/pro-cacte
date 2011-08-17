package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.*;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.AbstractCell;
import org.extremecomponents.table.core.TableModel;
import org.springframework.security.context.SecurityContextHolder;

//
/**
 * The Class ClinicalStaffLinkDisplayDetailsCell.
 *
 * @author Mehul Gulati
 *         Date: Oct 28, 2008
 */
public class ClinicalStaffLinkDisplayDetailsCell extends AbstractCell {

    /* (non-Javadoc)
     * @see org.extremecomponents.table.cell.Cell#getExportDisplay(org.extremecomponents.table.core.TableModel, org.extremecomponents.table.bean.Column)
     */
    public String getExportDisplay(TableModel model, Column column) {
        return column.getValueAsString();
    }

    /* (non-Javadoc)
     * @see org.extremecomponents.table.cell.Cell#getHtmlDisplay(org.extremecomponents.table.core.TableModel, org.extremecomponents.table.bean.Column)
     */
//    public String getHtmlDisplay(TableModel model, Column column) {
//        ColumnBuilder inputBuilder = new ColumnBuilder(column);
//        inputBuilder.tdStart();
//
//        try {
//            ClinicalStaff bean = (ClinicalStaff) model.getCurrentRowBean();
//            Integer id = bean.getId();
//            inputBuilder.getHtmlBuilder().a("createClinicalStaff?clinicalStaffId=" + id);
//
//            inputBuilder.getHtmlBuilder().xclose();
//            inputBuilder.tdBody(bean.getFirstName());
//        } catch (Exception e) {
//
//        }
//        inputBuilder.tdEnd();
//
//        return inputBuilder.toString().trim();
//    }


    protected String getCellValue(TableModel tableModel, Column column) {
        ClinicalStaff bean = (ClinicalStaff) tableModel.getCurrentRowBean();
        Integer id = bean.getId();
        boolean odc = true;
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        for (OrganizationClinicalStaff site : bean.getOrganizationClinicalStaffs()){
                for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : site.getStudyOrganizationClinicalStaff()) {
                    Study study = studyOrganizationClinicalStaff.getStudyOrganization().getStudy();
                    if (!user.isODCOnStudy(study)) {
                        odc = false;
                        break;
                    }
                }
            }

        String cellValue = "<a class=\"fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all\" id=\"clinicalStaffActions" + id.toString() + "\"><span class=\"ui-icon ui-icon-triangle-1-s\"></span>Actions</a><script>showPopUpMenuClinicalStaff('" + id.toString() + "','" + bean.getStatus() + "','" + odc + "');</script>";
        return cellValue;
    }

}
