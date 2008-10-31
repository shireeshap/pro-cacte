package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.web.AbstractTableModel;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class StudyTableModel extends AbstractTableModel {

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

		}
		return "";
	}

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

	private void addStudyCoordinatingCenter(TableModel model) {
		Column columnSponsorCode = model.getColumnInstance();
		columnSponsorCode.setTitle("Coordinating center");
		columnSponsorCode
				.setProperty("studyCoordinatingCenter.organization.nciInstituteCode");
		columnSponsorCode.setAlias("studyCoordinatingCenter");

		columnSponsorCode.setSortable(Boolean.TRUE);
		model.addColumn(columnSponsorCode);
	}

	private void addSponsorColumn(TableModel model) {
		Column columnSponsorCode = model.getColumnInstance();
		columnSponsorCode.setTitle("Funding sponsor");
		columnSponsorCode
				.setProperty("studyFundingSponsor.organization.nciInstituteCode");
		columnSponsorCode.setAlias("studyFundingSponsor");
		columnSponsorCode.setSortable(Boolean.TRUE);
		model.addColumn(columnSponsorCode);
	}

	private void addShorTitleColumn(TableModel model) {
		Column columnShortTitle = model.getColumnInstance();
        columnShortTitle.setTitle("Short title");
		columnShortTitle.setProperty("shortTitle");
		columnShortTitle.setSortable(Boolean.TRUE);
		// columnShortTitle.setCell("gov.nih.nci.cabig.ctcae.web.study.StudyLinkDisplayCell");
		model.addColumn(columnShortTitle);
	}

	private void addAssignedIdentifier(TableModel model) {
		Column columnShortTitle = model.getColumnInstance();
        columnShortTitle.setTitle("Study identifier");
        columnShortTitle.setProperty("assignedIdentifier");
		columnShortTitle.setSortable(Boolean.TRUE);
		// columnShortTitle.setCell("gov.nih.nci.ctcae.web.study.SelectedStudyCell");
		model.addColumn(columnShortTitle);
	}

	private void addAssignedIdentifierForSelection(TableModel model) {
		Column columnShortTitle = model.getColumnInstance();
		columnShortTitle.setTitle("Study identifier");
		columnShortTitle.setProperty("assignedIdentifier");
		columnShortTitle.setSortable(Boolean.TRUE);
		columnShortTitle
				.setCell("gov.nih.nci.ctcae.web.study.SelectedStudyCell");
		model.addColumn(columnShortTitle);
	}

	private void addParticipantStudyIdentifierText(TableModel model) {
		Column columnParticipantStudyIdentifier = model.getColumnInstance();
		columnParticipantStudyIdentifier.setTitle("Patient study identifier");
		columnParticipantStudyIdentifier.setSortable(Boolean.FALSE);
		columnParticipantStudyIdentifier
				.setCell("gov.nih.nci.ctcae.web.study.TextBoxCell");
		model.addColumn(columnParticipantStudyIdentifier);
	}
}
