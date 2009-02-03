package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.web.AbstractTableModel;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class StudyTableModel.
 *
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class StudyTableModel extends AbstractTableModel {

    /**
     * Builds the study table.
     *
     * @param parameterMap the parameter map
     * @param objects      the objects
     * @param request      the request
     * @return the string
     */
    public String buildStudyTable(Map parameterMap, Collection<Study> objects,
                                  HttpServletRequest request) {

        try {
            TableModel model = getModel(parameterMap, request, objects);

            addAssignedIdentifier(model);
            addShorTitleColumn(model);

            addSponsorColumn(model);

            addStudyCoordinatingCenter(model);
            return model.assemble().toString();
        } catch (Exception e) {
            logger.error("error while generating table for study. " + e.getMessage(), e);
            throw new CtcAeSystemException(e);

        }
    }

    /**
     * Builds the study table for selection.
     *
     * @param parameterMap the parameter map
     * @param objects      the objects
     * @param request      the request
     * @return the string
     */
    public String buildStudyTableForSelection(Map parameterMap,
                                              Collection<Study> objects, HttpServletRequest request) {

        try {

            TableModel model = getModel(parameterMap, request, objects);

            addAssignedIdentifierForSelection(model);
            addShorTitleColumn(model);

            addSponsorColumn(model);

            addStudyCoordinatingCenter(model);
            addParticipantStudyIdentifierText(model);

            return model.assemble().toString();
        } catch (Exception e) {

        }
        return "";
    }

    /**
     * Adds the study coordinating center.
     *
     * @param model the model
     */
    private void addStudyCoordinatingCenter(TableModel model) {
        Column columnSponsorCode = model.getColumnInstance();
        columnSponsorCode.setTitle("Coordinating center");
        columnSponsorCode
                .setProperty("studyCoordinatingCenter.organization.nciInstituteCode");
        columnSponsorCode.setAlias("studyCoordinatingCenter");

        columnSponsorCode.setSortable(Boolean.TRUE);
        model.addColumn(columnSponsorCode);
    }

    /**
     * Adds the sponsor column.
     *
     * @param model the model
     */
    private void addSponsorColumn(TableModel model) {
        Column columnSponsorCode = model.getColumnInstance();
        columnSponsorCode.setTitle("Funding sponsor");
        columnSponsorCode
                .setProperty("studyFundingSponsor.organization.nciInstituteCode");
        columnSponsorCode.setAlias("studyFundingSponsor");
        columnSponsorCode.setSortable(Boolean.TRUE);
        model.addColumn(columnSponsorCode);
    }

    /**
     * Adds the shor title column.
     *
     * @param model the model
     */
    private void addShorTitleColumn(TableModel model) {
        Column columnShortTitle = model.getColumnInstance();
        columnShortTitle.setTitle("Short title");
        columnShortTitle.setProperty("shortTitle");
        columnShortTitle.setSortable(Boolean.TRUE);
        // columnShortTitle.setCell("gov.nih.nci.cabig.ctcae.web.study.StudyLinkDisplayCell");
        model.addColumn(columnShortTitle);
    }

    /**
     * Adds the assigned identifier.
     *
     * @param model the model
     */
    private void addAssignedIdentifier(TableModel model) {
        Column columnShortTitle = model.getColumnInstance();
        columnShortTitle.setTitle("Study identifier");
        columnShortTitle.setProperty("assignedIdentifier");
        columnShortTitle.setSortable(Boolean.TRUE);
        // columnShortTitle.setCell("gov.nih.nci.ctcae.web.study.SelectedStudyCell");
        model.addColumn(columnShortTitle);
    }

    /**
     * Adds the assigned identifier for selection.
     *
     * @param model the model
     */
    private void addAssignedIdentifierForSelection(TableModel model) {
        Column columnShortTitle = model.getColumnInstance();
        columnShortTitle.setTitle("Study identifier");
        columnShortTitle.setProperty("assignedIdentifier");
        columnShortTitle.setSortable(Boolean.TRUE);
        columnShortTitle
                .setCell("gov.nih.nci.ctcae.web.study.SelectedStudyCell");
        model.addColumn(columnShortTitle);
    }

    /**
     * Adds the participant study identifier text.
     *
     * @param model the model
     */
    private void addParticipantStudyIdentifierText(TableModel model) {
        Column columnParticipantStudyIdentifier = model.getColumnInstance();
        columnParticipantStudyIdentifier.setTitle("Patient study identifier");
        columnParticipantStudyIdentifier.setSortable(Boolean.FALSE);
        columnParticipantStudyIdentifier
                .setCell("gov.nih.nci.ctcae.web.study.TextBoxCell");
        model.addColumn(columnParticipantStudyIdentifier);
    }
}
