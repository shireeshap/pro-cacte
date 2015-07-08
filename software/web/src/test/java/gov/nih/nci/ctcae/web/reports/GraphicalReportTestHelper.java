package gov.nih.nci.ctcae.web.reports;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.util.ArrayList;

public class GraphicalReportTestHelper {

    //    private static booelan showCharts = true;
    private static boolean showCharts = false;

    public static void showCharts(ArrayList<Object[]> charts) throws InterruptedException {
        if (showCharts) {
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
            while (frame.isVisible()) {
                Thread.sleep(5000);
            }
        }
    }

    public static void showCharts(JFreeChart chart) throws InterruptedException {
        ArrayList<Object[]> charts = new ArrayList<Object[]>();
        Object[] obj = new Object[4];
        obj[3] = chart;
        charts.add(obj);
        showCharts(charts);

    }
}
