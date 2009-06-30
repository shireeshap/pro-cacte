package gov.nih.nci.ctcae.web.reports.graphical;

import org.jfree.data.category.CategoryDataset;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.chart.labels.AbstractCategoryItemLabelGenerator;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.ui.TextAnchor;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 2:42:11 PM
 */
public abstract class AbstractChartGenerator {

    private String title;
    private Integer total;
    private String rangeAxisLabel;
    private String domainAxisLabel;
    private boolean showPercentage = false;
    private String queryString;

    public AbstractChartGenerator(String title, String domainAxisLabel, String rangeAxisLabel, String queryString) {
        this.title = title;
        this.rangeAxisLabel = rangeAxisLabel;
        this.domainAxisLabel = domainAxisLabel;
        this.queryString = queryString;

    }

    public AbstractChartGenerator(String title, String domainAxisLabel, String rangeAxisLabel, boolean showPercentage, Integer total, String queryString) {
        this(title, domainAxisLabel, rangeAxisLabel, queryString);
        this.title = title;
        this.showPercentage = showPercentage;
        this.total = total;
    }                                                                               

    protected abstract CategoryDataset createDataSet(Object results);

    public JFreeChart getChart(Object results) {
        return createChart(createDataSet(results));
    }

    private JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart3D(
                title,                      // chart title
                domainAxisLabel,           // domain axis label
                rangeAxisLabel,                      // range axis label
                dataset,                    // data
                PlotOrientation.VERTICAL,   // orientation
                true,                       // include legend
                true,                       // tooltips?
                false                       // URLs?
        );

        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setDomainGridlinePaint(Color.gray);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.gray);
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setItemMargin(0.05);
        LabelGenerator lg = new LabelGenerator();
        ItemLabelPosition itemLabelPosition = new ItemLabelPosition(ItemLabelAnchor.INSIDE12, TextAnchor.CENTER_RIGHT, TextAnchor.CENTER_RIGHT, -Math.PI / 2.0);
        for (int i = 0; i < dataset.getRowCount(); i++) {
            renderer.setSeriesItemLabelGenerator(i, lg);
            renderer.setSeriesItemLabelsVisible(i, true);
            renderer.setSeriesItemLabelPaint(i, Color.WHITE);
            renderer.setSeriesPositiveItemLabelPosition(i, itemLabelPosition);
            renderer.setSeriesItemURLGenerator(i, new URLGenerator());
        }

        return chart;

    }

    class LabelGenerator extends AbstractCategoryItemLabelGenerator
            implements CategoryItemLabelGenerator {

        public LabelGenerator() {
            super("", NumberFormat.getInstance());
        }

        public String generateLabel(CategoryDataset dataset, int series, int category) {
            Number value = dataset.getValue(series, category);
            DecimalFormat df = new DecimalFormat("0.0");
            String label = df.format(value);
            if (showPercentage) {
                label += " (" + df.format((value.doubleValue() / total) * 100) + "%)";
            }
            return label;
        }
    }

    class URLGenerator implements CategoryURLGenerator {

        public String generateURL(CategoryDataset dataset, int series, int category) {
            String url = "javascript:showDetails('" + queryString + "&col=";
            Comparable categoryKey = dataset.getColumnKey(category);
            url += categoryKey.toString();
            Comparable seriesKey = dataset.getRowKey(series);
            url += "&ser=" + seriesKey.toString();
            url += "');";
            return url;
        }
    }
}