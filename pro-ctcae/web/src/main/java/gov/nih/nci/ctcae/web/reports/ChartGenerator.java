package gov.nih.nci.ctcae.web.reports;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.Range;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.commons.utils.DateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.TreeMap;
import java.awt.*;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 2:42:11 PM
 */
public class ChartGenerator {

    public static JFreeChart getChartForSymptom(TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> results, ArrayList<Date> dates, Integer inputSymptomId, ArrayList<String> arrSelectedTypes) {
        ProCtcTerm selectedTerm = null;
        HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> dataForChart = null;
        for (ProCtcTerm proCtcTerm : results.keySet()) {
            if (proCtcTerm.getId().equals(inputSymptomId)) {
                selectedTerm = proCtcTerm;
                dataForChart = results.get(selectedTerm);
                break;
            }
        }
        CategoryDataset dataset = createDataset(dataForChart, dates, arrSelectedTypes);
        JFreeChart chart = createChart(dataset, selectedTerm);
        return chart;
    }

    /**
     * Creates the dataset.
     *
     * @param dataForChart
     * @param dates
     * @param arrSelectedTypes
     * @return the category dataset
     */
    private static CategoryDataset createDataset(HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> dataForChart, ArrayList<Date> dates, ArrayList<String> arrSelectedTypes) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();


        int i = 0;
        for (Date date : dates) {
            for (ProCtcQuestion proCtcQuestion : dataForChart.keySet()) {
                ArrayList<ProCtcValidValue> proCtcValidValues = dataForChart.get(proCtcQuestion);
                ProCtcValidValue proCtcValidValue = proCtcValidValues.get(i);
                String questionType = proCtcValidValue.getProCtcQuestion().getProCtcQuestionType().getDisplayName();
                if (arrSelectedTypes == null) {
                    dataset.addValue(proCtcValidValue.getDisplayOrder(), questionType, DateUtils.format(date));
                } else {
                    if (arrSelectedTypes.contains(questionType)) {
                        dataset.addValue(proCtcValidValue.getDisplayOrder(), questionType, DateUtils.format(date));
                    }
                }
            }
            i++;
        }

        return dataset;
    }

    /**
     * Creates the chart.
     *
     * @param dataset      the dataset
     * @param selectedTerm
     * @return the j free chart
     */

    private static JFreeChart createChart(CategoryDataset dataset, ProCtcTerm selectedTerm) {

        String title = "";
        if (selectedTerm != null) {
            title = selectedTerm.getTerm();
        }
        JFreeChart chart = ChartFactory.createBarChart(
                title,       // chart title
                "Date",               // domain axis label
                "Value",                  // range axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
        );

        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);


        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        Range range = new Range(0, 4);
        rangeAxis.setRange(range);
        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        CategoryItemRenderer categoryItemRenderer = plot.getRenderer();
        categoryItemRenderer.setBaseItemLabelsVisible(true);
        // set up gradient paints for series...
        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue,
                0.0f, 0.0f, new Color(0, 0, 64));
        GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green,
                0.0f, 0.0f, new Color(0, 64, 0));
        GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red,
                0.0f, 0.0f, new Color(64, 0, 0));
        GradientPaint gp3 = new GradientPaint(0.0f, 0.0f, Color.cyan,
                0.0f, 0.0f, new Color(64, 0, 0));
        GradientPaint gp4 = new GradientPaint(0.0f, 0.0f, Color.orange,
                0.0f, 0.0f, new Color(64, 0, 0));
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);
        renderer.setSeriesPaint(3, gp3);
        renderer.setSeriesPaint(4, gp4);
        renderer.setSeriesShape(0, new Rectangle(100, 1));
        renderer.setSeriesShape(1, new Rectangle(100, 100));
        renderer.setSeriesShape(2, new Rectangle(100, 200));
        renderer.setSeriesShape(3, new Rectangle(100, 300));
        renderer.setSeriesShape(4, new Rectangle(100, 400));

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(
                        Math.PI / 6.0));
        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;

    }
}
