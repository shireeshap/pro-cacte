package gov.nih.nci.ctcae.web.reports.graphical;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;

import java.util.*;
import java.awt.*;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 2:42:11 PM
 */
public class SymptomSummaryWorstResponsesChartGenerator extends AbstractChartGenerator {
    private String countString = "";
    private boolean multipleArms = false;

    public SymptomSummaryWorstResponsesChartGenerator(String title, String domainAxisLabel, String rangeAxisLabel, String queryString, boolean multipleArms, int totalParticipant, String countString) {
        super(title, domainAxisLabel, rangeAxisLabel, true, totalParticipant, queryString, "SYMPTOM_SUMMARY_BAR_CHART", multipleArms);
        this.countString = countString;
        this.multipleArms = multipleArms;
    }

    public CategoryDataset createDataSet(Object results) {
        TreeMap<String, TreeMap<String, Integer>> temp = (TreeMap<String, TreeMap<String, Integer>>) results;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (String attribute : temp.keySet()) {
            TreeMap<String, Integer> map = temp.get(attribute);
            ArrayList<String> levels = new ArrayList(map.keySet());
            Collections.sort(levels);
            for (String level : levels) {
                dataset.addValue(map.get(level), attribute, level);
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
        if (multipleArms) {
            TextTitle source = new TextTitle(countString);
            source.setFont(new Font("SansSerif", Font.PLAIN, 10));
            source.setPosition(RectangleEdge.TOP);
            source.setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.addSubtitle(source);
        }
    }
}