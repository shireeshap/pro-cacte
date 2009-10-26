package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.web.AbstractTableModel;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

//
/**
 * The Class ClinicalStaffTableModel.
 *
 * @author Mehul Gulati
 *         Date: Oct 22, 2008
 */
public class ClinicalStaffTableModel extends AbstractTableModel {

    /**
     * Builds the clinical staff table.
     *
     * @param parameterMap the parameter map
     * @param objects      the objects
     * @param request      the request
     * @return the string
     */
    public String buildClinicalStaffTable(Map parameterMap, Collection<ClinicalStaff> objects, HttpServletRequest request) {

        try {
            TableModel model = getModel(parameterMap, request, objects);

            addFirstName(model);
            addMiddleName(model);
            addLastName(model);
            addNciIdentifier(model);
            return model.assemble().toString();

        } catch (Exception e) {
            logger.error("error while generating table for clinical staff. " + e.getMessage(), e);
            throw new CtcAeSystemException(e);
        }
    }

    /**
     * Adds the first name.
     *
     * @param model the model
     */
    private void addFirstName(TableModel model) {
        Column columnFirstName = model.getColumnInstance();
        columnFirstName.setTitle("First name");
        columnFirstName.setProperty("firstName");
        columnFirstName.setSortable(Boolean.TRUE);
        columnFirstName.setCell("gov.nih.nci.ctcae.web.clinicalStaff.ClinicalStaffLinkDisplayDetailsCell");

        model.addColumn(columnFirstName);
    }

    /**
     * Adds the last name.
     *
     * @param model the model
     */
    private void addLastName(TableModel model) {
        Column columnLastName = model.getColumnInstance();
        columnLastName.setTitle("Last name");
        columnLastName.setProperty("lastName");
        columnLastName.setSortable(Boolean.TRUE);

        model.addColumn(columnLastName);
    }

    /**
     * Adds the middle name.
     *
     * @param model the model
     */
    private void addMiddleName(TableModel model) {
        Column columnMiddleName = model.getColumnInstance();
        columnMiddleName.setTitle("Middle name");
        columnMiddleName.setProperty("middleName");
        columnMiddleName.setSortable(Boolean.TRUE);

        model.addColumn(columnMiddleName);
    }

    /**
     * Adds the nci identifier.
     *
     * @param model the model
     */
    private void addNciIdentifier(TableModel model) {
        Column columnNciIdentifier = model.getColumnInstance();
        columnNciIdentifier.setTitle("Identifier");
        columnNciIdentifier.setProperty("nciIdentifier");
        columnNciIdentifier.setSortable(Boolean.TRUE);

        model.addColumn(columnNciIdentifier);
    }

}
