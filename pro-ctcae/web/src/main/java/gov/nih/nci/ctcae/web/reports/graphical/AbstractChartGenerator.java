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
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.ui.TextAnchor;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import gov.nih.nci.ctcae.web.ControllersUtils;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 2:42:11 PM
 */
public abstract class AbstractChartGenerator {
    private boolean isLineChart = false;
    protected String title;
    private Integer total;
    protected String rangeAxisLabel;
    protected String domainAxisLabel;
    private boolean showPercentage = false;
    private String queryString;
    private String reportType;

    public AbstractChartGenerator(String title, String domainAxisLabel, String rangeAxisLabel, String queryString, String reportType, boolean isLineChart) {
        this.title = title;
        this.rangeAxisLabel = rangeAxisLabel;
        this.domainAxisLabel = domainAxisLabel;
        this.queryString = queryString;
        this.isLineChart = isLineChart;
        this.reportType = reportType;
    }

    public AbstractChartGenerator(String title, String domainAxisLabel, String rangeAxisLabel, boolean showPercentage, Integer total, String queryString, String reportType, boolean isLineChart) {
        this(title, domainAxisLabel, rangeAxisLabel, queryString, reportType, isLineChart);
        this.title = title;
        this.showPercentage = showPercentage;
        this.total = total;
    }

    protected abstract CategoryDataset createDataSet(Object results);

    public JFreeChart getChart(Object results) {
        return createChart(createDataSet(results));
    }

    protected JFreeChart createChart(CategoryDataset dataset) {
        if (isLineChart) {
            JFreeChart chart = ChartFactory.createLineChart(
                    title,                      // chart title
                    domainAxisLabel,           // domain axis label
                    rangeAxisLabel,                      // range axis label
                    dataset,                    // data
                    PlotOrientation.VERTICAL,   // orientation
                    true,                       // include legend
                    true,                       // tooltips?
                    false                       // URLs?
            );
            formatLineChart(dataset, chart);
            return chart;
        } else {
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

            formatChart(dataset, chart);

            return chart;
        }

    }

    protected void formatLineChart(CategoryDataset dataset, JFreeChart chart) {
        chart.setBackgroundPaint(Color.white);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setDomainGridlinePaint(Color.gray);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.gray);
        LineAndShapeRenderer renderer
                = (LineAndShapeRenderer) plot.getRenderer();
        for (int i = 0; i < dataset.getRowCount(); i++) {
            renderer.setSeriesShapesVisible(i, true);
            renderer.setSeriesStroke(i, new BasicStroke(2.0f));
            renderer.setSeriesOutlineStroke(i, new BasicStroke(2.0f));
        }
        renderer.setDrawOutlines(true);
        renderer.setUseFillPaint(true);

    }

    protected void formatChart(CategoryDataset dataset, JFreeChart chart) {
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
            renderer.setSeriesItemLabelFont(i, new Font(null, Font.BOLD, 12));
            renderer.setSeriesItemLabelPaint(i, Color.BLACK);
            renderer.setSeriesPositiveItemLabelPosition(i, itemLabelPosition);
            renderer.setSeriesItemURLGenerator(i, new URLGenerator());
        }
    }

    class LabelGenerator extends AbstractCategoryItemLabelGenerator
            implements CategoryItemLabelGenerator {

        public LabelGenerator() {
            super("", NumberFormat.getInstance());
        }

        public String generateLabel(CategoryDataset dataset, int series, int category) {
            Number value = dataset.getValue(series, category);
            DecimalFormat df = new DecimalFormat("0.0");
            String label = value.toString();
            if (!(value instanceof Integer)) {
                label = df.format(value);
            }
            if (showPercentage) {
                if (total > -1) {
                    label += " (" + df.format((value.doubleValue() / total) * 100) + "%)";
                } else {
                    label = value + "%";
                }
            }
            return label;
        }
    }

    class URLGenerator implements CategoryURLGenerator {

        public String generateURL(CategoryDataset dataset, int series, int category) {
            StringBuffer url = new StringBuffer();
            url.append("javascript:showDetails('").append(queryString);
            String categoryVal = dataset.getColumnKey(category).toString();
            String seriesVal = dataset.getRowKey(series).toString();
            if ("SYMPTOM_SUMMARY_BAR_CHART".equals(reportType)) {
                url.append("&att=").append(seriesVal);
                url.append("&grade=").append(categoryVal);
            }
            if ("SYMPTOM_OVER_TIME_BAR_CHART".equals(reportType)) {
                url.append("&att=").append(seriesVal);
                url.append("&period=").append(categoryVal);
            }
            if ("SYMPTOM_OVER_TIME_STACKED_BAR_CHART".equals(reportType)) {
                url.append("&grade=").append(seriesVal.substring(seriesVal.indexOf(" - ") + 3));
                if (categoryVal.indexOf("[") != -1) {
                    url.append("&period=").append(categoryVal.substring(0, categoryVal.indexOf("[") - 1));
                } else {
                    url.append("&period=").append(categoryVal);
                    url = new StringBuffer(ControllersUtils.removeParameterFromQueryString(url.toString(), "arms"));
                    String arm = seriesVal.substring(0, seriesVal.indexOf(" - "));
                    url.append("&arms=").append(arm).append(",");
                }
            }
            url.append("')");
            return url.toString();
        }
    }
}