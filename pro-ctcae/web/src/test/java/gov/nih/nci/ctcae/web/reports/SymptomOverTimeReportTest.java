package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.Arm;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import gov.nih.nci.ctcae.web.reports.graphical.SymptomOverTimeReportResultsController;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.springframework.web.servlet.ModelAndView;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

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
        request.setParameter("crf", crf.getId().toString());
        request.setParameter("symptom", symptomId.toString());
        request.setParameter("attributes", ",Severity,Frequency");
        request.setParameter("group", "cycle");
        request.setMethod("GET");

        ModelAndView modelAndView = controller.handleRequest(request, response);
        Map m = modelAndView.getModel();
        ArrayList<Object[]> charts = (ArrayList<Object[]>) m.get("results");
        showCharts(charts);
    }

    public void testReportControllerMultipleArms() throws Exception {

        Study study = StudyTestHelper.getDefaultStudy();
        String arms = "";
        for (Arm arm : study.getArms()) {
            arms += arm.getId() + ",";
        }
        CRF crf = study.getCrfs().get(0);
        Integer symptomId = crf.getAllCrfPageItems().get(13).getProCtcQuestion().getProCtcTerm().getId();

        SymptomOverTimeReportResultsController controller = new SymptomOverTimeReportResultsController();
        controller.setGenericRepository(genericRepository);
        request.setParameter("crf", crf.getId().toString());
        request.setParameter("symptom", symptomId.toString());
        request.setParameter("attributes", ",Severity,Frequency");
        request.setParameter("group", "cycle");
        request.setParameter("arms", arms);
        request.setMethod("GET");

        ModelAndView modelAndView = controller.handleRequest(request, response);
        Map m = modelAndView.getModel();
        ArrayList<Object[]> charts = (ArrayList<Object[]>) m.get("results");
        showCharts(charts);
    }

    public void testReportControllerSingleArm() throws Exception {

        Study study = StudyTestHelper.getDefaultStudy();
        String arms = study.getArms().get(0).getId() + ",";
        CRF crf = study.getCrfs().get(0);
        Integer symptomId = crf.getAllCrfPageItems().get(13).getProCtcQuestion().getProCtcTerm().getId();

        SymptomOverTimeReportResultsController controller = new SymptomOverTimeReportResultsController();
        controller.setGenericRepository(genericRepository);
        request.setParameter("crf", crf.getId().toString());
        request.setParameter("symptom", symptomId.toString());
        request.setParameter("attributes", ",Severity,Frequency");
        request.setParameter("group", "cycle");
        request.setParameter("arms", arms);
        request.setMethod("GET");

        ModelAndView modelAndView = controller.handleRequest(request, response);
        Map m = modelAndView.getModel();
        ArrayList<Object[]> charts = (ArrayList<Object[]>) m.get("results");
        showCharts(charts);
    }

    public void testReportDetailsController() throws Exception {

        Study study = StudyTestHelper.getDefaultStudy();
        CRF crf = study.getCrfs().get(0);
        Integer symptomId = crf.getAllCrfPageItems().get(13).getProCtcQuestion().getProCtcTerm().getId();

        ReportDetailsController controller = new ReportDetailsController();
        controller.setGenericRepository(genericRepository);
        request.setParameter("crf", crf.getId().toString());
        request.setParameter("symptom", symptomId.toString());
        request.setParameter("attributes", ",Severity,Frequency");
        request.setParameter("group", "week");
        request.setParameter("col", "Week 26");
        request.setParameter("ser", "Frequency");
        request.setParameter("type", "WOR");
        request.setParameter("sum", "2");
        request.setMethod("GET");

        ModelAndView modelAndView = controller.handleRequest(request, response);
    }

    private void showCharts(ArrayList<Object[]> charts) throws InterruptedException {
        ApplicationFrame frame = new ApplicationFrame("MyFrame");
        frame.setLayout(new FlowLayout());
        for (Object[] chartArr : charts) {
            JFreeChart chart = (JFreeChart) chartArr[3];
            ChartPanel chartPanel = new ChartPanel(chart, false);
            chartPanel.setPreferredSize(new Dimension(500, 270));
            frame.add(chartPanel);
        }
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);
        Thread.sleep(20000);
    }

}