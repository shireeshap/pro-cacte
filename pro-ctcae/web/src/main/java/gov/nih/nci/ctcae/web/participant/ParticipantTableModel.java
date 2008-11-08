package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.web.AbstractTableModel;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

/**
 * @author Harsh Agarwal
 *         Date: Oct 23, 2008
 */
public class ParticipantTableModel extends AbstractTableModel {

    public String buildParticipantTable(Map parameterMap, Collection<Participant> objects, HttpServletRequest request) {

        try {
            TableModel model = getModel(parameterMap, request, objects);

            addIdentifier(model);
            addFirstName(model);
            addMiddleName(model);
            addLastName(model);

            return model.assemble().toString();

        } catch (Exception e) {

        }
        return "";
    }

    private void addFirstName(TableModel model) {
        Column columnFirstName = model.getColumnInstance();
        columnFirstName.setTitle("First name");
        columnFirstName.setProperty("firstName");
        columnFirstName.setSortable(Boolean.TRUE);

        model.addColumn(columnFirstName);
    }

    private void addLastName(TableModel model) {
        Column columnLastName = model.getColumnInstance();
        columnLastName.setTitle("Last name");
        columnLastName.setProperty("lastName");
        columnLastName.setSortable(Boolean.TRUE);

        model.addColumn(columnLastName);
    }

    private void addMiddleName(TableModel model) {
        Column columnMiddleName = model.getColumnInstance();
        columnMiddleName.setTitle("Middle name");
        columnMiddleName.setProperty("middleName");
        columnMiddleName.setSortable(Boolean.TRUE);

        model.addColumn(columnMiddleName);
    }

    private void addIdentifier(TableModel model) {
        Column columnIdentifier = model.getColumnInstance();
        columnIdentifier.setTitle("Patient identifier");
        columnIdentifier.setProperty("identifier");
        columnIdentifier.setSortable(Boolean.TRUE);
        columnIdentifier.setCell("gov.nih.nci.ctcae.web.participant.ParticipantLinkDisplayDetailsCell");
        model.addColumn(columnIdentifier);
    }

}
