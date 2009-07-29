package gov.nih.nci.ctcae.web.reports.graphical;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.*;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 2:42:11 PM
 */
public class SymptomOverTimeWorstResponsesLineChartGenerator extends AbstractChartGenerator {
    public SymptomOverTimeWorstResponsesLineChartGenerator(String title, String domainAxisLabel, String rangeAxisLabel, String queryString) {
        super(title, domainAxisLabel, rangeAxisLabel, queryString, "SYMPTOM_OVER_TIME_BAR_CHART");
    }

    public CategoryDataset createDataSet(Object results) {
        TreeMap<String, TreeMap<Integer, Float>> temp = (TreeMap<String, TreeMap<Integer, Float>>) results;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (String attribute : temp.keySet()) {
            TreeMap<Integer, Float> map = temp.get(attribute);
            ArrayList<String> l = new ArrayList(map.keySet());
            Collections.sort(l);
            for (String i : l) {
                dataset.addValue(map.get(i), attribute, i);
            }
        }
        return dataset;
    }

    protected JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createLineChart3D(
                title,                      // chart title
                domainAxisLabel,           // domain axis label
                rangeAxisLabel,                      // range axis label
                dataset,                    // data
                PlotOrientation.VERTICAL,   // orientation
                true,                       // include legend
                true,                       // tooltips?
                false                       // URLs?
        );
        return chart;

    }


}