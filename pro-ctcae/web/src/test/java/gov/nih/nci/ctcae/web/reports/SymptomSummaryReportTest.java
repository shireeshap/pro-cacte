package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import gov.nih.nci.ctcae.web.reports.graphical.SymptomSummaryReportResultsController;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.CrfTestHelper;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.query.reports.SymptomSummaryWorstResponsesQuery;

import java.util.*;
import java.util.List;
import java.awt.*;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.ApplicationFrame;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Harsh Agarwal
 * @since Mar 18, 2009
 */
public class SymptomSummaryReportTest extends AbstractWebTestCase {


    public void testController() throws Exception {

        Study study = StudyTestHelper.getDefaultStudy();
        CRF crf = study.getCrfs().get(0);
        Integer symptomId = crf.getAllCrfPageItems().get(13).getProCtcQuestion().getProCtcTerm().getId();

        SymptomSummaryReportResultsController controller = new SymptomSummaryReportResultsController();
        controller.setGenericRepository(genericRepository);
        request.setParameter("crf", crf.getId().toString());
        request.setParameter("symptom", symptomId.toString());
        request.setParameter("attributes", ",Severity,Frequency");
        request.setMethod("GET");

        ModelAndView modelAndView = controller.handleRequest(request, response);
        Map m = modelAndView.getModel();
        assertNotNull(m.get("worstResponseChartFileName"));
        assertNotNull(m.get("worstResponseChartImageMap"));
//        JFreeChart worstResponseChart = (JFreeChart) m.get("worstResponseChart");
//        showCharts( worstResponseChart);


    }

    public void testController_table() throws Exception {

        Study study = StudyTestHelper.getDefaultStudy();
        CRF crf = study.getCrfs().get(0);

        SymptomSummaryReportResultsController controller = new SymptomSummaryReportResultsController();
        controller.setGenericRepository(genericRepository);
        request.setParameter("crf", crf.getId().toString());
        request.setParameter("symptom", "-1");
        request.setMethod("GET");

        ModelAndView modelAndView = controller.handleRequest(request, response);
        Map m = modelAndView.getModel();
        assertNotNull(m.get("results"));
        assertEquals("reports/symptomsummarytable",modelAndView.getViewName());

    }

    private void showCharts(JFreeChart worstResponseChart) throws InterruptedException {
        ChartPanel chartPanel1 = new ChartPanel(worstResponseChart, false);
        chartPanel1.setPreferredSize(new Dimension(500, 270));
        ApplicationFrame frame = new ApplicationFrame("MyFrame");
        frame.setLayout(new GridLayout(1, 1));
        frame.add(chartPanel1);
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);
        while (frame.isVisible()) {
            Thread.sleep(2000);
        }
    }

    public void testQuery() {
        SymptomSummaryWorstResponsesQuery query = new SymptomSummaryWorstResponsesQuery();
        query.filterByCrf(StudyTestHelper.getDefaultStudy().getCrfs().get(0).getId());
        List<Persistable> l = genericRepository.find(query);
        assertTrue(l.size() > 0);
    }

}
