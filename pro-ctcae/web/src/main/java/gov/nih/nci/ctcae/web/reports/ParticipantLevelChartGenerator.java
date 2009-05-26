package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.AbstractCategoryItemLabelGenerator;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 2:42:11 PM
 */
public class ParticipantLevelChartGenerator {

    ArrayList<String> typesInSymptom = new ArrayList<String>();

    public JFreeChart getChartForSymptom(TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> results, ArrayList<Date> dates, Integer inputSymptomId, ArrayList<String> arrSelectedTypes) {
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
    private CategoryDataset createDataset(HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> dataForChart, ArrayList<Date> dates, ArrayList<String> arrSelectedTypes) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int i = 0;
        for (Date date : dates) {
            for (ProCtcQuestion proCtcQuestion : dataForChart.keySet()) {
                ArrayList<ProCtcValidValue> proCtcValidValues = dataForChart.get(proCtcQuestion);
                ProCtcValidValue proCtcValidValue = proCtcValidValues.get(i);
                String questionType = proCtcQuestion.getProCtcQuestionType().getDisplayName();
                if (arrSelectedTypes == null) {
                    dataset.addValue(proCtcValidValue.getDisplayOrder(), questionType, DateUtils.format(date));
                } else {
                    if (arrSelectedTypes.contains(questionType)) {
                        dataset.addValue(proCtcValidValue.getDisplayOrder(), questionType, DateUtils.format(date));
                    }
                }
                if (!typesInSymptom.contains(questionType)) {
                    typesInSymptom.add(questionType);
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

    private JFreeChart createChart(CategoryDataset dataset, ProCtcTerm selectedTerm) {

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
        renderer.setItemMargin(0.05);

        CategoryItemRenderer categoryItemRenderer = plot.getRenderer();
        categoryItemRenderer.setBaseItemLabelsVisible(true);
        // set up gradient paints for series...

        categoryItemRenderer.setSeriesPaint(0, Color.blue);
        categoryItemRenderer.setSeriesPaint(1, Color.green);
        categoryItemRenderer.setSeriesPaint(2, Color.red);
        categoryItemRenderer.setSeriesPaint(3, Color.cyan);
        categoryItemRenderer.setSeriesPaint(4, Color.orange);
        ItemLabelPosition itemLabelPosition = new ItemLabelPosition(ItemLabelAnchor.INSIDE12, TextAnchor.CENTER_RIGHT, TextAnchor.CENTER_RIGHT, -Math.PI / 2.0);

        for (int i = 0; i < 5; i++) {
            categoryItemRenderer.setSeriesItemLabelsVisible(i, true);
            categoryItemRenderer.setSeriesItemLabelGenerator(i, new LabelGenerator());
            categoryItemRenderer.setSeriesPositiveItemLabelPosition(i, itemLabelPosition);
            categoryItemRenderer.setSeriesItemLabelPaint(i, Color.white);
        }
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(
                        Math.PI / 6.0));

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
            String questionType = typesInSymptom.get(series);
            ProCtcQuestionType proCtcQuestionType = ProCtcQuestionType.getByCode(questionType);
            Number value = dataset.getValue(series, category);
            String label = proCtcQuestionType.getValidValues()[value.intValue()];
            return label;
        }
    }
}
