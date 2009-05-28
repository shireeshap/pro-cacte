package gov.nih.nci.ctcae.web.reports;

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
import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.TreeMap;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 2:42:11 PM
 */
public class SymptomOverTimeChartGenerator {

    private String queryString;
    private int firstCat = 0;

    public JFreeChart getChart(TreeMap<Integer, Float> results, String symptom, String attribute, String dates, String queryString, Long l, int totalSchedules, String group) {
        this.queryString = queryString;
        CategoryDataset dataset = createDataset(results, group);
        JFreeChart chart = createChart(dataset, symptom, attribute, dates, l, totalSchedules);
        return chart;
    }

    /**
     * Creates the dataset.
     *
     * @param results
     * @param group
     * @return the category dataset
     */


    private CategoryDataset createDataset(TreeMap<Integer, Float> results, String group) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        if (results.keySet().size() > 0) {
            firstCat = (Integer) results.keySet().toArray()[0];
        }

        for (Integer i : results.keySet()) {
            dataset.addValue(results.get(i), "", StringUtils.capitalize(group) + " " + (i - firstCat + 1));
        }
        return dataset;
    }

    /**
     * Creates the chart.
     *
     * @param dataset        the dataset
     * @param symptom
     * @param attribute      @return the j free chart
     * @param l
     * @param totalSchedules
     */

    private JFreeChart createChart(CategoryDataset dataset, String symptom, String attribute, String dates, Long l, int totalSchedules) {

        String title = "Average Participant Reported Responses vs. Time for the " + attribute + " of " + symptom + " symptom (" + dates + " responses)";
        JFreeChart chart = ChartFactory.createBarChart3D(
                title,       // chart title
                "Week #",               // domain axis label
                "Average Response (n=" + totalSchedules + ", p=" + l + ")",                  // range axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                false,                     // include legend
                false,                     // tooltips?
                true                     // URLs?
        );

        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setDomainGridlinePaint(Color.gray);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.gray);

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        CategoryItemRenderer categoryItemRenderer = plot.getRenderer();
        categoryItemRenderer.setBaseItemLabelsVisible(true);
        categoryItemRenderer.setSeriesItemLabelsVisible(0, true);
        categoryItemRenderer.setSeriesItemLabelPaint(0, Color.white);
        ItemLabelPosition itemLabelPosition1 = new ItemLabelPosition(ItemLabelAnchor.INSIDE12, TextAnchor.TOP_CENTER, TextAnchor.CENTER, 0);
        categoryItemRenderer.setSeriesPositiveItemLabelPosition(0, itemLabelPosition1);
        StandardCategoryItemLabelGenerator lg1 = new StandardCategoryItemLabelGenerator("{2}", new DecimalFormat("0.0"));
        categoryItemRenderer.setSeriesItemLabelGenerator(0, lg1);
        categoryItemRenderer.setSeriesItemURLGenerator(0, new URLGenerator());
        categoryItemRenderer.setSeriesItemLabelFont(0, new Font("SansSerif", Font.PLAIN, 13));
        categoryItemRenderer.setSeriesPaint(0, Color.blue);
        return chart;

    }

    class URLGenerator implements CategoryURLGenerator {

        public String generateURL(CategoryDataset dataset, int series, int category) {
            String url = "javascript:showDetails('" + queryString + "&cat=";
            Comparable categoryKey = dataset.getColumnKey(category);
            String cat = categoryKey.toString();
            cat = cat.substring(cat.indexOf(' ') + 1);
            int weekInYear = Integer.parseInt(cat) + firstCat - 1;
            url += weekInYear;
            url += "');";
            return url;
        }
    }
}