package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.reports.StudyLevelReportExcelView;

import java.util.HashMap;

/**
 * @author mehul gulati
 *         Date: Oct 19, 2010
 */
public class StudyLevelReportExcelViewTest extends AbstractWebTestCase {

    StudyLevelReportExcelView excelView;

    public void testBuildExcelDocument() throws Exception {
        excelView = new StudyLevelReportExcelView();
        HashMap map = new HashMap();
        Study study = new Study();

        request.getSession().setAttribute("study", study);
        CRF crf = new CRF();
        request.getSession().setAttribute("crf", crf);
        excelView.render(map,request, response);

    }
}
