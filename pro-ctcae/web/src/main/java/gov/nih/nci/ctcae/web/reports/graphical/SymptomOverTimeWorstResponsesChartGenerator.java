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
public class SymptomOverTimeWorstResponsesChartGenerator extends AbstractChartGenerator {
    public SymptomOverTimeWorstResponsesChartGenerator(String title, String domainAxisLabel, String rangeAxisLabel, String queryString, boolean multipleArms) {
        super(title, domainAxisLabel, rangeAxisLabel, queryString, "SYMPTOM_OVER_TIME_BAR_CHART",multipleArms);
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
}