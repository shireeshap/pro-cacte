package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.web.AbstractTableModel;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Collection;

import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.bean.Column;

/**
 * @author Mehul Gulati
 * Date: Oct 22, 2008
 */
public class ClinicalStaffTableModel extends AbstractTableModel {

    public String buildClinicalStaffTable(Map parameterMap, Collection<ClinicalStaff> objects, HttpServletRequest request) {

        try {
            TableModel model = getModel(parameterMap, request, objects);

            addFirstName(model);
            addMiddleName(model);
            addLastName(model);
            addNciIdentifier(model);
            return model.assemble().toString();

        }      catch (Exception e) {

        }
        return"";
    }

    private void addFirstName(TableModel model) {
        Column columnFirstName = model.getColumnInstance();
        columnFirstName.setTitle("First name");
        columnFirstName.setProperty("firstName");
        columnFirstName.setSortable(Boolean.TRUE);
        columnFirstName.setCell("gov.nih.nci.ctcae.web.clinicalStaff.ClinicalStaffLinkDisplayDetailsCell");

        model.addColumn(columnFirstName);
    }

     private void addLastName(TableModel model) {
        Column columnLastName = model.getColumnInstance();
        columnLastName.setTitle("Last name");
        columnLastName.setProperty("lastName");
        columnLastName.setSortable(Boolean.TRUE);

        model.addColumn(columnLastName);
    }

    private void addMiddleName (TableModel model) {
        Column columnMiddleName = model.getColumnInstance();
        columnMiddleName.setTitle("Middle name");
        columnMiddleName.setProperty("middleName");
        columnMiddleName.setSortable(Boolean.TRUE);

        model.addColumn(columnMiddleName);
    }

     private void addNciIdentifier(TableModel model) {
        Column columnNciIdentifier = model.getColumnInstance();
        columnNciIdentifier.setTitle("NCI institue code");
        columnNciIdentifier.setProperty("nciIdentifier");
        columnNciIdentifier.setSortable(Boolean.TRUE);

        model.addColumn(columnNciIdentifier);
    }

}
