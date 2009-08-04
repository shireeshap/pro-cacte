package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.EmptyBlock;
import org.jfree.chart.block.BorderArrangement;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.title.CompositeTitle;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 2:42:11 PM
 */
public class ParticipantLevelChartGenerator {
    Color[] attributeColors = new Color[5];

    public ParticipantLevelChartGenerator() {
        Color c1 = new Color(255, 102, 102);
        Color c2 = new Color(102, 255, 102);
        Color c3 = new Color(255, 153, 51);
        Color c4 = new Color(0, 204, 204);
        Color c5 = new Color(255, 0, 255);

        attributeColors[0] = c1;
        attributeColors[1] = c2;
        attributeColors[2] = c3;
        attributeColors[3] = c4;
        attributeColors[4] = c5;
    }


    ArrayList<String> typesInSymptom = new ArrayList<String>();

    public JFreeChart getChartForSymptom(TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> results, ArrayList<String> dates, Integer inputSymptomId, ArrayList<String> arrSelectedTypes, String baselineDate) {
        ProCtcTerm selectedTerm = null;
        HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> dataForChart = null;
        for (ProCtcTerm proCtcTerm : results.keySet()) {
            if (proCtcTerm.getId().equals(inputSymptomId)) {
                selectedTerm = proCtcTerm;
                dataForChart = results.get(selectedTerm);
                break;
            }
        }
        DefaultCategoryDataset baselineDataSet = new DefaultCategoryDataset();
        CategoryDataset primaryDataSet = createDataset(dataForChart, dates, arrSelectedTypes, baselineDate, baselineDataSet);
        return createChart(primaryDataSet, selectedTerm, baselineDataSet);
    }

    /**
     * Creates the dataset.
     *
     * @param dataForChart
     * @param dates
     * @param arrSelectedTypes
     * @param baselineDate
     * @param baselineDataSet
     * @return the category dataset
     */
    private CategoryDataset createDataset(HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> dataForChart, ArrayList<String> dates, ArrayList<String> arrSelectedTypes, String baselineDate, DefaultCategoryDataset baselineDataSet) {

        int i = 0;
        HashMap<String, Integer> baselineValues = new HashMap<String, Integer>();
        for (String date : dates) {
            if (date.indexOf(baselineDate) > -1) {
                for (ProCtcQuestion proCtcQuestion : dataForChart.keySet()) {
                    ArrayList<ProCtcValidValue> proCtcValidValues = dataForChart.get(proCtcQuestion);
                    ProCtcValidValue proCtcValidValue = proCtcValidValues.get(i);
                    String questionType = proCtcQuestion.getProCtcQuestionType().getDisplayName();
                    baselineValues.put(questionType, proCtcValidValue.getDisplayOrder());
                }
            }
            i++;
        }

        i = 0;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (String date : dates) {
            for (ProCtcQuestion proCtcQuestion : dataForChart.keySet()) {
                ArrayList<ProCtcValidValue> proCtcValidValues = dataForChart.get(proCtcQuestion);
                ProCtcValidValue proCtcValidValue = proCtcValidValues.get(i);
                String questionType = proCtcQuestion.getProCtcQuestionType().getDisplayName();
                if (date.indexOf(baselineDate) > -1) {
                    date = StringUtils.replace(date, "<br/>", "");
                }
                if (arrSelectedTypes == null || arrSelectedTypes.size() == 0 || arrSelectedTypes.contains(questionType)) {
                    dataset.addValue(proCtcValidValue.getDisplayOrder(), questionType, date);
                    if (baselineValues.get(questionType) != null) {
                        baselineDataSet.addValue(baselineValues.get(questionType), questionType, date);
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
     * @param dataset         the dataset
     * @param selectedTerm
     * @param baselineDataSet
     * @return the j free chart
     */

    private JFreeChart createChart(CategoryDataset dataset, ProCtcTerm selectedTerm, DefaultCategoryDataset baselineDataSet) {

        String title = "";
        if (selectedTerm != null) {
            title = selectedTerm.getTerm();
        }
        JFreeChart chart = ChartFactory.createBarChart3D(
                title,
                "Date",
                "Response",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setDomainGridlinePaint(Color.gray);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.gray);
        chart.setBackgroundPaint(Color.white);

        plot.setDataset(1, baselineDataSet);
        plot.mapDatasetToRangeAxis(1, 1);
        LineAndShapeRenderer baselineRendrer = new LineAndShapeRenderer();
        plot.setRenderer(1, baselineRendrer);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        Range range = new Range(0, 5);


        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        final ValueAxis baselineAxis = new NumberAxis("Baseline");
        baselineAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        plot.setRangeAxis(1, baselineAxis);

        rangeAxis.setRange(range);
        baselineAxis.setRange(range);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setItemMargin(0.05);

        CategoryItemRenderer categoryItemRenderer = plot.getRenderer();
        categoryItemRenderer.setBaseItemLabelsVisible(true);

        ItemLabelPosition itemLabelPosition = new ItemLabelPosition(ItemLabelAnchor.INSIDE12, TextAnchor.CENTER_RIGHT, TextAnchor.CENTER_RIGHT, -Math.PI / 2.0);
        LabelGenerator lg = new LabelGenerator();
        for (int i = 0; i < dataset.getRowCount(); i++) {
            categoryItemRenderer.setSeriesItemLabelsVisible(i, true);
            categoryItemRenderer.setSeriesItemLabelGenerator(i, lg);
            renderer.setSeriesItemLabelFont(i, new Font(null, Font.BOLD, 12));
            renderer.setSeriesItemLabelPaint(i, Color.BLACK);
            renderer.setSeriesPaint(i, attributeColors[i]);
            categoryItemRenderer.setSeriesPositiveItemLabelPosition(i, itemLabelPosition);
            baselineRendrer.setSeriesShapesVisible(i, true);
            baselineRendrer.setSeriesStroke(i, new BasicStroke(2.0f));
            baselineRendrer.setSeriesOutlineStroke(i, new BasicStroke(2.0f));
            baselineRendrer.setSeriesPaint(i, attributeColors[i]);
        }

        LegendTitle legend1 = new LegendTitle(plot.getRenderer(0));
        legend1.setMargin(new RectangleInsets(2, 2, 2, 2));
        legend1.setBorder(1, 1, 1, 1);
        BlockContainer container = new BlockContainer(new BorderArrangement());
        container.add(legend1, RectangleEdge.LEFT);
        CompositeTitle legends = new CompositeTitle(container);
        legends.setPosition(RectangleEdge.BOTTOM);
        chart.addSubtitle(legends);
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

        return chart;

    }

    class LabelGenerator extends AbstractCategoryItemLabelGenerator
            implements CategoryItemLabelGenerator {

        public LabelGenerator() {
            super("", NumberFormat.getInstance());
        }

        public String generateLabel(CategoryDataset dataset,
                                    int series,
                                    int category) {
            String questionType = typesInSymptom.get(series);
            ProCtcQuestionType proCtcQuestionType = ProCtcQuestionType.getByCode(questionType);
            Number value = dataset.getValue(series, category);
            return proCtcQuestionType.getValidValues()[value.intValue()];
        }
    }
}
