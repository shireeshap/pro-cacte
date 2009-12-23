package gov.nih.nci.ctcae.web.participant;

import org.extremecomponents.table.cell.AbstractCell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.bean.Column;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;

/**
 * @author Mehul Gulati
 * Date: Dec 23, 2009
 */
public class ParticipantStudyDisplayCell extends AbstractCell {

    public String getExportDisplay(TableModel model, Column column) {
        return column.getValueAsString();
    }

    protected String getCellValue(TableModel tableModel, Column column) {

        Participant bean = (Participant) tableModel.getCurrentRowBean();
        String cellValue = "";

            for (StudyParticipantAssignment spa : bean.getStudyParticipantAssignments()){
                 cellValue += spa.getStudySite().getStudy().getDisplayName() + "<br>";
            }


        return cellValue;
    }
}