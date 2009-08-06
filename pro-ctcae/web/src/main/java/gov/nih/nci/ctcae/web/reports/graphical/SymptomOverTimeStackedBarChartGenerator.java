package gov.nih.nci.ctcae.web.reports.graphical;

import gov.nih.nci.ctcae.core.domain.Arm;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;
import org.jfree.ui.RectangleEdge;
import org.jfree.text.TextUtilities;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 2:42:11 PM
 */
public class SymptomOverTimeStackedBarChartGenerator extends AbstractChartGenerator {
    private GenericRepository genericRepository;
    Color level0 = new Color(222, 216, 216);
    Color level1 = new Color(240, 249, 78);
    Color level2 = new Color(244, 187, 89);
    Color level3 = new Color(235, 122, 107);
    Color level4 = new Color(255, 54, 54);
    private HashMap<String, String> armPeriodCount = new HashMap<String, String>();

    public SymptomOverTimeStackedBarChartGenerator(String title, String domainAxisLabel, String rangeAxisLabel, String queryString) {
        super(title, domainAxisLabel, rangeAxisLabel, true, -1, queryString, "SYMPTOM_OVER_TIME_STACKED_BAR_CHART", false);
    }

    public CategoryDataset createDataSet(Object results) {
        TreeMap<Integer, TreeMap<String, TreeMap<Integer, Integer>>> data = (TreeMap<Integer, TreeMap<String, TreeMap<Integer, Integer>>>) results;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DecimalFormat df = new DecimalFormat("0");
        for (Integer armid : data.keySet()) {
            TreeMap<String, TreeMap<Integer, Integer>> temp = data.get(armid);
            for (String period : temp.keySet()) {
                TreeMap<Integer, Integer> map = temp.get(period);
                Integer sum = 0;
                for (Integer grade : map.keySet()) {
                    sum += map.get(grade);
                }
                for (Integer grade : map.keySet()) {
                    Float percentage = new Float(df.format(map.get(grade) * 100 / sum));
                    if (data.keySet().size() > 1) {
                        dataset.addValue(percentage, armid + " - " + grade, period);
                        armPeriodCount.put(armid + " - " + period, "[N=" + sum + "]");
                    } else {
                        dataset.addValue(percentage, armid + " - " + grade, period + " [N=" + sum + "]");
                    }
                }
            }
        }
        return dataset;
    }

    public JFreeChart getChartA(TreeMap<Integer, TreeMap<String, TreeMap<Integer, Integer>>> results, HashSet<Integer> selectedArms) {
        return createChart(createDataSet(results), selectedArms);
    }

    private JFreeChart createChart(CategoryDataset dataset, HashSet<Integer> selectedArms) {
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
        CategoryPlot plot = chart.getCategoryPlot();
        if (selectedArms.size() > 1) {
            GroupedStackedBarRenderer renderer = new ExtendedGroupedStackedBarRendered();
            KeyToGroupMap map = null;
            SubCategoryAxis domainAxis = new SubCategoryAxis(rangeAxisLabel);
            domainAxis.setCategoryMargin(0.10);
            for (Integer armid : selectedArms) {
                if (map == null) {
                    map = new KeyToGroupMap("G" + armid);
                }
                domainAxis.addSubCategory(genericRepository.findById(Arm.class, armid).getTitle());
                for (int i = 0; i < 5; i++) {
                    map.mapKeyToGroup(armid + " - " + i, "G" + armid);
                }
            }
            renderer.setSeriesToGroupMap(map);
            renderer.setItemMargin(0.10);
            plot.setDomainAxis(domainAxis);
            plot.setRenderer(renderer);
        }
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        formatChart(dataset, chart);
        for (int i = 0; i < selectedArms.size(); i++) {
            renderer.setSeriesPaint(i * 5, level0);
            renderer.setSeriesPaint(i * 5 + 1, level1);
            renderer.setSeriesPaint(i * 5 + 2, level2);
            renderer.setSeriesPaint(i * 5 + 3, level3);
            renderer.setSeriesPaint(i * 5 + 4, level4);
        }
        plot.setFixedLegendItems(createLegendItems());

        return chart;
    }

