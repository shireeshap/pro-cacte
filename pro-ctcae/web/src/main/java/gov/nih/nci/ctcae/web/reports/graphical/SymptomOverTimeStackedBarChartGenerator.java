package gov.nih.nci.ctcae.web.reports.graphical;

import org.apache.commons.lang.StringUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.KeyToGroupMap;
import org.jfree.ui.TextAnchor;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;

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
        super(title, domainAxisLabel, rangeAxisLabel, true, -1, queryString,"SYMPTOM_OVER_TIME_STACKED_BAR_CHART");
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
                dataset.addValue(new Float(df.format(map.get(grade) * 100 / sum)), grade, period + " [N=" + sum + "]");
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
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        renderer.setSeriesPaint(0, new Color(222, 216, 216));
        renderer.setSeriesPaint(1, new Color(240, 249, 78));
        renderer.setSeriesPaint(2, new Color(244, 187, 89));
        renderer.setSeriesPaint(3, new Color(235, 122, 107));
        renderer.setSeriesPaint(4, new Color(255, 54, 54));

        return chart;
    }


}