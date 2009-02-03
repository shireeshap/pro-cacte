package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.web.AbstractTableModel;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.core.TableModel;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class CrfTableModel.
 *
 * @author Mehul Gulati
 *         Date: Nov 5, 2008
 */
public class CrfTableModel extends AbstractTableModel {

    /**
     * Builds the crf table.
     *
     * @param parameterMap the parameter map
     * @param objects      the objects
     * @param request      the request
     * @return the string
     */
    public String buildCrfTable(Map parameterMap, Collection<CRF> objects, HttpServletRequest request) {

        try {
            TableModel model = getModel(parameterMap, request, objects);

            addShowVersion(model);
            addTitle(model);
            addVersion(model);
            addEffectiveDate(model);
            addExpirationDate(model);
            addStatus(model);
            addOptions(model);
            return model.assemble().toString();
        } catch (Exception e) {
            logger.error("error while generating table for crf. " + e.getMessage(), e);
            throw new CtcAeSystemException(e);

        }

    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.web.AbstractTableModel#getSortable()
     */
    protected Boolean getSortable() {
        return false;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.web.AbstractTableModel#getFilterable()
     */
    protected Boolean getFilterable() {
        return false;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.web.AbstractTableModel#getToolTips()
     */
    protected Boolean getToolTips() {
        return false;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.web.AbstractTableModel#getTitle()
     */
    protected Boolean getTitle() {
        return false;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.web.AbstractTableModel#getStatusBar()
     */
    protected Boolean getStatusBar() {
        return false;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.web.AbstractTableModel#getPagination()
     */
    protected boolean getPagination() {
        return false;
    }

    /**
     * Adds the show version.
     *
     * @param model the model
     */
    private void addShowVersion(TableModel model) {
        Column columnShowVersion = model.getColumnInstance();
        columnShowVersion.setTitle("");
        columnShowVersion.setProperty("");
        columnShowVersion.setAlias("");
        columnShowVersion.setSortable(Boolean.TRUE);
        columnShowVersion.setFilterable(false);
        columnShowVersion.setCell("gov.nih.nci.ctcae.web.form.CrfDisplayVersionsCell");
        model.addColumn(columnShowVersion);
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.web.AbstractTableModel#updateRow(org.extremecomponents.table.bean.Row)
     */
    protected void updateRow(Row row) {

        row.setHighlightRow(false);
        row.setInterceptor("gov.nih.nci.ctcae.web.form.ManageFormTableRowInterceptor");
    }

    /**
     * Adds the title.
     *
     * @param model the model
     */
    private void addTitle(TableModel model) {
        Column columnTitle = model.getColumnInstance();
        columnTitle.setTitle("Title");
        columnTitle.setProperty("title");
        columnTitle.setAlias("title");
        columnTitle.setSortable(Boolean.TRUE);
        columnTitle.setFilterable(false);
        model.addColumn(columnTitle);
    }

    /**
     * Adds the version.
     *
     * @param model the model
     */
    private void addVersion(TableModel model) {
        Column columnTitle = model.getColumnInstance();
        columnTitle.setTitle("Version");
        columnTitle.setProperty("crfVersion");
        columnTitle.setAlias("crfVersion");
        columnTitle.setSortable(Boolean.TRUE);
        columnTitle.setFilterable(false);
        model.addColumn(columnTitle);

    }

    /**
     * Adds the effective date.
     *
     * @param model the model
     */
    private void addEffectiveDate(TableModel model) {
        Column columnEffectiveDate = model.getColumnInstance();
        columnEffectiveDate.setTitle("Effective Date");
        columnEffectiveDate.setProperty("effectiveStartDate");
        columnEffectiveDate.setAlias("effectiveStartDate");
        columnEffectiveDate.setSortable(Boolean.TRUE);
        columnEffectiveDate.setFilterable(false);
        columnEffectiveDate.setCell("gov.nih.nci.ctcae.web.table.cell.DateFormatterCell");
        model.addColumn(columnEffectiveDate);

    }

    /**
     * Adds the expiration date.
     *
     * @param model the model
     */
    private void addExpirationDate(TableModel model) {
        Column columnExpirationDate = model.getColumnInstance();
        columnExpirationDate.setTitle("Expiration Date");
        columnExpirationDate.setProperty("effectiveEndDate");
        columnExpirationDate.setAlias("effectiveEndDate");
        columnExpirationDate.setSortable(Boolean.TRUE);
        columnExpirationDate.setFilterable(false);
        columnExpirationDate.setCell("gov.nih.nci.ctcae.web.table.cell.DateFormatterCell");
        model.addColumn(columnExpirationDate);

    }

    /**
     * Adds the status.
     *
     * @param model the model
     */
    private void addStatus(TableModel model) {
        Column columnStatus = model.getColumnInstance();
        columnStatus.setTitle("Status");
        columnStatus.setProperty("status");
        columnStatus.setAlias("status");
        columnStatus.setSortable(Boolean.TRUE);
        columnStatus.setFilterable(false);
        model.addColumn(columnStatus);
    }

    /**
     * Adds the options.
     *
     * @param model the model
     */
    private void addOptions(TableModel model) {
        Column columnOptions = model.getColumnInstance();
        columnOptions.setTitle("Action");
        columnOptions.setSortable(Boolean.TRUE);
        columnOptions.setFilterable(false);
        columnOptions.setSortable(false);
        columnOptions.setAlias("options");
        columnOptions.setCell("gov.nih.nci.ctcae.web.form.CRFLinkDisplayDetailsCell");
        model.addColumn(columnOptions);
    }


}
