package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import gov.nih.nci.ctcae.web.reports.graphical.SymptomSummaryReportResultsController;
import gov.nih.nci.ctcae.web.reports.graphical.SymptomOverTimeReportResultsController;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.CRF;

import java.util.Map;
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
public class SymptomOverTimeReportTest extends AbstractWebTestCase {


    public void testReportController() throws Exception {

        Study study = StudyTestHelper.getDefaultStudy();
        CRF crf = study.getCrfs().get(0);
        Integer symptomId = crf.getAllCrfPageItems().get(13).getProCtcQuestion().getProCtcTerm().getId();

        SymptomOverTimeReportResultsController controller = new SymptomOverTimeReportResultsController();
        controller.setGenericRepository(genericRepository);
        request.setParameter("crfId", crf.getId().toString());
        request.setParameter("symptom", symptomId.toString());
        request.setParameter("attributes", ",Severity,Frequency");
        request.setParameter("group", "week");
        request.setMethod("GET");

        ModelAndView modelAndView = controller.handleRequest(request, response);
        Map m = modelAndView.getModel();
//        assertNotNull(m.get("worstResponseChartFileName"));
//        assertNotNull(m.get("worstResponseChartImageMap"));
//        assertNotNull(m.get("allResponseChartFileName"));
//        assertNotNull(m.get("allResponseChartImageMap"));
        JFreeChart allResponseChart = (JFreeChart) m.get("allResponseChart");
        JFreeChart worstResponseChart = (JFreeChart) m.get("worstResponseChart");
        showCharts(allResponseChart, worstResponseChart);


    }

    public void testReportDetailsController() throws Exception {

        Study study = StudyTestHelper.getDefaultStudy();
        CRF crf = study.getCrfs().get(0);
        Integer symptomId = crf.getAllCrfPageItems().get(13).getProCtcQuestion().getProCtcTerm().getId();

        SymptomOverTimeReportDetailsController controller = new SymptomOverTimeReportDetailsController();
        controller.setGenericRepository(genericRepository);
        request.setParameter("crfId", crf.getId().toString());
        request.setParameter("symptom", symptomId.toString());
        request.setParameter("attributes", ",Severity,Frequency");
        request.setParameter("group", "week");
        request.setParameter("col", "Week 26");
        request.setParameter("ser", "Frequency");
        request.setParameter("type", "WOR");
        request.setMethod("GET");

        ModelAndView modelAndView = controller.handleRequest(request, response);
        Map m = modelAndView.getModel();
    }

    private void showCharts(JFreeChart allResponseChart, JFreeChart worstResponseChart) throws InterruptedException {
        ChartPanel chartPanel1 = new ChartPanel(worstResponseChart, false);
        chartPanel1.setPreferredSize(new Dimension(500, 270));
        ChartPanel chartPanel2 = new ChartPanel(allResponseChart, false);
        chartPanel2.setPreferredSize(new Dimension(500, 270));
        ApplicationFrame frame = new ApplicationFrame("MyFrame");
        frame.setLayout(new GridLayout(1, 1));
        frame.add(chartPanel1);
        frame.add(chartPanel2);
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);
        while (frame.isVisible()) {
            Thread.sleep(2000);
        }
    }

}