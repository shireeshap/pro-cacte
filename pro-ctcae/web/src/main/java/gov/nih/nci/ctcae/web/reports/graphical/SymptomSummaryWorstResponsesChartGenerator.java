package gov.nih.nci.ctcae.web.reports.graphical;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;

import java.util.*;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 2:42:11 PM
 */
public class SymptomSummaryWorstResponsesChartGenerator extends AbstractChartGenerator {

    public SymptomSummaryWorstResponsesChartGenerator(String title, String domainAxisLabel, String rangeAxisLabel, String queryString, boolean multipleArms, int totalParticipant) {
        super(title, domainAxisLabel, rangeAxisLabel, true, totalParticipant, queryString, "SYMPTOM_SUMMARY_BAR_CHART", multipleArms);
    }

    public CategoryDataset createDataSet(Object results) {
        TreeMap<String, TreeMap<Integer, Integer>> temp = (TreeMap<String, TreeMap<Integer, Integer>>) results;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (String a : temp.keySet()) {
            TreeMap<Integer, Integer> map = temp.get(a);
            ArrayList<Integer> l = new ArrayList(map.keySet());
            Collections.sort(l);
            for (Integer i : l) {
                dataset.addValue(map.get(i), a, i);
            }
        }
        return dataset;
    }

    @Override
    protected void formatLineChart(CategoryDataset dataset, JFreeChart chart) {
        super.formatLineChart(dataset, chart);
        CategoryPlot plot = chart.getCategoryPlot();
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

    }
}