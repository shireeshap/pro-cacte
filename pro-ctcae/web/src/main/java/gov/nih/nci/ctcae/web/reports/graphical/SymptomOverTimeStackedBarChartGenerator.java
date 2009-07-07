package gov.nih.nci.ctcae.web.reports.graphical;

import org.apache.commons.lang.StringUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import java.awt.*;
import java.awt.List;
import java.text.DecimalFormat;
import java.util.*;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 2:42:11 PM
 */
public class SymptomOverTimeStackedBarChartGenerator extends AbstractChartGenerator {
    public SymptomOverTimeStackedBarChartGenerator(String title, String domainAxisLabel, String rangeAxisLabel, String queryString) {
        super(title, domainAxisLabel, rangeAxisLabel,true,-1, queryString + "&type=WOR");
    }

    public CategoryDataset createDataSet(Object results) {
        TreeMap<String, TreeMap<Integer, Integer>> temp = (TreeMap<String, TreeMap<Integer, Integer>>) results;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (String period : temp.keySet()) {
            TreeMap<Integer, Integer> map = temp.get(period);
            ArrayList<Integer> l = new ArrayList(map.keySet());
            Collections.sort(l);
            Integer sum = 0;
            for (Integer grade : l) {
                sum += map.get(grade);
            }
            DecimalFormat df = new DecimalFormat("0");
            for (Integer grade : l) {
                dataset.addValue(new Float(df.format(new Float(map.get(grade)) * 100 / sum)), grade, period + " [N=" + sum + "]");
            }
        }
        return dataset;
    }

    @Override
    protected JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createStackedBarChart3D(
                "",  // chart title
                rangeAxisLabel,                   // domain axis label
                domainAxisLabel,                      // range axis label
                dataset,                      // data
                PlotOrientation.VERTICAL,     // the plot orientation
                true,                         // include legend
                true,                         // tooltips
                false                         // urls
        );
        formatChart(dataset, chart);
        return chart;
    }
}