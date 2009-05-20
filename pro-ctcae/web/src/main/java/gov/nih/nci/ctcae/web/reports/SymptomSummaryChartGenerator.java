package gov.nih.nci.ctcae.web.reports;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import java.awt.*;
import java.util.List;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 2:42:11 PM
 */
public class SymptomSummaryChartGenerator {


    public JFreeChart getChart(java.util.List results, String symptom, String attribute, String dates) {
        CategoryDataset[] dataset = createDataset(results);
        JFreeChart chart = createChart(dataset, symptom, attribute, dates);
        return chart;
    }


    /**
     * Creates the dataset.
     *
     * @param results
     * @return the category dataset
     */


    private CategoryDataset[] createDataset(List results) {

        float sum = 0;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Object obj : results) {
            Object[] o = (Object[]) obj;
            dataset.addValue((Long) o[0], "", (String) o[1]);
            sum = sum + (Long) o[0];
        }
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        for (Object obj : results) {
            Object[] o = (Object[]) obj;
            dataset1.addValue(((Long) o[0] / sum) * 100, "", (String) o[1]);
        }
        return new CategoryDataset[]{dataset, dataset1};
    }

    /**
     * Creates the chart.
     *
     * @param dataset   the dataset
     * @param symptom
     * @param attribute @return the j free chart
     */

    private JFreeChart createChart(CategoryDataset[] dataset, String symptom, String attribute, String dates) {

        String title = "Participant reported responses for the " + attribute + " of " + symptom + " symptoms (" + dates + " responses)";
        JFreeChart chart = ChartFactory.createBarChart(
                title,       // chart title
                "Response",               // domain axis label
                "Number of Responses",                  // range axis label
                dataset[0],                  // data
                PlotOrientation.VERTICAL, // orientation
                false,                     // include legend
                false,                     // tooltips?
                false                     // URLs?
        );

        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);

        NumberAxis axis2 = new NumberAxis("Percentage(%)");
        plot.setRangeAxis(1, axis2);
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        plot.setDataset(1, dataset[1]);
        plot.mapDatasetToRangeAxis(1, 1);
        BarRenderer barrenderer1 = new BarRenderer();
        plot.setRenderer(1, barrenderer1);
        ItemLabelPosition itemLabelPosition = new ItemLabelPosition(ItemLabelAnchor.INSIDE12, TextAnchor.TOP_CENTER, TextAnchor.CENTER, 0);

        barrenderer1.setSeriesItemLabelsVisible(0, true);
        barrenderer1.setSeriesItemLabelPaint(0, Color.RED);
        LabelGenerator lg = new LabelGenerator();
        barrenderer1.setSeriesItemLabelGenerator(0, lg);


        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        CategoryItemRenderer categoryItemRenderer = plot.getRenderer();
        categoryItemRenderer.setBaseItemLabelsVisible(true);
        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue,
                0.0f, 0.0f, new Color(0, 0, 64));
        categoryItemRenderer.setSeriesPaint(0, gp0);
        categoryItemRenderer.setSeriesItemLabelsVisible(0, true);
        categoryItemRenderer.setSeriesItemLabelPaint(0, Color.white);
        categoryItemRenderer.setSeriesPositiveItemLabelPosition(0, itemLabelPosition);
        StandardCategoryItemLabelGenerator lg1 = new StandardCategoryItemLabelGenerator("{2}", new DecimalFormat("0"));
        categoryItemRenderer.setSeriesItemLabelGenerator(0, lg1);

        return chart;

    }

    class LabelGenerator extends AbstractCategoryItemLabelGenerator
            implements CategoryItemLabelGenerator {

        public LabelGenerator() {
            super("", NumberFormat.getInstance());
        }

        /**
         * Generates a label for the specified item. The label is typically a
         * formatted version of the data value, but any text can be used.
         *
         * @param dataset  the dataset (<code>null</code> not permitted).
         * @param series   the series index (zero-based).
         * @param category the category index (zero-based).
         * @return the label (possibly <code>null</code>).
         */
        public String generateLabel(CategoryDataset dataset,
                                    int series,
                                    int category) {
            Number value = dataset.getValue(series, category);
            DecimalFormat df = new DecimalFormat("0.00");
            String label = df.format(value) + "%";
            return label;
        }
    }
}