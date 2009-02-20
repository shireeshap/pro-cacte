package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.web.AbstractTableModel;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

//
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
            addActions(model);
            return model.assemble().toString();
        } catch (Exception e) {
            logger.error("error while generating table for study. " + e.getMessage(), e);
            throw new CtcAeSystemException(e);

        }
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
        model.addColumn(columnShortTitle);
    }

    private void addActions(TableModel model) {
        Column columnAction = model.getColumnInstance();
        columnAction.setTitle("Actions");
        columnAction.setSortable(Boolean.FALSE);
        columnAction.setCell("gov.nih.nci.ctcae.web.study.StudyLinkDisplayCell");


        model.addColumn(columnAction);
    }


}
