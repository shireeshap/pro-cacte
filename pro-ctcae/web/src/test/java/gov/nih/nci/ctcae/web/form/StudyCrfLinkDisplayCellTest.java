package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.StudyCrf;
import gov.nih.nci.ctcae.web.table.cell.AbstractCellTestCase;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.bean.Row;

/**
 * @author Mehul Gulati
 *         Date: Nov 5, 2008
 */
public class StudyCrfLinkDisplayCellTest extends AbstractCellTestCase {

	private StudyCrfLinkDisplayDetailsCell studyCrfLinkDisplayDetailsCell;
	private StudyCrf studyCrf;
	private Column testOptionsColumn;


	@Override
	protected void setUp() throws Exception {
		super.setUp();

		studyCrfLinkDisplayDetailsCell = new StudyCrfLinkDisplayDetailsCell();
		testOptionsColumn = new Column(model);
		//  testOptionsColumn.setCell("gov.nih.nci.ctcae.web.form.StudyCrfLinkDisplayDetailsCell");
		//  testOptionsColumn.setTitle("Options");

	}


	public void testScheduleFormStatusLink() {
		studyCrf = new StudyCrf();
		studyCrf.setCrf(new CRF());
		studyCrf.setId(1);
		studyCrf.getCrf().setStatus(CrfStatus.RELEASED);
		model.addColumn(testOptionsColumn);
		row = new Row(model);
		model.addRow(row);
		model.setCurrentRowBean(studyCrf);


		assertEquals("<a href=\"/pages/participant/schedulecrf?studyCrfId=1\">Schedule</a> | <a href=\"/pages/form/copyForm?studyCrfId=1\">Copy</a> | <a href=\"javascript:versionForm('1')\">Version</a>", studyCrfLinkDisplayDetailsCell.getCellValue(model, testOptionsColumn));
	}

	public void testReleaseFormStatusLink() {
		studyCrf = new StudyCrf();
		CRF crf = new CRF();
		crf.setTitle("title");
		studyCrf.setCrf(crf);
		studyCrf.setId(1);

		studyCrf.getCrf().setStatus(CrfStatus.DRAFT);
		model.addColumn(testOptionsColumn);
		row = new Row(model);
		model.addRow(row);
		model.setCurrentRowBean(studyCrf);


		assertEquals("<a href=\"javascript:releaseForm('1')\">Release&nbsp;&nbsp;</a> | <a href=\"/pages/form/copyForm?studyCrfId=1\">Copy</a> | <a href=\"javascript:deleteForm('1')\">Delete</a> | <a href=\"/pages/form/editForm?studyCrfId=1\">Edit</a>", studyCrfLinkDisplayDetailsCell.getCellValue(model, testOptionsColumn));
	}


}


