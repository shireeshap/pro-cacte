package gov.nih.nci.ctcae.web.study;

import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.bean.Column;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

import gov.nih.nci.ctcae.web.AbstractTableModel;
import gov.nih.nci.ctcae.core.domain.Study;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class StudyTableModel extends AbstractTableModel {


    public String buildStudyTable(Map parameterMap, Collection<Study> objects, HttpServletRequest request) {

        try {
            TableModel model = getModel(parameterMap, request, objects);


            addAssignedIdentifier(model);
            addShorTitleColumn(model);

            addSponsorColumn(model);

            addStudyCoordinatingCentern(model);
            return model.assemble().toString();
        } catch (Exception e) {

        }
        return "";
    }


    private void addStudyCoordinatingCentern(TableModel model) {
        Column columnSponsorCode = model.getColumnInstance();
        columnSponsorCode.setTitle("Coordinating center");
        columnSponsorCode.setProperty("studyCoordinatingCenter.organization.nciInstituteCode");
        columnSponsorCode.setAlias("studyCoordinatingCenter");

        columnSponsorCode.setSortable(Boolean.TRUE);
        model.addColumn(columnSponsorCode);
    }

    private void addSponsorColumn(TableModel model) {
        Column columnSponsorCode = model.getColumnInstance();
        columnSponsorCode.setTitle("Funding Sponsor");
        columnSponsorCode.setProperty("studyFundingSponsor.organization.nciInstituteCode");
        columnSponsorCode.setAlias("studyFundingSponsor");
        columnSponsorCode.setSortable(Boolean.TRUE);
        model.addColumn(columnSponsorCode);
    }

    private void addShorTitleColumn(TableModel model) {
        Column columnShortTitle = model.getColumnInstance();
        columnShortTitle.setProperty("shortTitle");
        columnShortTitle.setSortable(Boolean.TRUE);
        //columnShortTitle.setCell("gov.nih.nci.cabig.ctcae.web.study.StudyLinkDisplayCell");
        model.addColumn(columnShortTitle);
    }

    private void addAssignedIdentifier(TableModel model) {
        Column columnShortTitle = model.getColumnInstance();
        columnShortTitle.setProperty("assignedIdentifier");
        columnShortTitle.setSortable(Boolean.TRUE);
        // columnShortTitle.setCell("gov.nih.nci.cabig.ctcae.web.study.StudyLinkDisplayCell");
        model.addColumn(columnShortTitle);
    }


}
