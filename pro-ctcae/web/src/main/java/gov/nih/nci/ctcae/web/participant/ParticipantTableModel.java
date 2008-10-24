package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.web.AbstractTableModel;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;

/**
 * @author Harsh Agarwal
 * Date: Oct 23, 2008
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
            
        }      catch (Exception e) {

        }
        return"";
    }

    private void addFirstName(TableModel model) {
        Column columnFirstName = model.getColumnInstance();
        columnFirstName.setTitle("First Name");
        columnFirstName.setProperty("firstName");
        columnFirstName.setSortable(Boolean.TRUE);

        model.addColumn(columnFirstName);
    }

     private void addLastName(TableModel model) {
        Column columnLastName = model.getColumnInstance();
        columnLastName.setTitle("Last Name");
        columnLastName.setProperty("lastName");
        columnLastName.setSortable(Boolean.TRUE);

        model.addColumn(columnLastName);
    }

    private void addMiddleName (TableModel model) {
        Column columnMiddleName = model.getColumnInstance();
        columnMiddleName.setTitle("Middle Name");
        columnMiddleName.setProperty("middleName");
        columnMiddleName.setSortable(Boolean.TRUE);

        model.addColumn(columnMiddleName);
    }

     private void addIdentifier(TableModel model) {
        Column columnIdentifier = model.getColumnInstance();
        columnIdentifier.setTitle("Identifier");
        columnIdentifier.setProperty("identifier");
        columnIdentifier.setSortable(Boolean.TRUE);
        columnIdentifier.setCell("gov.nih.nci.ctcae.web.participant.ParticipantLinkDisplayDetailsCell");
        model.addColumn(columnIdentifier);
    }

}
