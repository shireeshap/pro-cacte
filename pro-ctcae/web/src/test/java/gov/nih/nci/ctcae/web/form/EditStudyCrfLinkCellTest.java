package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.StudyCrf;
import gov.nih.nci.ctcae.web.table.cell.AbstractCellTestCase;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.bean.Row;


/**
 * @author Vinay Kumar
 *         Date: Dec 8, 2008
 */
public class EditStudyCrfLinkCellTest extends AbstractCellTestCase {

	private EditStudyCrfLinkCell cell;
	private StudyCrf studyCrf;
	private Column testTitleColumn;


	@Override
	protected void setUp() throws Exception {
		super.setUp();

		cell = new EditStudyCrfLinkCell();
		testTitleColumn = new Column(model);
		//  testOptionsColumn.setCell("gov.nih.nci.ctcae.web.form.EditStudyCrfLinkCell");
		//  testOptionsColumn.setTitle("Options");

	}


	public void testEditFormLinkForDraftFormOnly() {
		studyCrf = new StudyCrf();
		CRF crf = new CRF();
		crf.setStatus(CrfStatus.DRAFT);

		studyCrf.setCrf(crf);
		studyCrf.setId(1);
		studyCrf.getCrf().setTitle("Test Crf");
		model.addColumn(testTitleColumn);
		row = new Row(model);
		model.addRow(row);
		model.setCurrentRowBean(studyCrf);


		assertEquals("only draft form should be editable", "<a href=\"/pages/form/editForm?studyCrfId=1\">Test Crf</a>", cell.getCellValue(model, testTitleColumn));
	}

	public void testNoHyperLinkForFormStatusLinkForForm() {
		studyCrf = new StudyCrf();
		CRF crf = new CRF();
		crf.setStatus(CrfStatus.RELEASED);

		studyCrf.setCrf(crf);
		studyCrf.setId(1);
		studyCrf.getCrf().setTitle("Test Crf");
		studyCrf.getCrf().setStatus(CrfStatus.RELEASED);
		model.addColumn(testTitleColumn);
		row = new Row(model);
		model.addRow(row);
		model.setCurrentRowBean(studyCrf);


		assertEquals("you can not edit a released form", crf.getTitle(), cell.getCellValue(model, testTitleColumn));
	}


}