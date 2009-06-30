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
public class SymptomOverTimeAllResponsesChartGenerator extends AbstractChartGenerator {
    public SymptomOverTimeAllResponsesChartGenerator(String title, String domainAxisLabel, String rangeAxisLabel,String queryString) {
        super(title,domainAxisLabel, rangeAxisLabel,queryString+"&type=ALL");
    }

    public CategoryDataset createDataSet(Object results) {

        java.util.List temp = (java.util.List) results;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Object obj : temp) {
            Object[] o = (Object[]) obj;
            dataset.addValue((Double) o[0], ((ProCtcQuestionType) o[1]).getDisplayName(), (String) o[2]);
        }
        return dataset;
    }
}