    private LegendItemCollection createLegendItems() {
        LegendItemCollection result = new LegendItemCollection();
        LegendItem item1 = new LegendItem("0", "-", null, null, Plot.DEFAULT_LEGEND_ITEM_BOX, level0);
        LegendItem item2 = new LegendItem("1", "-", null, null, Plot.DEFAULT_LEGEND_ITEM_BOX, level1);
        LegendItem item3 = new LegendItem("2", "-", null, null, Plot.DEFAULT_LEGEND_ITEM_BOX, level2);
        LegendItem item4 = new LegendItem("3", "-", null, null, Plot.DEFAULT_LEGEND_ITEM_BOX, level3);
        LegendItem item5 = new LegendItem("4", "-", null, null, Plot.DEFAULT_LEGEND_ITEM_BOX, level4);
        result.add(item1);
        result.add(item2);
        result.add(item3);
        result.add(item4);
        result.add(item5);
        return result;
    }

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    class ExtendedGroupedStackedBarRendered extends GroupedStackedBarRenderer {
        @Override
        public void drawItem(Graphics2D graphics2D, CategoryItemRendererState state, Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis, ValueAxis rangeAxis, CategoryDataset dataset, int row, int column, int pass) {
            super.drawItem(graphics2D, state, dataArea, plot, domainAxis, rangeAxis, dataset, row, column, pass);
//            Number dataValue = dataset.getValue(row, column);
//            if (dataValue == null) {
//                return;
//            }
//
//            double value = dataValue.doubleValue();
//
//            PlotOrientation orientation = plot.getOrientation();
//            double barW0 = domainAxis.getCategoryMiddle(column, getColumnCount(),
//                    dataArea, plot.getDomainAxisEdge()) - state.getBarWidth() / 2.0;
//
//            double positiveBase = 0.0;
//            double negativeBase = 0.0;
//
//            for (int i = 0; i < row; i++) {
//                Number v = dataset.getValue(i, column);
//                if (v != null) {
//                    double d = v.doubleValue();
//                    if (d > 0) {
//                        positiveBase = positiveBase + d;
//                    } else {
//                        negativeBase = negativeBase + d;
//                    }
//                }
//            }
//
//            double translatedBase;
//            double translatedValue;
//            RectangleEdge location = plot.getRangeAxisEdge();
//            if (value > 0.0) {
//                translatedBase = rangeAxis.valueToJava2D(positiveBase, dataArea,
//                        location);
//                translatedValue = rangeAxis.valueToJava2D(positiveBase + value,
//                        dataArea, location);
//            } else {
//                translatedBase = rangeAxis.valueToJava2D(negativeBase, dataArea,
//                        location);
//                translatedValue = rangeAxis.valueToJava2D(negativeBase + value,
//                        dataArea, location);
//            }
//            double barL0 = Math.min(translatedBase, translatedValue);
//            double barLength = Math.max(Math.abs(translatedValue - translatedBase),
//                    getMinimumBarLength());
//            Rectangle2D bar = new Rectangle2D.Double(barW0, barL0, state.getBarWidth(),
//                    barLength);
//
//            String armidGrade = (String) dataset.getRowKey(row);
//            String period = (String) dataset.getColumnKey(column);
//            String armId = armidGrade.substring(0, armidGrade.indexOf(" -"));
//            String n = armPeriodCount.get(armId + " - " + period);
////            float labelX = 0f;
////            float labelY = 0f;
////            categoryItemRendererState.
//            float labelX = (float) bar.getCenterX();
//            float labelY = (float) bar.getMinY() - 4.0f;
//            TextAnchor labelAnchor = TextAnchor.BOTTOM_CENTER;
//            graphics2D.setPaint(Color.black);
//            TextUtilities.drawRotatedString(
//                    n, graphics2D, labelX,
//                    labelY, labelAnchor, 0.0, TextAnchor.CENTER);
////            TextUtilities.drawRotatedString(n, graphics2D, -Math.PI / 2, labelX, labelY);

        }
    }
}